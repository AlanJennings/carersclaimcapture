package uk.gov.dwp.carersallowance.xml;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import uk.gov.dwp.carersallowance.controller.XmlBuilder;
import uk.gov.dwp.carersallowance.utils.xml.ClaimXmlUtil;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
import uk.gov.dwp.carersallowance.utils.xml.XmlPrettyPrinter;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.carersallowance.utils.xml.ClaimXmlUtil.getNodeValue;

public class ClaimHeaderItemsTest {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimHeaderItemsTest.class);

    private Document document;
    private XPath xpath;
    private String appVersion = "3.14";
    private String xmlVersion = "0.27";
    private String transactionId = "16011234";
    private String origin = "GB";
    private String language = "English";

    @Before
    public void setUp() throws Exception {
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put("transactionId", transactionId);
        sessionMap.put("transactionIdAttr", transactionId);
        sessionMap.put("dateTimeGenerated", ClaimXmlUtil.currentDateTime("dd-MM-yyyy HH:mm"));
        sessionMap.put("xmlVersion", xmlVersion);
        sessionMap.put("appVersion", appVersion);
        sessionMap.put("origin", origin);
        sessionMap.put("language", language);

        XmlBuilder xmlBuilder = new XmlBuilder("DWPBody", sessionMap);
        String xml = xmlBuilder.render(true, false);
        document = xmlBuilder.getDocument();
        xpath = XPathFactory.newInstance().newXPath();
    }

    @Test
    public void justDumpXml() {
        try {
            System.out.println(XmlPrettyPrinter.prettyPrintXml(document.getFirstChild()));
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkClaimXmlContainsC3AppVersion() {
        assertThat(getNodeValue(document, "/DWPBody/Version"), is(appVersion));
    }

    @Test
    public void checkClaimXmlContainsSchemaVersion() {
        assertThat(getNodeValue(document, "/DWPBody/ClaimVersion"), is(xmlVersion));
    }

    @Test
    public void checkClaimXmlContainsOrigin() {
        assertThat(getNodeValue(document, "/DWPBody/Origin"), is(origin));
    }

    @Test
    public void checkClaimXmlContainsDWPCATransactionIdAttribute() {
        assertThat(getNodeValue(document, "/DWPBody/DWPCATransaction/@id"), is(transactionId));
    }

    @Test
    public void checkClaimXmlContainsTransactionId() {
        assertThat(getNodeValue(document, "/DWPBody/DWPCATransaction/TransactionId"), is(transactionId));
    }

    @Test
    public void checkClaimXmlContainsDateTimeGeneratedWithCorrectFormat() {
        String dateTimeGenerated = getNodeValue(document, "/DWPBody/DWPCATransaction/DateTimeGenerated");
        long dtgSecs = 0;
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.getDefault());
            dtgSecs = LocalDateTime.parse(dateTimeGenerated, dtf).toEpochSecond(ZoneOffset.UTC);
        } catch (Exception e) {
            LOG.error("Exception parsing DateTimeGenerated:{}", dateTimeGenerated, e);
        }
        long nowsecs = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        assertThat("Datetime generated is after 70 secs ago", dtgSecs, greaterThan(nowsecs - 70));
        assertThat("Datetime generated before now", dtgSecs, lessThanOrEqualTo(nowsecs));
    }

    @Test
    public void checkClaimXmlContainsLanguage() {
        assertThat(getNodeValue(document, "/DWPBody/DWPCATransaction/LanguageUsed"), is(language));
    }
}