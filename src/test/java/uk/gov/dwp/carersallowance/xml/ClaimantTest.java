package uk.gov.dwp.carersallowance.xml;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import uk.gov.dwp.carersallowance.utils.C3Constants;
import uk.gov.dwp.carersallowance.utils.xml.ClaimXmlUtil;
import uk.gov.dwp.carersallowance.utils.xml.XmlPrettyPrinter;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ClaimantTest {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimantTest.class);

    private XmlClaimBuilder xmlBuilder;
    private String appVersion = "3.14";
    private String xmlVersion = "0.27";
    private String transactionId = "16011234";
    private String origin = "GB";
    private String language = "English";

    private Map<String, Object> sessionMap;

    @Mock
    private MessageSource messageSource;

    @Mock
    private ServerSideResolveArgs serverSideResolveArgs;

    @Before
    public void setUp() throws Exception {
        sessionMap = new HashMap<>();
        sessionMap.put(C3Constants.TRANSACTION_ID, transactionId);
        sessionMap.put("transactionIdAttr", transactionId);
        sessionMap.put("dateTimeGenerated", ClaimXmlUtil.currentDateTime("dd-MM-yyyy HH:mm"));
        sessionMap.put("xmlVersion", xmlVersion);
        sessionMap.put("appVersion", appVersion);
        sessionMap.put("origin", origin);
        sessionMap.put("language", language);

        sessionMap.put("benefitsAnswer", "AFIP");
        sessionMap.put("dateOfClaim_day", "31");
        sessionMap.put("dateOfClaim_month", "12");
        sessionMap.put("dateOfClaim_year", "2016");
        xmlBuilder = new XmlClaimBuilder("DWPBody", messageSource, serverSideResolveArgs, "xml.mapping.claim", 65);
        xmlBuilder.loadValuesIntoXML(sessionMap);
    }

    @Test
    public void justDumpXml() {
        try {
            LOG.debug(XmlPrettyPrinter.prettyPrintXml(xmlBuilder.getDocument().getFirstChild()));
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkClaimXmlContainsQualifyingBenefit() {
        //        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/QualifyingBenefit/QuestionLabel"), is("Benefits question"));
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/QualifyingBenefit/Answer"), is("AFIP"));
    }

    @Test
    public void checkClaimXmlContainsDateOfClaim() {
        LOG.debug("DATEOFCLAIM node:{}", xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/DateOfClaim"));
        //        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/DateOfClaim/QuestionLabel"), is("Benefits question"));
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/DateOfClaim/Answer"), is("31-12-2016"));
    }

    @Test
    public void checkClaimXmlDoesContainMiddleNameQuestionIfAnswered() throws Exception {
        sessionMap.put("carerMiddleName", "Freddy");
        XmlClaimBuilder xmlBuilder = new XmlClaimBuilder("DWPBody", messageSource, serverSideResolveArgs, "xml.mapping.claim", 65);
        xmlBuilder.loadValuesIntoXML(sessionMap);
        LOG.debug("Othernames node1:{}", xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/Claimant/MiddleNames"));
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/Claimant/MiddleNames/QuestionLabel").length() > 0, is(true));
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/Claimant/MiddleNames/Answer"), is("Freddy"));
    }

    @Test
    public void checkClaimXmlDoesntContainMiddleNameQuestionIfNotAnswered() throws Exception {
        XmlClaimBuilder xmlBuilder = new XmlClaimBuilder("DWPBody", messageSource, serverSideResolveArgs, "xml.mapping.claim", 65);
        xmlBuilder.loadValuesIntoXML(sessionMap);
        LOG.debug("Othernames node2:{}", xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/Claimant/MiddleNames"));
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/Claimant/MiddleNames/QuestionLabel"), is(""));
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/Claimant/MiddleNames/Answer"), is(""));
    }


    @Test
    public void checkClaimXmlContainsAddress() throws Exception {
        sessionMap.put("carerAddressLineOne", "22 Acacia Ave");
        sessionMap.put("carerPostcode", "PR12AA");
        XmlClaimBuilder xmlBuilder = new XmlClaimBuilder("DWPBody", messageSource, serverSideResolveArgs, "xml.mapping.claim", 65);
        xmlBuilder.loadValuesIntoXML(sessionMap);
        LOG.debug("Address node1:", xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/Claimant/Address"));
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/Claimant/Address/QuestionLabel").length() > 0, is(true));
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/Claimant/Address/Answer/Line"), is("22 Acacia Ave"));
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/Claimant/Address/Answer/PostCode"), is("PR12AA"));
    }

    @Test
    public void checkyesIsMappedToYesAndnoIsMappedToNo() throws Exception {
        sessionMap.put("beforeClaimCaring", C3Constants.YES);
        sessionMap.put("carerWantsEmailContact", C3Constants.NO);
        XmlClaimBuilder xmlBuilder = new XmlClaimBuilder("DWPBody", messageSource, serverSideResolveArgs, "xml.mapping.claim", 65);
        xmlBuilder.loadValuesIntoXML(sessionMap);
        assertThat(xmlBuilder.getNodeValue("DWPBody/DWPCATransaction/DWPCAClaim/Claimant/Cared35HoursBefore/Answer"), is("Yes"));
        assertThat(xmlBuilder.getNodeValue("DWPBody/DWPCATransaction/DWPCAClaim/Claimant/WantsContactEmail/Answer"), is("No"));
    }
}