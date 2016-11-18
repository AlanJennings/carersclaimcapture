package uk.gov.dwp.carersallowance.utils.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class XmlXpathRenderer {
    private static final String PATH_SEPERATOR = "/";

    private String              rootName;
    private Map<String, String> namespaces;

    public XmlXpathRenderer(String rootName, Map<String, String> namespaces) {
        Parameters.validateMandatoryArgs(rootName, "rootName");

        this.rootName = rootName;
        this.namespaces = namespaces;
    }

    public Document render(List<XPathMapping> items) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // add root node
        add(document, document, rootName, namespaces);

        // and any items
        if(items != null) {
            for(XPathMapping item: items) {
                add(document, item);
            }
        }

        return document;
    }

    public Node add(Document document, XPathMapping item) {
        Parameters.validateMandatoryArgs(document, "document");
        if(item == null) {
            return null;
        }

        String data = item.getValue();
        String xpath = item.getXpath();
        Node node = getNode(document, xpath, true);

        Node textNode = document.createTextNode(data);  // TODO might be better to use CDATASection
        document.appendChild(textNode);

        return node;
    }

    /**
     * Walk the existing document, making sure that the full path exists, create new nodes as required
     * e.g. DWPBody/DWPCATransaction/DWPCAClaim/DateOfClaim/QuestionLabel
     *
     * @param document
     * @param xpath
     * @return
     */
    public Node getNode(Document document, String xpath, boolean create) {
        if(xpath == null) {
            return null;
        }

        String[] xpathElements = xpath.split(PATH_SEPERATOR);
        return getNode(document, xpathElements, create);
    }

    public Node getNode(Document document, String[] xpathElements, boolean create) {
        // get parent node
        Node parent;
        if(xpathElements.length == 1) {
            parent = getRootNode(document, xpathElements[0], create);
        } else {
            String[] xpathParentElements = Arrays.copyOf(xpathElements, xpathElements.length - 1);
            parent = getNode(document, xpathParentElements, create);
        }

        // check if we have an existing matching child for the current last element
        String nodeName = xpathElements[xpathElements.length - 1];
        NodeList children = parent.getChildNodes();
        for(int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            String childNodeName = child.getNodeName();
            if(nodeName.equalsIgnoreCase(childNodeName)) {
                return child;
            }
        }

        // if not, create one
        if(create) {
            return add(document, parent, nodeName, null);
        }

        // if we are not creating new nodes, throw an exception
        String xpath = String.join(PATH_SEPERATOR, xpathElements);
        throw new CreateDocumentException("Node: " + xpath + " does not exist");
    }

    private Node getRootNode(Document document, String xpath, boolean create) {
        // we are at the root of the document, therefore the document should have a child node of xpath
        // there can only be a single rootNode, so if it doesn't match we cannot create a new one
        Node child = document.getFirstChild();
        if(child == null) {
            if(create) {
                return add(document, document, xpath, null);
            }
            throw new CreateDocumentException("Not root node: " + xpath);

        } else {
            String childName = child.getNodeName();
            if(xpath.equalsIgnoreCase(childName)) {
                return child;
            }
            throw new CreateDocumentException("existing root node: " + childName + ", does not match required root: " + xpath);
        }
    }

    public static Element add(Document document, Node node, String nodeName, Map<String, String> attributes) {
        Element newNode = document.createElement( nodeName);
        document.appendChild(newNode);
        if(attributes != null) {
            // sort, so we get a consistent order
            List<String> attrNames = new ArrayList<String>(attributes.keySet());
            Collections.sort(attrNames);
            for(String attrName: attrNames) {
                String attrValue = attributes.get(attrName);
                newNode.setAttribute(attrName, attrValue);
            }
        }

        return newNode;
    }

    public static class CreateDocumentException extends RuntimeException {
        private static final long serialVersionUID = 3818851091152770368L;

        public CreateDocumentException() {
            super();
        }

        public CreateDocumentException(String message) {
            super(message);
        }

        public CreateDocumentException(String message, Throwable cause) {
            super(message, cause);
        }

        public CreateDocumentException(Throwable cause) {
            super(cause);
        }
    }

    public static void main(String[] args) {
        String[] data = {
                ""
        };
    }
}
