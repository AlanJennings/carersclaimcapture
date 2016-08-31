package uk.gov.dwp.carersallowance.utils.xml;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

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

    public static void main(String[] args) throws IOException {
        System.out.println("Started");
        // read the XML, then add the signature
        File[] files;
        if(args.length == 0) {
//            files = new File[] {new File("/Users/drh/development-java/CarersClaimCapture/CarersClaimCapture/resources/unchangedDefaultClaim/DefaultClaim.xml.raw")};
            files = new File[] {new File("/Users/drh/development-java/CarersClaimCapture/CarersClaimCapture/resources/defaultClaim/DefaultClaim.xml")};
        } else {
            files = new File[args.length];
            for(int index = 0; index < args.length; index++) {
                files[index] = new File(args[index]);
            }
        }

        VerifyXmlSignature claimUtils = new VerifyXmlSignature(KEYSTORE);
        for(File file: files) {
            String xml = FileUtils.readFileToString(file, Charset.defaultCharset());
            Boolean valid = claimUtils.verifyXmlSignature(xml);
            System.out.println("XML File signature valid = " + valid);
        }
        System.out.println("Complete");
    }
}
