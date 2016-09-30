package uk.gov.dwp.carersallowance.utils.xml;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import gov.dwp.carers.xml.signing.XmlSignature;
import gov.dwp.carers.xml.signing.XmlSignatureFactory;

/**
 * TODO add some code to reformat the XML before it is signed, to be called separately from the signing, almot definitly no whitespace (i.e. compact mode)
 */
public class SignXml {
    private static final String KEYSTORE_SYSTEM_KEY = "carers.keystore";
    private static final String KEYSTORE            = "/Users/drh/carerskeystore";

    private XmlSignature xmlSignature;

    public SignXml(String keystoreLocation) {
        System.setProperty(KEYSTORE_SYSTEM_KEY, keystoreLocation);
        xmlSignature = XmlSignatureFactory.buildRsaSha1Generator();
    }

    public String signClaimXml(String xml) throws IOException {

        // e.g <TransactionId>16070000416</TransactionId>
        String[] transactionIds = StringUtils.substringsBetween(xml, "<TransactionId>", "</TransactionId>");
        if(transactionIds == null || transactionIds.length == 0) {
            throw new IOException("No transactionId in XML");
        }
        if(transactionIds.length > 1) {
            throw new IOException("Too many matches for transaction Id");
        }

        String transactionId = transactionIds[0];
        String signedXml = xmlSignature.sign(xml, transactionId);

        return signedXml;
    }

    private static File deriveNewFilename(File file, String newFilenameElement) {
        if(file == null || StringUtils.isBlank(newFilenameElement)) {
            return file;
        }

        File parent = file.getParentFile();
        String filename = file.getName();
        int pos = filename.lastIndexOf(".");
        String extension;
        if(pos != -1) {
            extension = "." + filename.substring(pos + 1);
            filename = filename.substring(0,  pos);
        } else {
            extension = "";
        }
        String newFilename = filename + "." + newFilenameElement + extension;
        File newFile = new File(parent, newFilename);

        return newFile;
    }
}
