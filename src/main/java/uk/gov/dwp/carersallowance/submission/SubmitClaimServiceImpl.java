package uk.gov.dwp.carersallowance.submission;

import gov.dwp.carers.monitor.Counters;
import gov.dwp.carers.xml.helpers.XMLMessageHelper;
import gov.dwp.carers.xml.signing.SigningException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import uk.gov.dwp.carersallowance.database.Status;
import uk.gov.dwp.carersallowance.database.TransactionIdService;
import uk.gov.dwp.carersallowance.email.EmailService;
import uk.gov.dwp.carersallowance.email.EmailServletResponse;
import uk.gov.dwp.carersallowance.encryption.ClaimEncryptionService;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.utils.C3Constants;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.utils.xml.ClaimXmlUtil;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
import uk.gov.dwp.carersallowance.xml.ServerSideResolveArgs;
import uk.gov.dwp.carersallowance.xml.XmlBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class SubmitClaimServiceImpl implements SubmitClaimService {
    private static final Logger LOG = LoggerFactory.getLogger(SubmitClaimServiceImpl.class);

    private final RestTemplate restTemplate;
    private final String crUrl;
    private final SessionManager sessionManager;
    private final TransactionIdService transactionIdService;
    private final MessageSource messageSource;
    private final Counters counters;
    private final EmailService emailService;

    private static final Integer JS_ENABLED = 1;
    private static final Integer JS_DISABLED = 0;
    private static final Integer FULL_CLAIM = 1;
    private static final Integer CHANGE_CIRCUMSTANCES = 2;

    @Inject
    public SubmitClaimServiceImpl(final RestTemplate restTemplate,
                                  @Value("${cr.url}") final String crUrl,
                                  final SessionManager sessionManager,
                                  final TransactionIdService transactionIdService,
                                  final MessageSource messageSource,
                                  final Counters counters,
                                  final EmailService emailService) {
        this.restTemplate = restTemplate;
        this.crUrl = crUrl;
        this.sessionManager = sessionManager;
        this.transactionIdService = transactionIdService;
        this.messageSource = messageSource;
        this.counters = counters;
        this.emailService = emailService;
    }

    @Override
    @Async
    public void sendClaim(final Session session, final String transactionId, final String emailBody) throws IOException, InstantiationException, ParserConfigurationException, XPathMappingList.MappingException {
        final String xml = buildClaimXml(session, transactionId);

        transactionIdService.insertTransactionStatus(transactionId, Status.GENERATED.getStatus(), getClaimType(session), getCircsType(session), null, (String) session.getAttribute("language"), getJsEnabled(session), null, null);

        sendClaim(xml, transactionId, session, emailBody);
        LOG.info("Sent claim for transactionId :{}", session.getAttribute(C3Constants.TRANSACTION_ID));
    }

    private Integer getJsEnabled(final Session session) {
        if ("true".equals(session.getAttribute("jsEnabled"))) {
            return JS_ENABLED;
        }
        return JS_DISABLED;
    }

    public Session getSession(final HttpServletRequest request) {
        return sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
    }

    public String retrieveTransactionId(final Session session) {
        final String transactionId = getTransactionId(session);
        saveTransactionId(transactionId, session);
        return transactionId;
    }

    private Integer getClaimType(final Session session) {
        if (C3Constants.CIRCS.equals(session.getAttribute(C3Constants.KEY))) {
            return CHANGE_CIRCUMSTANCES;
        }
        return FULL_CLAIM;
    }

    public void sendClaim(final String xml, final String transactionId, final Session session, final String emailBody) {
        try {
            transactionIdService.setTransactionStatusById(transactionId, Status.SUBMITTED.getStatus());

            final HttpHeaders headers = new HttpHeaders();
            final MediaType mediaTypeXml = new MediaType("application", "xml", StandardCharsets.UTF_8);
            headers.setContentType(mediaTypeXml);
            final HttpEntity<String> requestRest = new HttpEntity<>(xml, headers);

            final String submitUrl = crUrl + "/submission";
            LOG.info("Posting claim to :{}", submitUrl);
            final ResponseEntity<String> responseRest = restTemplate.exchange(submitUrl, HttpMethod.POST, requestRest, String.class);

            LOG.debug("RESPONSE:{}", responseRest.getStatusCode());
            processResponse(responseRest, transactionId, session, emailBody);
        } catch (RestClientException rce) {
            LOG.error("sendClaim error:", rce);
        } catch (Exception e) {
            LOG.error("sendClaim error:", e);
        }
    }

    private void processResponse(final ResponseEntity<String> responseRest, final String transactionId, final Session session, final String emailBody) throws Exception {
        if (responseRest.getStatusCode() == HttpStatus.OK) {
            processOk(responseRest, transactionId, session, emailBody);
        } else {
            processErrorResponse(responseRest, transactionId);
        }
    }

    private void processErrorResponse(final ResponseEntity<String> response, final String transactionId) {
        transactionIdService.setTransactionStatusById(transactionId, Status.SERVICE_UNAVAILABLE.getStatus());
        counters.incrementMetric("submission-error-status-" + response.getStatusCode().value());
    }

    private void processOk(final ResponseEntity<String> responseRest, final String transactionId, final Session session, final String emailBody) throws Exception {
        LOG.info("Successful submission [{}] - response status {} for transactionId:{}.", Status.SUCCESS.getStatus(), responseRest.getStatusCode(), transactionId);
        transactionIdService.setTransactionStatusById(transactionId, Status.SUCCESS.getStatus());
        counters.incrementMetric("submission-successful-count");

        //We send email after we update status and we verify that the submission has been successful
        emailService.sendEmail(emailBody, session, transactionId);

        sessionManager.removeSession(session.getSessionId());
    }

    /**
     * Build the Claim XML and add the digital signature
     * flatten the XML and send it
     *
     * @param session
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws ParserConfigurationException
     * @throws XPathMappingList.MappingException
     */
    private String buildClaimXml(final Session session, final String transactionId) throws IOException, InstantiationException, ParserConfigurationException, XPathMappingList.MappingException {
        Parameters.validateMandatoryArgs(session, "session");

        Map<String, Object> sessionMap = new HashMap<>(session.getData());

        sessionMap.put("dateTimeGenerated", ClaimXmlUtil.currentDateTime("dd-MM-yyyy HH:mm"));

        final XmlBuilder xmlBuilder = new XmlBuilder("DWPBody", sessionMap, messageSource, new ServerSideResolveArgs());
        final String xml = xmlBuilder.render(true, false);
        LOG.debug("xml:{}", xml);
        final String signedXml = signClaim(xml, transactionId);
        LOG.debug("signedXml:{}", signedXml);
        return signedXml;
    }

    private void saveTransactionId(final String transactionId, final Session session) {
        if (session.getAttribute(C3Constants.TRANSACTION_ID) == null) {
            session.setAttribute(C3Constants.TRANSACTION_ID, transactionId);
            sessionManager.saveSession(session);
        }
    }

    private String getTransactionId(final Session session) {
        String transactionId = (String) session.getAttribute(C3Constants.TRANSACTION_ID);
        if (StringUtils.isEmpty(transactionId)) {
            transactionId = transactionIdService.getTransactionId();
        }
        return transactionId;
    }

    private String signClaim(final String xmlClaim, final String transactionId) {
        final String xmlString = xmlClaim.replaceAll("<!--[\\s\\S]*?-->", "");
        final XMLMessageHelper xMLMessageHelper = new XMLMessageHelper();
        try {
            final Document document = xMLMessageHelper.createDocument(xmlString);
            xMLMessageHelper.normaliseDocument(document);
            final String xmlStringTransformed = xMLMessageHelper.transformXml(xMLMessageHelper.createDefaultTransformer(), document);
            return xMLMessageHelper.signXml(xmlStringTransformed, transactionId);
        } catch (Exception e) {
            LOG.error("Unable to sign xml:{}", e.getMessage(), e);
            throw new SigningException("Unable to sign xml", e);
        }
    }

    //need to get email body before request is overwritten
    public String getEmailBody(final HttpServletRequest request, final Session session) {
        try {
            EmailServletResponse response = new EmailServletResponse();
            response.setContentType("text/html");
            response.setStatus(200);
            setRequestParameters(request, session);
            request.getRequestDispatcher("/WEB-INF/views/" + (isClaim(session) ? C3Constants.CLAIM : C3Constants.CIRCS) + "Email.jsp").forward(request, response);
            return response.toString();
        } catch (Exception e) {
            LOG.error("Unable to send email", e);
            throw new RuntimeException("Unable to send email", e);
        }
    }

    private void setRequestParameters(final HttpServletRequest request, final Session session) {
        request.setAttribute("isOriginGB", isOriginGB(session));
        request.setAttribute(C3Constants.IS_CLAIM, isClaim(session));
        request.setAttribute("isEmployedByEmployer", isEmployedByEmployer(session));
        request.setAttribute("isEmployment", isEmployment(session));
        request.setAttribute("isSelfEmployed", isSelfEmployed(session));
        request.setAttribute("isCofcSelfEmployment", isCofcSelfEmployment(session));
        request.setAttribute("isCofcFinishedEmployment", isCofcFinishedEmployment(session));
        request.setAttribute("hasStatutorySickPay", hasStatutorySickPay(session));
        request.setAttribute("hasStatutoryPay", hasStatutoryPay(session));
        request.setAttribute("pensionStatementsRequired", pensionStatementsRequired(session));
        request.setAttribute("dateOfClaim_day", session.getAttribute("dateOfClaim_day"));
        request.setAttribute("dateOfClaim_month", session.getAttribute("dateOfClaim_month"));
        request.setAttribute("dateOfClaim_year", session.getAttribute("dateOfClaim_year"));
        request.setAttribute("versionSchemaTransactionInfo", versionSchemaTransactionInfo(session));
    }

    private Boolean isOriginGB(final Session session) {
        return "GB".equals(session.getAttribute("originTag"));
    }

    private String versionSchemaTransactionInfo(final Session session) {
        return session.getAttribute("appVersion") + " / " + session.getAttribute("xmlVersion") + " / " +  session.getAttribute(C3Constants.TRANSACTION_ID);
    }

    private Boolean pensionStatementsRequired(final Session session) {
        return C3Constants.YES.equals(session.getAttribute("selfEmployedPayPensionScheme")) || C3Constants.YES.equals(session.getAttribute("payPensionScheme"));
    }

    private Boolean hasStatutoryPay(final Session session) {
        return C3Constants.YES.equals(session.getAttribute("yourIncome_patmatadoppay"));
    }

    private Boolean hasStatutorySickPay(final Session session) {
        return C3Constants.YES.equals(session.getAttribute("yourIncome_sickpay"));
    }

    private Boolean isCofcFinishedEmployment(final Session session) {
        return C3Constants.YES.equals(session.getAttribute("circsEmploymentFinished"));
    }

    private Boolean isCofcSelfEmployment(final Session session) {
        return C3Constants.YES.equals(session.getAttribute("circsSelfEmployment"));
    }

    private Boolean isEmployment(final Session session) {
        return isClaimEmployment(session) || isCircsEmployment(session);
    }

    private Boolean isClaimEmployment(final Session session) {
        return C3Constants.YES.equals(getEmployment(session)) || C3Constants.YES.equals(getSelfEmployment(session));
    }

    private Boolean isEmployedByEmployer(final Session session) {
        return C3Constants.YES.equals(getEmployment(session));
    }

    private Boolean isSelfEmployed(final Session session) {
        return C3Constants.YES.equals(getSelfEmployment(session));
    }

    private Boolean isCircsEmployment(final Session session) {
        return C3Constants.YES.equals(session.getAttribute("circsEmployed"));
    }

    private String getSelfEmployment(final Session session) {
        return (String)session.getAttribute("beenSelfEmployedSince1WeekBeforeClaim");
    }

    private String getEmployment(final Session session) {
        return (String)session.getAttribute("beenEmployedSince6MonthsBeforeClaim");
    }

    public Boolean isClaim(final Session session) {
        return C3Constants.CLAIM.equals(session.getAttribute(C3Constants.KEY));
    }

    private Integer getCircsType(final Session session) {
        if (C3Constants.CIRCS.equals(session.getAttribute(C3Constants.KEY)) && session.getAttribute("changeTypeAnswer") != null) {
            switch((String)session.getAttribute("changeTypeAnswer")) {
                case "stoppedProvidingCare" :
                    return 0;
                case "changeOfAddress" :
                    return 1;
                case "incomeChanged" :
                    return 2;
                case "changePaymentDetails" :
                    return 3;
                case "somethingElse" :
                    return 4;
                case "patientAway" :
                    return 5;
                case "carerAway" :
                    return 5;
                default:
                    return null;
            }
        }
        return null;
    }
}
