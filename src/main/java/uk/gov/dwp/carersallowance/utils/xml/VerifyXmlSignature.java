package uk.gov.dwp.carersallowance.utils.xml;

import java.io.IOException;

import gov.dwp.carers.xml.signing.XmlSignature;
import gov.dwp.carers.xml.signing.XmlSignatureFactory;

/**
 * Note: the xml signature is based on the String bytes, not the XML itself,
 *       so it is sensitive to layout, whitespace etc.
 * @author drh
 * TODO add some code to reformat the XML before it is signed, to be called separately from the signing, almot definitly no whitespace (i.e. compact mode)
 *
 */
public class VerifyXmlSignature {
    private static final String KEYSTORE_SYSTEM_KEY = "carers.keystore";
    private static final String KEYSTORE            = "/Users/drh/carerskeystore";

    private XmlSignature xmlSignature;

    public VerifyXmlSignature(String keystoreLocation) {
        System.setProperty(KEYSTORE_SYSTEM_KEY, keystoreLocation);
        xmlSignature = XmlSignatureFactory.buildRsaSha1Generator();
    }

    public Boolean verifyXmlSignature(String xml) throws IOException {
        Boolean valid = xmlSignature.verifySignature(xml);
        return valid;
    }
}
