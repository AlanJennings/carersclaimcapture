package uk.gov.dwp.carersallowance.controller.submission;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import gov.dwp.carers.xml.helpers.XMLMessageHelper;
import gov.dwp.carers.xml.signing.SigningException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.w3c.dom.Document;
import uk.gov.dwp.carersallowance.encryption.ClaimEncryptionService;
import uk.gov.dwp.carersallowance.submission.SubmitClaimService;
import uk.gov.dwp.carersallowance.xml.XmlBuilder;
import uk.gov.dwp.carersallowance.database.TransactionIdService;

import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.utils.xml.ClaimXmlUtil;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList.MappingException;

/**
 * Controller to submit the overall claim
 * It does not expect any request parameters, and does not validate anything
 * It just creates the claim XML and sends it.  If is successful, then it redirects to a success page
 * otherwise it redirects to a retry page.  We may do a waiting page, as we currently do. (TODO)
 */
@Controller
public class SubmitClaimController {
    private static final Logger LOG = LoggerFactory.getLogger(SubmitClaimController.class);

    private static final String XML_MAPPING__CLAIM_CAREBREAK = "xml.mapping.claim.careBreak";

    private static final String CURRENT_PAGE       = "/submit-claim";
    private static final String SUCCESS_PAGE       = "/async-submitting";
//    private static final String FAILED_PAGE        = "/oh-no-its-all-gone-horribly-wrong";

    private SessionManager sessionManager;
    private TransactionIdService transactionIdService;
    private SubmitClaimService submitClaimService;
    private final ClaimEncryptionService claimEncryptionService;

    @Autowired
    public SubmitClaimController(final SessionManager sessionManager,
                                 final TransactionIdService transactionIdService,
                                 final ClaimEncryptionService claimEncryptionService,
                                 final SubmitClaimService submitClaimService) {
        this.sessionManager = sessionManager;
        this.transactionIdService = transactionIdService;
        this.claimEncryptionService = claimEncryptionService;
        this.submitClaimService = submitClaimService;
    }

    /**
     * This allows an easy to submit route, but is only temporary.
     */
    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(final HttpServletRequest request) {
        return postForm(request);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(final HttpServletRequest request) {

        LOG.trace("Starting SubmitClaimController.postForm");
        try {
            LOG.debug("request.getParameterMap() = {}", request.getParameterMap()); // log these jsut in case
            Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));

            String xml = buildClaimXml(session);
            submitClaimService.sendClaim(xml);
            return "redirect:" + SUCCESS_PAGE;
        } catch(IOException | InstantiationException | ParserConfigurationException | MappingException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw new RuntimeException("Oh no its all gone horribly wrong", e);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending SubmitClaimController.postForm");
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
     * @throws MappingException
     */
    private String buildClaimXml(final Session session) throws IOException, InstantiationException, ParserConfigurationException, MappingException {
        Parameters.validateMandatoryArgs(session, "session");

        claimEncryptionService.encryptClaim(session);

        Map<String, Object> sessionMap = new HashMap<>(session.getData());

// TODO these need setting up in the session at the start of the claim.
//        sessionMap.put("xmlVersion", xmlVersion);
//        sessionMap.put("appVersion", appVersion);
//        sessionMap.put("origin", origin);
//        sessionMap.put("language", language);
        final String transactionId = transactionIdService.getTransactionId();
        sessionMap.put("transactionId", transactionId);
        sessionMap.put("dateTimeGenerated", ClaimXmlUtil.currentDateTime("dd-MM-yyyy HH:mm"));

        final XmlBuilder xmlBuilder = new XmlBuilder("DWPBody", sessionMap);
        final String xml = xmlBuilder.render(true, false);
        LOG.debug("xml:{}", xml);
        final String signedXml = signClaim(xml, transactionId);
        LOG.debug("signedXml:{}", signedXml);
        return signedXml;
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
