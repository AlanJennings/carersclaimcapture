package uk.gov.dwp.carersallowance.utils.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

public class ClaimXmlUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimXmlUtil.class);

    static public String getNodeValue(Document document, String nodepath) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String nodevalue = null;
        try {
            nodevalue = xpath.compile(nodepath).evaluate(document);
        } catch (XPathException e) {
            LOG.error("Exception compiling xpath:{}", e.toString(), e);
        }
        return nodevalue;
    }

//    private static final Logger  LOG                  = LoggerFactory.getLogger(ClaimXmlUtil.class);
//
//    private static final String KEYSTORE_SYSTEM_KEY = "carers.keystore";
//    private static final String KEYSTORE            = "/Users/drh/carerskeystore";
//
//    private static final String  UTF_8                = "UTF-8";
//    private static final boolean PRETTY_PRINT_XML     = true;
//    private static final int     BLOCK_SIZE           = 64;
//
//    private String xml;
//
//    public ClaimXmlUtil(String filename) throws IOException {
//        System.setProperty(KEYSTORE_SYSTEM_KEY, KEYSTORE);
//        if(filename != null) {
//            xml = loadXml(filename);
//        }
//    }
//
//    public String getXml() {
//        return xml;
//    }
//
//    public String format(String xml) throws InstantiationException {
//        return XmlPrettyPrinter.formatXml(xml, true, true);
//    }
//
//    public String unformat(String xml) throws InstantiationException {
//        return XmlPrettyPrinter.formatXml(xml, false, false);
//    }
//
//    public String encryptFields(String xml) {
//        return null;
//
//    }
//
//    public String decryptFields(String xml) throws InstantiationException {
//        if(xml == null) {
//            return null;
//        }
//
//        Node rootNode = XmlPrettyPrinter.stringToNode(xml);
//        Node decrypted = decryptFields(rootNode);
//
//        String result = XmlPrettyPrinter.xmlToString(decrypted, PRETTY_PRINT_XML, false);
//        return result;
//    }
//
//    public Node decryptFields(Node node)  {
//        LOG.trace("Started ClaimXml.decryptFields");
//        try {
//            if(node == null) {
//                LOG.info("node is null");
//                return null;
//            }
//
//            String nodeName = node.getNodeName();
//            int nodeType = node.getNodeType();
//            LOG.info("Node name = {}, type = {}", nodeName, nodeType);
//
//            // check for text nodes
//            if(nodeType == Node.TEXT_NODE || nodeType == Node.CDATA_SECTION_NODE) {
//                String text = node.getNodeValue();
//                if(isEncryptedData(text)) {
//                    try {
//                        String decryptedtText = decryptText(text);
//                        node.setNodeValue(decryptedtText);
//                        // lets try and print out the xpath
//                        LOG.info("Node({}) is encrypted", getNodePathname(node));
//                        System.err.println(getNodePathname(node));
//                    } catch(NotEncryptedException e) {
//                        // do nothing
//                    }
//                }
//            }
//
//            // check for children
//            NodeList children = node.getChildNodes();
//            for(int index = 0; index < children.getLength(); index++) {
//                Node child = children.item(index);
//                decryptFields(child);
//            }
//
//            return node;
//        } finally {
//            LOG.trace("Ending ClaimXml.decryptFields");
//        }
//    }
//
//    private String getNodePathname(Node node) {
//        if(node == null) {
//            return "";
//        }
//
//        String path = getNodePathname(node.getParentNode()) + "/" + node.getNodeName();
//        return path;
//    }
//
//    private String decryptText(String text) {
//        try {
//            EncryptorAES encryptor = new EncryptorAES();
//            String decrypted = encryptor.decrypt(DatatypeConverter.parseBase64Binary(text));
//            return decrypted;
//        } catch(NotEncryptedException e) {
//            LOG.warn("Text: {} is not encrypted: {}", text, e.getMessage());
//            throw e;
//        } catch(DwpRuntimeException e) {
//            LOG.error("Unable to decrypt text " + text, e);
//            throw e;
//        }
//    }
//
//
//    /**
//     * Text must have a length multiples of the block size (i.e. 64)
//     * and have no spaces (its possible it can have spaces randomly)
//     * @return
//     */
//    private boolean isEncryptedData(String text) {
//        if(text == null) {
//            return false;
//        }
//
//        if(text.length() % BLOCK_SIZE != 0) {
//            return false;
//        }
//
//        for(int index = 0; index < text.length(); index++) {
//            if(Character.isWhitespace(text.charAt(index))) {
//                return false;
//            }
//        }
//
//        return  true;
//    }
//
//    public String loadXml(String filename) throws IOException {
//        if(filename == null) {
//            return null;
//        }
//
//        File file = new File(filename);
//        String xml = FileUtils.readFileToString(file, UTF_8);   // UTF-8 is guarenteed
//        return xml;
//    }
//
//    public void saveXml(String xml, String filename) throws IOException {
//        Parameters.validateMandatoryArgs(new Object[]{xml, filename}, new String[]{"xml", "filename"});
//        File file = new File(filename);
//        FileUtils.write(file, xml, UTF_8);
//    }
//
//    public static void main(String[] args) throws IOException, InstantiationException, NoSuchAlgorithmException {
//        String filename = "/Users/drh/development/Tools/data/claim.16010000027.v3_4.xml";
//        ClaimXmlUtil claimXml = new ClaimXmlUtil(filename);
//
//        String decrypted = claimXml.decryptFields(claimXml.getXml());
////        System.out.println("text      = " + text);
//        System.out.println("decrypted = " + decrypted);
//
//    }
}
