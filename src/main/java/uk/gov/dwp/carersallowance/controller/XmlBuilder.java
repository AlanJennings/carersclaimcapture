package uk.gov.dwp.carersallowance.controller;

import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.utils.xml.XPathMapping;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
import uk.gov.dwp.carersallowance.utils.xml.XmlPrettyPrinter;

public class XmlBuilder {
    private static final String PATH_SEPARATOR = "/";
    private Document document;

    public XmlBuilder(String rootNodeName, Map<String, String> namespaces, Map<String, Object> values, XPathMappingList valueMappings) throws ParserConfigurationException {
        Parameters.validateMandatoryArgs(new Object[]{rootNodeName}, new String[]{"rootNodeName"});

        document = createDocument(rootNodeName, namespaces);
        add(values, valueMappings);
    }

    private Document createDocument(String rootNodeName, Map<String, String> namespaces) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootNode = doc.createElement(rootNodeName);
        doc.appendChild(rootNode);

        if(namespaces != null) {
            for(Map.Entry<String, String> namespace : namespaces.entrySet()) {
                Attr attr = doc.createAttribute(namespace.getKey());
                attr.setValue(namespace.getValue());
                rootNode.setAttributeNode(attr);
            }
        }

        return doc;
    }

    private void add(Map<String, Object> values, XPathMappingList valueMappings) {
        if(values == null || valueMappings == null) {
            return;
        }

        List<XPathMapping> list = valueMappings.getList();
        for(XPathMapping mapping : list) {
            String valueKey = mapping.getValue();
            Object value = values.get(valueKey);
            String xpath = mapping.getXpath();
            if(StringUtils.isNotBlank(xpath)) {
                add(value, mapping.getXpath(), true);
            }
        }
    }

    /**
     * Add a single node value to the existing document merging in the XPath to the existing structure
     * @param value e.g. DLA
     * @param Xpath e.g. DWPBody/DWPCATransaction/DWPCAClaim/QualifyingBenefit/Answer
     * @return the newly created node
     */
    private Node add(Object value, String xPath, boolean createEmptyNodes) {
        if(xPath == null || value == null || (createEmptyNodes == false && isValueEmpty(value))) {
            return null;
        }

        String[] pathElements = xPath.split(PATH_SEPARATOR);
        Node current = document;
        for(int index = 0; index < pathElements.length; index++) {
            String element = pathElements[index];
            Node childNode = getNamedNode(current, element, true);
            if(childNode == null) {
                throw new IllegalStateException("Unable to create node(" + element + ") at: " + subpath(xPath, 0, index));
            }
            current = childNode;
        }

        // by this stage we should have the node we require.
        if(isValueEmpty(value) == false) {
            String text = String.valueOf(value);
            Node textNode = document.createTextNode(text);
            current.appendChild(textNode);
        }

        return current;
    }

    private String subpath(String path, int start, int end) {
        if(path == null) {
            return null;
        }

        int startOffset = 0;
        int endOffset = -1;
        int searchStartIndex = 0;

        for(int index = 0; index < end; index++) {
            searchStartIndex = path.indexOf(PATH_SEPARATOR, searchStartIndex);
            if(searchStartIndex < 0) {
                throw new IndexOutOfBoundsException("index = " + index);
            } else {
                if((index + 1) == start) {
                    startOffset = searchStartIndex;
                }
                if((index + 1) == end) {
                    endOffset = searchStartIndex;
                    break;
                }
            }
        }

        String substring = path.substring(startOffset, endOffset);
        return substring;
    }

    private Node getNamedNode(Node node, String childName, boolean create) {
        if(node == null || childName == null) {
            return null;
        }

        NodeList children = node.getChildNodes();
        for(int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            String name = child.getNodeName();
            if(childName.equals(name)) {
                return child;
            }
        }

        if(create) {
            // we can use node.getOwnerDocument also
            Element childNode = document.createElement(childName);
            node.appendChild(childNode);
            return childNode;
        }

        return null;
    }

    private boolean isValueEmpty(Object value) {
        if(value == null) {
            return true;
        }
        if(value instanceof String) {
            String string = (String)value;
            if(string.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    public String render() throws InstantiationException {
        return render(true, false);
    }

    public String render(boolean includeXmlDeclaration, boolean prettyPrint) throws InstantiationException {
        String xml = XmlPrettyPrinter.xmlToString(document, prettyPrint, includeXmlDeclaration);
        return xml;
    }
}