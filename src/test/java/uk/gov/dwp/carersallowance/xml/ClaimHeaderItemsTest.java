package uk.gov.dwp.carersallowance.xml;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import uk.gov.dwp.carersallowance.utils.xml.ClaimXmlUtil;
import uk.gov.dwp.carersallowance.utils.xml.XmlPrettyPrinter;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
    private String schemaVersion = "0.27";
    private String transactionId = "16011234";
    private String origin = "GB";
    private String language = "English";

    @Before
    public void setUp() throws Exception {
        // initialise claim header information only, claim is blank in these tests no matter.
        ClaimXml claimXml = new ClaimXml();
        document = claimXml.buildXml(null, transactionId, appVersion, schemaVersion, origin, language);
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
        assertThat(getNodeValue(document, "/DWPBody/ClaimVersion"), is(schemaVersion));
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