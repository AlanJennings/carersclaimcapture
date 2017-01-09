package uk.gov.dwp.carersallowance.submission;

import gov.dwp.carers.xml.helpers.XMLMessageHelper;
import gov.dwp.carers.xml.signing.SigningException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import uk.gov.dwp.carersallowance.database.Status;
import uk.gov.dwp.carersallowance.database.TransactionIdService;
import uk.gov.dwp.carersallowance.encryption.ClaimEncryptionService;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.utils.xml.ClaimXmlUtil;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
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

    private static final String TRANSACTION_ID = "transactionId";
    private final RestTemplate restTemplate;
    private final String crUrl;
    private final SessionManager sessionManager;
    private final TransactionIdService transactionIdService;
    private final ClaimEncryptionService claimEncryptionService;

    private static final Integer JS_ENABLED = 1;
    private static final Integer JS_DISABLED = 0;
    private static final Integer FULL_CLAIM = 1;
    private static final Integer CHANGE_CIRCUMSTANCES = 2;

    @Inject
    public SubmitClaimServiceImpl(final RestTemplate restTemplate,
                                  @Value("${cr.url}") final String crUrl,
                                  final SessionManager sessionManager,
                                  final TransactionIdService transactionIdService,
                                  final ClaimEncryptionService claimEncryptionService) {
        this.restTemplate = restTemplate;
        this.crUrl = crUrl;
        this.sessionManager = sessionManager;
        this.transactionIdService = transactionIdService;
        this.claimEncryptionService = claimEncryptionService;
    }

    @Override
    @Async
    public void sendClaim(final HttpServletRequest request) throws IOException, InstantiationException, ParserConfigurationException, XPathMappingList.MappingException {
        final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));

        String transactionId = getTransactionId(session);
        saveTransactionId(transactionId, session);

        String xml = buildClaimXml(session, transactionId);

        //transactionIdService.insertTransactionStatus(transactionId, "0100", type, thirdParty, circsType, lang, jsEnabled, email, saveForLaterEmail);
        transactionIdService.insertTransactionStatus(transactionId, Status.GENERATED.getStatus(), getClaimType(session), null, null, (String)session.getAttribute("language"), getJsEnabled(session), null, null);

        sendClaim(xml, transactionId);
        LOG.info("Sent claim for transactionId :{}", session.getAttribute(TRANSACTION_ID));
    }

    private Integer getJsEnabled(final Session session) {
        if ("true".equals(session.getAttribute("jsEnabled"))) {
            return JS_ENABLED;
        }
        return JS_DISABLED;
    }

    private Integer getClaimType(final Session session) {
        if ("circs".equals(session.getAttribute("key"))) {
            return CHANGE_CIRCUMSTANCES;
        }
        return FULL_CLAIM;
    }

    public void sendClaim(final String xml, final String transactionId) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            final MediaType mediaTypeXml = new MediaType("application", "xml", StandardCharsets.UTF_8);
            headers.setContentType(mediaTypeXml);
            final HttpEntity<String> request = new HttpEntity<>(xml, headers);
            LOG.info("Posting claim to :{}", crUrl);
            transactionIdService.setTransactionStatusById(transactionId, Status.SUBMITTED.getStatus());
            final ResponseEntity<String> response = restTemplate.exchange(crUrl + "/submission", HttpMethod.POST, request, String.class);
            LOG.debug("RESPONSE:{}", response.getStatusCode());
            //TODO Handle response from submit to claim received
            processResponse(response, transactionId);
        } catch (RestClientException rce) {
            LOG.error("sendClaim error:", rce);
        } catch (Exception e) {
            LOG.error("sendClaim error:", e);
        }
    }

    private void processResponse(final ResponseEntity<String> response, final String transactionId) {
        if (response.getStatusCode() == HttpStatus.OK) {
            transactionIdService.setTransactionStatusById(transactionId, Status.SUCCESS.getStatus());
            //Counters.incrementClaimSubmissionCount submission-successful-count
        } else {
            transactionIdService.setTransactionStatusById(transactionId, Status.SERVICE_UNAVAILABLE.getStatus());
            //Counters.incrementSubmissionErrorStatus - submission-error-status- + status
        }
    }

    /**
     * Build the Claim XML and add the digital signature
     * flatten the XML and send it
     * @param session
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws ParserConfigurationException
     * @throws XPathMappingList.MappingException
     */
    private String buildClaimXml(final Session session, final String transactionId) throws IOException, InstantiationException, ParserConfigurationException, XPathMappingList.MappingException {
        Parameters.validateMandatoryArgs(session, "session");

        claimEncryptionService.encryptClaim(session);

        Map<String, Object> sessionMap = new HashMap<>(session.getData());

// TODO these need setting up in the session at the start of the claim.
//        sessionMap.put("xmlVersion", xmlVersion);
//        sessionMap.put("appVersion", appVersion);
//        sessionMap.put("origin", origin);
//        sessionMap.put("language", language);

        sessionMap.put("transactionId", transactionId);
        sessionMap.put("dateTimeGenerated", ClaimXmlUtil.currentDateTime("dd-MM-yyyy HH:mm"));

        final XmlBuilder xmlBuilder = new XmlBuilder("DWPBody", sessionMap);
        final String xml = xmlBuilder.render(true, false);
        LOG.debug("xml:{}", xml);
        final String signedXml = signClaim(xml, transactionId);
        LOG.debug("signedXml:{}", signedXml);
        return signedXml;
    }

    private void saveTransactionId(final String transactionId, final Session session) {
        if (session.getAttribute(TRANSACTION_ID) == null) {
            session.setAttribute(TRANSACTION_ID, transactionId);
            sessionManager.saveSession(session);
        }
    }

    private String getTransactionId(final Session session) {
        String transactionId = (String)session.getAttribute(TRANSACTION_ID);
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
}
