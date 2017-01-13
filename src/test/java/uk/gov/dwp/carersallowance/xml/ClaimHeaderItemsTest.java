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

import javax.xml.xpath.XPath;
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

public class ClaimHeaderItemsTest {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimHeaderItemsTest.class);

    private XmlBuilder xmlBuilder;
    private XPath xpath;
    private String appVersion = "3.14";
    private String xmlVersion = "0.27";
    private String transactionId = "16011234";
    private String origin = "GB";
    private String language = "English";

    @Mock
    private MessageSource messageSource;

    @Mock
    private ServerSideResolveArgs serverSideResolveArgs;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(C3Constants.TRANSACTION_ID, transactionId);
        sessionMap.put("transactionIdAttr", transactionId);
        sessionMap.put("dateTimeGenerated", ClaimXmlUtil.currentDateTime("dd-MM-yyyy HH:mm"));
        sessionMap.put("xmlVersion", xmlVersion);
        sessionMap.put("appVersion", appVersion);
        sessionMap.put("origin", origin);
        sessionMap.put("language", language);

        xmlBuilder = new XmlBuilder("DWPBody", sessionMap, messageSource, serverSideResolveArgs);
        String xml = xmlBuilder.render(true, false);
        xpath = XPathFactory.newInstance().newXPath();
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
    public void checkClaimXmlContainsSchemaVersion() {
        assertThat(xmlBuilder.getNodeValue("/DWPBody/Version"), is(xmlVersion));
    }

    @Test
    public void checkClaimXmlContainsC3AppVersion() {
        assertThat(xmlBuilder.getNodeValue("/DWPBody/ClaimVersion"), is(appVersion));
    }

    @Test
    public void checkClaimXmlContainsOrigin() {
        assertThat(xmlBuilder.getNodeValue("/DWPBody/Origin"), is(origin));
    }

    @Test
    public void checkClaimXmlContainsDWPCATransactionIdAttribute() {
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/@id"), is(transactionId));
    }

    @Test
    public void checkClaimXmlContainsTransactionId() {
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/TransactionId"), is(transactionId));
    }

    @Test
    public void checkClaimXmlContainsDateTimeGeneratedWithCorrectFormat() {
        String dateTimeGenerated = xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DateTimeGenerated");
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
        assertThat(xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/LanguageUsed"), is(language));
    }
}