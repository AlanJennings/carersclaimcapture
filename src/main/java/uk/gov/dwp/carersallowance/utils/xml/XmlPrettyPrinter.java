package uk.gov.dwp.carersallowance.utils.xml;

import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSParserFilter;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.traversal.NodeFilter;

import uk.gov.dwp.carersallowance.utils.Parameters;

/**
 * Format XML.
 * String XML is converted to a DOM first and then back to a String, so moderately expensive.
 * There is probably an assumption that you would not routinely reformat XML (typically machine readable XML has no formatting)
 *
 * @author David Hutchinson (drh@elegantsoftwaresolutions.co.uk)
 */
public final class XmlPrettyPrinter {
    private XmlPrettyPrinter() {}

    public static String xmlToString(Node xml, boolean includeXmlDeclaration) throws InstantiationException { return xmlToString(xml, false, includeXmlDeclaration); }

    public static String xmlToString(Node xml) throws InstantiationException     { return xmlToString(xml, false, true); }
    public static String prettyPrintXml(Node xml) throws InstantiationException  { return xmlToString(xml, true, true); }

    public static String xmlToString(Node xml, boolean prettyPrint, boolean includeXmlDeclaration)
            throws InstantiationException {

        Parameters.validateMandatoryArgs(new Object[]{xml}, new String[]{"xml"});
        try {
            if (!prettyPrint) {
                xml.normalize();
            }

            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");
            LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.valueOf(prettyPrint));
            writer.getDomConfig().setParameter("xml-declaration", Boolean.valueOf(includeXmlDeclaration));

            LSOutput output = impl.createLSOutput();
            StringWriter stringWriter = new StringWriter();
            output.setCharacterStream(stringWriter);
            writer.write(xml, output);
            String xmlStr = stringWriter.toString();

            return xmlStr;
        } catch (ClassCastException e) {
            InstantiationException exception = new InstantiationException();
            exception.initCause(e);
            throw exception;
        } catch (ClassNotFoundException e) {
            InstantiationException exception = new InstantiationException();
            exception.initCause(e);
            throw exception;
        } catch (IllegalAccessException e) {
            InstantiationException exception = new InstantiationException();
            exception.initCause(e);
            throw exception;
        }
    }

    public static Node stringToNode(String string)
            throws InstantiationException {

        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");
            LSParser reader = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS , null);
            reader.getDomConfig().setParameter("namespaces", Boolean.valueOf(false));
            reader.getDomConfig().setParameter("element-content-whitespace", Boolean.valueOf(false));   // this only works if there is a DTD
            reader.setFilter(new StripWhitespaceFilter());  // we always strip whitespace, even when pretty-printing

            LSInput input = impl.createLSInput();
            input.setStringData(string);

            Document doc = reader.parse(input);
            return doc;
        } catch (ClassCastException e) {
            InstantiationException exception = new InstantiationException();
            exception.initCause(e);
            throw exception;
        } catch (ClassNotFoundException e) {
            InstantiationException exception = new InstantiationException();
            exception.initCause(e);
            throw exception;
        } catch (IllegalAccessException e) {
            InstantiationException exception = new InstantiationException();
            exception.initCause(e);
            throw exception;
        }
    }

    public static String formatXml(String input, boolean prettyPrint, boolean includeXmlDeclaration)
            throws InstantiationException {

        Node node = stringToNode(input);
        String xml = xmlToString(node, prettyPrint, includeXmlDeclaration);
        return xml;
    }

    private static class StripWhitespaceFilter implements LSParserFilter {

        @Override
        public short startElement(Element elementArg) {
            return FILTER_ACCEPT;
        }

        @Override
        public short acceptNode(Node nodeArg) {
            if (nodeArg == null) {
                return FILTER_REJECT;
            }

            if (nodeArg instanceof Text) {
                Text textNode = (Text)nodeArg;
                String wholeText = textNode.getWholeText();
                // Bizarrely textNode.isElementContentWhitespace() does not always return true when the text is all whitespace!!
                if (StringUtils.isWhitespace(wholeText)) {
                    return FILTER_REJECT;
                }
            }
            return FILTER_ACCEPT;
        }

        @Override
        public int getWhatToShow() {
            // const unsigned long       SHOW_ALL                       = 0xFFFFFFFF;
            // const unsigned long       SHOW_ELEMENT                   = 0x00000001;
            // const unsigned long       SHOW_ATTRIBUTE                 = 0x00000002;
            // const unsigned long       SHOW_TEXT                      = 0x00000004;
            // const unsigned long       SHOW_CDATA_SECTION             = 0x00000008;
            // const unsigned long       SHOW_ENTITY_REFERENCE          = 0x00000010;
            // const unsigned long       SHOW_ENTITY                    = 0x00000020;
            // const unsigned long       SHOW_PROCESSING_INSTRUCTION    = 0x00000040;
            // const unsigned long       SHOW_COMMENT                   = 0x00000080;
            // const unsigned long       SHOW_DOCUMENT                  = 0x00000100;
            // const unsigned long       SHOW_DOCUMENT_TYPE             = 0x00000200;
            // const unsigned long       SHOW_DOCUMENT_FRAGMENT         = 0x00000400;
            // const unsigned long       SHOW_NOTATION                  = 0x00000800;

            return NodeFilter.SHOW_TEXT | NodeFilter.SHOW_CDATA_SECTION;
        }

    }

//    public static void main(String[] args) {
//        LOG.debug("Started");
//
//        String filename = "/Users/drh/development-java/CarersClaimCapture/CarersClaimCapture/resources/data/claim.16010000027.v3_4.xml";
//
////        for(int index = 0; index < args.length; index++) {
////            String filename = args[index];
//            try {
//                String rawXml = FileUtils.readFileToString(new File(filename), Charset.defaultCharset());
//                String prettyXml = formatXml(rawXml, true, false);
//                String flatXml = formatXml(rawXml, false, false);
//                String flattenedXml = formatXml(prettyXml, false, false);
//
//                LOG.debug("   RAW XML");
//                LOG.debug("=============");
//                LOG.debug(rawXml);
//                LOG.debug();
//
//                LOG.debug("  PRETTY XML");
//                LOG.debug("=============");
//                LOG.debug(prettyXml);
//                LOG.debug();
//
//                LOG.debug("  FLAT XML");
//                LOG.debug("=============");
//                LOG.debug(flatXml);
//                LOG.debug();
//
//                LOG.debug("  FLATENED XML");
//                LOG.debug("================");
//                LOG.debug(flattenedXml);
//                LOG.debug();
//
//            } catch(IOException e) {
//                System.err.println("Problems with file: " + filename);
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                System.err.println("Problems with file: " + filename);
//                e.printStackTrace();
//            }
////        }
//            LOG.debug("Complete");
//    }
}