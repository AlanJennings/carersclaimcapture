package uk.gov.dwp.carersallowance.utils.xml;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class BuildXpaths {
    private static final Logger LOG = LoggerFactory.getLogger(BuildXpaths.class);

    private List<XPathMapping> items;

    public BuildXpaths(Document document) {
        items = parse(document);
    }

    private List<XPathMapping> parse(Document document) {
        Parameters.validateMandatoryArgs(document, "document");
        List<XPathMapping> list = new ArrayList<>();

        parse(list, "", document);
        return list;
    }

    private void parse(List<XPathMapping> list, String parentPath, Node parent) {
        LOG.trace("Started BuildXpaths.parse(List<Item> list, String parentPath, Node parent)");
        LOG.debug("parentPath = {}", parentPath);
        if (parent == null) {
            return;
        }

        LOG.debug("getting children");
        NodeList children = parent.getChildNodes();
        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            String nodeName = child.getNodeName();
            String xpath = parentPath + nodeName;
            short nodeType = child.getNodeType();
            LOG.debug("nodeType = {}, nodeName = {}", nodeType, nodeName);

            // we can't use instanceof as everything is a Node
            if (nodeType == Node.ELEMENT_NODE) {
                String value = getNodeValue(child);
                LOG.debug("value = {}", value);
                if (isFormVariable(value)) {
                    LOG.debug("found form variable");
                    String data = extractFormVariable(value);
                    if (data.endsWith(".label") == false) {
                        XPathMapping item = new XPathMapping(data, xpath, null);
                        list.add(item);
                    }
                }

                String childPath = xpath + "/";
                parse(list, childPath, child);
            } else {
                LOG.debug("Unexpected Node type: " + child.getNodeType() + " at " + parentPath);
            }
        }
    }

    private boolean isFormVariable(String value) {
        if (value == null || value.equals("") || value.charAt(0) != '{' || value.charAt(value.length() - 1) != '}') {
            return false;
        }

        return true;
    }

    private String extractFormVariable(String value) {
        Parameters.validateMandatoryArgs(value,  "value");
        return value.substring(1, value.length() - 1);
    }

    /**
     * Return the trimmed aggregate of all the immediate text nodes
     * @param node
     * @return
     */
    private String getNodeValue(Node node) {
        if (node == null) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        NodeList children = node.getChildNodes();
        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            if (child instanceof Text) {
                Text textNode = (Text)child;
                buffer.append(textNode.getData());
            }
        }

        return buffer.toString().trim();
    }

    public List<XPathMapping> getItems() {
        return items;
    }

    public static void main(String[] args) throws IOException, InstantiationException {
        LOG.debug("Started ...");

        String directory = "/Users/drh/development-java/CarersClaimCapture/CarersClaimCapture/src/main/resources";
        String filename = "claim.template.xml";

        File dir = new File(directory);
        File file = new File(dir, filename);

        String xml = FileUtils.readFileToString(file, Charset.defaultCharset());
        Document document = (Document)XmlPrettyPrinter.stringToNode(xml);

        BuildXpaths buildXpaths = new BuildXpaths(document);
        List<XPathMapping> items = buildXpaths.getItems();

        LOG.debug("{}", items);

        for (XPathMapping item: items) {
            LOG.debug(item.getValue() + "\t = " + item.getXpath());
        }
        LOG.debug("Complete");
    }
}
