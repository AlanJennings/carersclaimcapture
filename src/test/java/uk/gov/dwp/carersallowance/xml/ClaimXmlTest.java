package uk.gov.dwp.carersallowance.xml;

import gov.dwp.carers.xml.helpers.XMLExtractor;
import org.junit.Test;
import org.mockito.Mock;
import org.w3c.dom.Document;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.utils.xml.XmlPrettyPrinter;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import java.util.Arrays;


public class ClaimXmlTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    SessionManager sessionManager;
/*
    @Test
    public void testXml() {
        System.out.println("Testing XML...");
        try {
            ClaimXml claimXml = new ClaimXml();
            // buildXml(Object claim, String transactionId, String schemaLocation, String version, String claimVersion, String origin, String language)
            Document document = claimXml.buildXml(null, "1234", "1.01", "7.23", "GB", "English");
            System.out.println("---DOCUMENT FOLLOWS-----");
            System.out.println(XmlPrettyPrinter.prettyPrintXml(document.getFirstChild()));
        } catch (Exception e) {
            System.out.println("Exception from test:" + e.toString());
        }
    }

    @Test
    public void checkClaimXmlContainsVersion() {
        System.out.println("Testing XML...");

        try {
            ClaimXml claimXml = new ClaimXml();
            // buildXml(Object claim, String transactionId, String schemaLocation, String version, String claimVersion, String origin, String language)
            Document document = claimXml.buildXml(null, "1234", "1.01", "7.23", "GB", "English");
            String xml=XmlPrettyPrinter.prettyPrintXml(document.getFirstChild());
            //           "DWPBody//DWPCATransaction"
            //           "DWPBody//DWPCATransaction//TransactionId"
            System.out.println("XML...."+xml);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String version=xpath.compile("/DWPBody/ClaimVersion").evaluate(document);
            System.out.println("Got version:"+version);
            XMLExtractor x=new XMLExtractor();
            String v=x.getTextFromXmlNode(document, "DWPBody", Arrays.asList("ClaimVersion"));
            System.out.println(("v:"+v));
        } catch (Exception e) {
            System.out.println("Exception from test:" + e.toString());
        }
    }

    @Test
    public void claimXmlContainsTransactionId() {
        System.out.println("Testing XML...");

        try {
            ClaimXml claimXml = new ClaimXml();
            // buildXml(Object claim, String transactionId, String schemaLocation, String version, String claimVersion, String origin, String language)
            Document document = claimXml.buildXml(null, "1234", "1.01", "7.23", "GB", "English");
            String xml=XmlPrettyPrinter.prettyPrintXml(document.getFirstChild());
            //           "DWPBody//DWPCATransaction"
            //           "DWPBody//DWPCATransaction//TransactionId"
            XPath xpath = XPathFactory.newInstance().newXPath();
            String transactionId=xpath.compile("/DWPBody/DWPCATransaction/TransactionId").evaluate(document);
            System.out.println("Got transactionId:"+transactionId);
            XMLExtractor x=new XMLExtractor();
            x.getTextFromXmlNode(document, "DWPCATransaction", Arrays.asList("DWPCATransaction", "DWPCAClaim"));
            System.out.println(("X:"+x));
        } catch (Exception e) {
            System.out.println("Exception from test:" + e.toString());
        }
    }
*/
}