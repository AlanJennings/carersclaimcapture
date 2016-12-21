package uk.gov.dwp.carersallowance.controller.submission;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.controller.XmlBuilder;
import uk.gov.dwp.carersallowance.database.TransactionIdService;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.session.SessionManager.Session;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
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

    private static final String XML_MAPPING__CLAIM           = "xml.mapping.claim";
    private static final String XML_MAPPING__CLAIM_CAREBREAK = "xml.mapping.claim.careBreak";

    private static final String CURRENT_PAGE       = "/submit-claim";
    private static final String SUCCESS_PAGE       = "/async-submitting";
//    private static final String FAILED_PAGE        = "/oh-no-its-all-gone-horribly-wrong";

    private SessionManager       sessionManager;
    private MessageSource        messageSource;
    private TransactionIdService transactionIdService;

    @Autowired
    public SubmitClaimController(SessionManager sessionManager, MessageSource messageSource, TransactionIdService transactionIdService) {
        this.sessionManager = sessionManager;
        this.messageSource = messageSource;
        this.transactionIdService = transactionIdService;
    }

    /**
     * This allows an easy to submit route, but is only temporary.
     */
    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String showForm(HttpServletRequest request) {
        return postForm(request);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request) {

        LOG.trace("Starting SubmitClaimController.postForm");
        try {
            LOG.debug("request.getParameterMap() = {}", request.getParameterMap()); // log these jsut in case
            Session session = sessionManager.createFromHttpSession(request.getSession());

            String xml = buildClaimXml(session);
            //return xml;
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
    private String buildClaimXml(Session session) throws IOException, InstantiationException, ParserConfigurationException, MappingException {
        Parameters.validateMandatoryArgs(session, "session");

        Map<String, XPathMappingList> mappings = new HashMap<>();
        loadXPathMappings(mappings, null, XML_MAPPING__CLAIM);
        loadXPathMappings(mappings, "CareBreak", XML_MAPPING__CLAIM_CAREBREAK);

        Map<String, Object> sessionMap = new HashMap<>(session.getData());

// TODO these need setting up in the session at the start of the claim.
//        sessionMap.put("xmlVersion", xmlVersion);
//        sessionMap.put("appVersion", appVersion);
//        sessionMap.put("origin", origin);
//        sessionMap.put("language", language);

        sessionMap.put("transactionId", transactionIdService.getTransactionId());
        sessionMap.put("dateTimeGenerated", currentDateTime("dd-MM-yyyy HH:mm"));

        Map<String, String> namespaces = new HashMap<>();
        namespaces.put("xmlns", "http://www.govtalk.gov.uk/dwp/carers-allowance");
        namespaces.put("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
        namespaces.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaces.put("xsi:schemaLocation", "http://www.govtalk.gov.uk/dwp/carers-allowance file:/future/schema/ca/CarersAllowance_Schema.xsd");

        XmlBuilder xmlBuilder = new XmlBuilder("DWPBody", namespaces, sessionMap, mappings);
        String xml = xmlBuilder.render(true, false);
        LOG.debug("xml:{}", xml);
        return xml;
    }

    private void loadXPathMappings(Map<String, XPathMappingList> mappings, String name, String resourceName)
            throws IOException, MappingException {

        URL claimTemplateUrl = this.getClass().getClassLoader().getResource(XML_MAPPING__CLAIM);
        List<String> xmlMappings = readLines(claimTemplateUrl);
        XPathMappingList valueMappings = new XPathMappingList();
        valueMappings.add(xmlMappings);
        mappings.put(null, valueMappings);
    }

    public static String currentDateTime(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String now = dateFormat.format(new Date());
        return now;
    }

    public static List<String> readLines(URL url) throws IOException {
        if(url == null) {
            return null;
        }

        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            List<String> list = IOUtils.readLines(inputStream, Charset.defaultCharset());
            return list;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

    }

}
