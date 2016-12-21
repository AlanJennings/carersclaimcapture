package uk.gov.dwp.carersallowance.xml;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import uk.gov.dwp.carersallowance.utils.xml.XmlPrettyPrinter;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;


public class ClaimHeaderItemsTest {
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
        assertThat(getNodeValue("/DWPBody/Version"), is(appVersion));
    }

    @Test
    public void checkClaimXmlContainsSchemaVersion() {
        assertThat(getNodeValue("/DWPBody/ClaimVersion"), is(schemaVersion));
    }

    @Test
    public void checkClaimXmlContainsOrigin() {
        assertThat(getNodeValue("/DWPBody/Origin"), is(origin));
    }

    @Test
    public void checkClaimXmlContainsDWPCATransactionIdAttribute() {
        assertThat(getNodeValue("/DWPBody/DWPCATransaction/@id"), is(transactionId));
    }

    @Test
    public void checkClaimXmlContainsTransactionId() {
        assertThat(getNodeValue("/DWPBody/DWPCATransaction/TransactionId"), is(transactionId));
    }

    @Test
    public void checkClaimXmlContainsDateTimeGeneratedWithCorrectFormat() {
        String dateTimeGenerated = getNodeValue("/DWPBody/DWPCATransaction/DateTimeGenerated");
        long dtgMillis = 0;
        try {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
            DateTime dt = dtf.parseDateTime(dateTimeGenerated);
            dtgMillis = dt.getMillis();
        } catch (Exception e) {
            System.out.println("Exception parsing DateTimeGenerated:" + dateTimeGenerated + " " + e.toString());
        }
        long nowmillis = DateTime.now().getMillis();
        assertThat("Datetime generated is after 70 secs ago", dtgMillis, greaterThan(nowmillis - 70000));
        assertThat("Datetime generated before now", dtgMillis, lessThanOrEqualTo(nowmillis));
    }

    @Test
    public void checkClaimXmlContainsLanguage() {
        assertThat(getNodeValue("/DWPBody/DWPCATransaction/LanguageUsed"), is(language));
    }

    private String getNodeValue(String nodepath) {
        String nodevalue = null;
        try {
            nodevalue = xpath.compile(nodepath).evaluate(document);
        } catch (XPathException e) {
            System.out.println("Exception compiling xpath:" + e.toString());
        }
        return nodevalue;
    }
}