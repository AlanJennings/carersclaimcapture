package uk.gov.dwp.carersallowance.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.gov.dwp.carersallowance.session.IllegalFieldValueException;
import uk.gov.dwp.carersallowance.utils.C3Constants;
import uk.gov.dwp.carersallowance.utils.LoadFile;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.utils.xml.XPathMapping;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
import uk.gov.dwp.carersallowance.utils.xml.XmlPrettyPrinter;

public class XmlBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(XmlBuilder.class);

    private static final String XML_MAPPING__CLAIM = "xml.mapping.claim";
    private static final String PATH_SEPARATOR = "/";

    private Document document;
    private Map<String, XPathMappingList> valueMappings;
    private final MessageSource messageSource;
    private final ServerSideResolveArgs serverSideResolveArgs;

    public XmlBuilder(final String rootNodeName, final Map<String, Object> values, final MessageSource messageSource, final ServerSideResolveArgs serverSideResolveArgs) throws ParserConfigurationException, IOException, XPathMappingList.MappingException {
        this.messageSource = messageSource;
        this.serverSideResolveArgs = serverSideResolveArgs;
        Parameters.validateMandatoryArgs(new Object[]{rootNodeName}, new String[]{"rootNodeName"});
        Map<String, String> namespaces = getNamespaces();
        document = createDocument(rootNodeName, namespaces);
        this.valueMappings = loadXPathMappings();

        addAssistedDecisionToClaim(values);
        addNodes(values, null, document);
    }

    private Map<String, XPathMappingList> loadXPathMappings() throws IOException, XPathMappingList.MappingException {
        Map<String, XPathMappingList> mappings = new HashMap<>();
        URL claimTemplateUrl = this.getClass().getClassLoader().getResource(XML_MAPPING__CLAIM);
        List<String> xmlMappings = LoadFile.readLines(claimTemplateUrl);
        XPathMappingList valueMappings = new XPathMappingList();
        valueMappings.add(xmlMappings);
        mappings.put(null, valueMappings);
        return mappings;
    }


    private Map<String, String> getNamespaces() {
        Map<String, String> namespaces = new HashMap<>();
        namespaces.put("xmlns", "http://www.govtalk.gov.uk/dwp/carers-allowance");
        namespaces.put("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
        namespaces.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaces.put("xsi:schemaLocation", "http://www.govtalk.gov.uk/dwp/carers-allowance file:/future/schema/ca/CarersAllowance_Schema.xsd");
        return namespaces;
    }

    private Document createDocument(String rootNodeName, Map<String, String> namespaces) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootNode = doc.createElement(rootNodeName);
        doc.appendChild(rootNode);

        if (namespaces != null) {
            for (Map.Entry<String, String> namespace : namespaces.entrySet()) {
                Attr attr = doc.createAttribute(namespace.getKey());
                attr.setValue(namespace.getValue());
                rootNode.setAttributeNode(attr);
            }
        }

        return doc;
    }

    /**
     * Render the contents of "values" as an XML document by iterating over XPathMappingList; which maps a named
     * value (in values) to a Node at a specific XPath location, populating the nodes with the corresponding values
     *
     * @param values
     * @param localRootNode XPath calculations use this as the root location, usually document, but can be different for FieldCollections
     */
    private void addNodes(Map<String, Object> values, String mappingName, Node localRootNode) {
        if (values == null) {
            return;
        }

        XPathMappingList mappingList = valueMappings.get(mappingName);
        if (mappingList == null) {
            throw new IllegalArgumentException("Unknown mapping: " + mappingName);
        }

        List<XPathMapping> list = mappingList.getList();

        // Store all string values by xpath to allow easy lookup when checking if questions need to be added to xml
        Map<String, String> valuesByValueKey = new HashMap<>();
        for (XPathMapping mapping : list) {
            String valueKey = mapping.getValue();
            Object value = getClaimValue(valueKey, values);
            LOG.debug("Build map ... checking valueKey:{} for xpath:{}->{}", valueKey, mapping.getXpath(), value);
            if (StringUtils.isNotBlank(mapping.getXpath()) && isValueEmpty(value) == false && value instanceof String) {
                LOG.debug("Build map ... ADDING valueKey:{}->{}", valueKey, value);
                valuesByValueKey.put(valueKey, (String) value);
            }
        }

        for (XPathMapping mapping : list) {
            String valueKey = mapping.getValue();
            Object value = getClaimValue(valueKey, values);
            String xpath = mapping.getXpath();
            String processingInstruction = mapping.getProcessingInstruction();
            LOG.debug("Checking xpath:{}", xpath);
            if (StringUtils.isNotBlank(xpath) && isValueEmpty(value) == false) {
                if (value instanceof String) {
                    // we may have add attribute without = which is a filter instruction i.e. DWPBody/DWPCATransaction/[@id]
                    if (processingInstruction != null && processingInstruction.length() > 0 && !processingInstruction.contains("=")) {
                        addAttr(xpath, processingInstruction, (String) value, localRootNode);
                    } else {
                        addNode(xpath, (String) value, true, localRootNode, processingInstruction);   // create leaf node
                    }
                } else if (value instanceof List) {
                    // field collection, we can't reliably assert the parameterized types, so will go with <?>
                    List<Map<String, Object>> fieldCollectionList = castFieldCollectionList(value);
                    add(fieldCollectionList, document);
                } else {
                    throw new IllegalFieldValueException("Unsupported value class: " + value.getClass().getName(), (String) null, (String[]) null);
                }
            } else if (valueKey != null && (valueKey.endsWith(".label") || valueKey.endsWith(".text"))) {
                LOG.debug("Checking QuestionLabel:{}", valueKey);
                String question = getQuestion(valueKey, values);
                String relatedAnswerKey = valueKey.replace(".label", "").replace(".text", "");
                // If we have a corresponding Answer and value set for this QuestionLabel then we add to xml
                if (valuesByValueKey.containsKey(relatedAnswerKey)) {
                    LOG.debug("Adding QuestionLabel:{}", xpath);
                    addNode(xpath, question, true, localRootNode, null);
                }
                // Address carerAddress.label has carerAddressLineOne, carerAddressLineTwo
                else if (valuesByValueKey.containsKey(relatedAnswerKey + "LineOne")){
                    LOG.debug("Adding Address QuestionLabel:{}", xpath);
                    addNode(xpath, question, true, localRootNode, null);
                }
            }
        }
    }

    private Object getClaimValue(String key, Map<String, Object> values) {
        Object value = values.get(key);
        if (value != null) {
            LOG.debug("GOT REGULAR KEY!!");
            return value;
        } else if (key != null && key.contains(".attribute")) {
            return values.get(key.replace(".attribute", ""));
        } else if (values.containsKey(key + "_day") && values.containsKey(key + "_month") && values.containsKey(key + "_year")) {
            LOG.debug("GOT DATE KEYS!!");
            String dateDay = (String) values.get(key + "_day");
            String dateMonth = (String) values.get(key + "_month");
            String dateYear = (String) values.get(key + "_year");
            String date = dateDay + "-" + dateMonth + "-" + dateYear;
            LOG.debug("RETURNING DATE:{}", date);
            return date;
        } else {
            return null;
        }
    }

    private String getQuestion(String questionKey, final Map<String, Object> values) {
        // TODO throw exception and stop processing if gap in messages. But too many Income / Breaks gaps as of 10/01/2016
        String questionMessage;
        try {
            Object[] parameters = getParameters(questionKey, values);
            questionMessage = messageSource.getMessage(questionKey, parameters, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            LOG.error("NoSuchMessageException thrown looking for message for key:" + questionKey);
            questionMessage = "ERROR " + questionKey + " - message not found";
        } catch (Exception e) {
            LOG.error("Exception thrown looking for message for key:" + questionKey);
            questionMessage = "ERROR " + questionKey + " - exception";
        }
        return (questionMessage);
    }

    private Object[] getParameters(final String questionKey, final Map<String, Object> values) {
        try {
            final String expression = messageSource.getMessage(questionKey + ".args", null, null, Locale.getDefault());
            List<Object> expressions = serverSideResolveArgs.evaluateExpressions(expression, values);
            return (expression == null) ? null : expressions.toArray();
        } catch (NoSuchMessageException e) {
        } catch (Exception e) {
            LOG.error("Exception thrown looking for message for key:" + questionKey);
        }
        return null;
    }

    /**
     * Adds an attribute to an existing node.
     */
    private void addAttr(String xPath, String name, String value, Node localRootNode) {
        Element node = (Element) getNamedNode(xPath, null, false, localRootNode);
        node.setAttribute(name.replace("@", ""), value);
    }


    /**
     * Add a single node value to the existing document merging in the XPath to the existing structure
     *
     * @param value e.g. DLA
     * @return the newly created node
     */
    private Node addNode(String xPath, String value, boolean createEmptyNodes, Node localRootNode, String processingInstruction) {
        if (xPath == null || (createEmptyNodes == false && isValueEmpty(value))) {
            return null;
        }

        Node node;
        if (processingInstruction != null) {
            Map<String, String> attributes = new HashMap<>();
            attributes.put(StringUtils.substringBefore(processingInstruction, "=").replace("@", ""), StringUtils.substringAfter(processingInstruction, "="));
            node = getNamedNode(xPath, attributes, true, localRootNode);
        } else {
            node = getNamedNode(xPath, null, false, localRootNode);
        }
        if (isValueEmpty(value) == false) {
            Node textNode = document.createTextNode(value.replace(C3Constants.YES, "Yes").replace(C3Constants.NO, "No"));
            node.appendChild(textNode);
        }

        return node;
    }

    private void add(List<Map<String, Object>> fieldCollectionList, Document document) {
        // create enclosing node, one per list item using order attribute, then create the inner values
        if (fieldCollectionList == null) {
            return;
        }

        for (int index = 0; index < fieldCollectionList.size(); index++) {
            Map<String, Object> fieldCollection = fieldCollectionList.get(index);
            addNodes(fieldCollection, null, document);
        }
    }

    private List<Map<String, Object>> castFieldCollectionList(Object untypedfieldCollection) {
        if ((untypedfieldCollection instanceof List) == false) {
            throw new IllegalArgumentException("field collection list is not a 'List'");
        }
        List<?> list = (List<?>) untypedfieldCollection;
        for (Object item : list) {
            if (item != null && (item instanceof Map<?, ?>) == false) {
                throw new IllegalArgumentException("item in the field collection list is not a 'Map'");
            }
            Map<?, ?> map = (Map<?, ?>) item;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey() != null && (entry.getKey() instanceof String) == false) {
                    throw new IllegalArgumentException("key in map instance in field collection is not a String");
                }
                if (entry.getValue() != null && (entry.getValue() instanceof String) == false) {
                    throw new IllegalArgumentException("value in map instance in field collection is not a String");
                }
            }
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> result = (List<Map<String, Object>>) list;

        return result;
    }

    private String subpath(String path, int start, int end) {
        if (path == null) {
            return null;
        }

        int startOffset = 0;
        int endOffset = -1;
        int searchStartIndex = 0;

        for (int index = 0; index < end; index++) {
            searchStartIndex = path.indexOf(PATH_SEPARATOR, searchStartIndex);
            if (searchStartIndex < 0) {
                throw new IndexOutOfBoundsException("index = " + index);
            } else {
                if ((index + 1) == start) {
                    startOffset = searchStartIndex;
                }
                if ((index + 1) == end) {
                    endOffset = searchStartIndex;
                    break;
                }
            }
        }

        String substring = path.substring(startOffset, endOffset);
        return substring;
    }

    /**
     * Return a pre-existing child to this specific node that matches the childName and attributes, or if
     * it does not exist: create a new child node if create = true, otherwise return null;
     *
     * @param localRootNode the rootNode used for xPath calculations
     * @return
     */
    private Node getNamedNode(String xPath, Map<String, String> attributes, boolean attrExactMatch, Node localRootNode) {
        //remove Line from path
        String[] pathElements = xPath.split(PATH_SEPARATOR);
        Node current = localRootNode;
        for (int index = 0; index < pathElements.length; index++) {
            String element = pathElements[index];
            Node childNode;
            if (attributes != null && index == pathElements.length -1) {
                childNode = getNamedNode(current, element, true, attributes, attrExactMatch);
            } else {
                childNode = getNamedNode(current, element, true, null, false);
            }
            if (childNode == null) {
                throw new IllegalStateException("Unable to create node(" + element + ") at: " + subpath(xPath, 0, index));
            }
            current = childNode;
        }

        return current;
    }

    /**
     * Return a pre-existing child to this specific node that matches the childName and attributes, or if
     * it does not exist: create a new child node if create = true, otherwise return null;
     *
     * @return
     */
    private Node getNamedNode(Node node, String childName, boolean create, Map<String, String> attributes, boolean attrExactMatch) {
        if (node == null || childName == null) {
            return null;
        }

        Boolean hasChild = Boolean.FALSE;
        Boolean attributesMatch = Boolean.TRUE;
        Node child = null;
        NodeList children = node.getChildNodes();
        for (int index = 0; index < children.getLength(); index++) {
            attributesMatch = Boolean.TRUE;
            child = children.item(index);
            hasChild = Boolean.FALSE;
            String name = child.getNodeName();
            if (childName.equals(name)) {
                if (attributes != null || attrExactMatch) {
                    Map<String, String> childAttrMap = attrsToMap(child.getAttributes());
                    attributesMatch = attrsMatch(childAttrMap, attributes, attrExactMatch);
                    if (!attributesMatch) {
                        break;
                    }
                }
                hasChild = Boolean.TRUE;
                break;
            }
        }

        if ((create && !hasChild) || (create && !attributesMatch)) {
            // we can use node.getOwnerDocument also
            Element childNode = document.createElement(childName);
            node.appendChild(childNode);
            if (attributes != null) {
                for (Map.Entry<String, String> attribute : attributes.entrySet()) {
                    //attributes not in schema
                    //childNode.setAttribute(attribute.getKey(), attribute.getValue());
                }
            }
            return childNode;
        }

        return hasChild ? child : null;
    }

    private boolean attrsMatch(Map<String, String> childAttrs, Map<String, String> attributes, boolean attrExactMatch) {
        if (attrExactMatch) {
            return childAttrs.equals(attributes);
        }

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ((childAttrs.containsKey(key) == false)
                    || (Objects.equals(childAttrs.get(key), value) == false)) {
                return false;
            }
        }
        return true;

    }

    private Map<String, String> addToAttrMap(Map<String, String> map, String name, String value) {
        Parameters.validateMandatoryArgs(name, "name");
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(name, value);
        return map;
    }

    private Map<String, String> attrsToMap(NamedNodeMap rawAttrMap) {
        if (rawAttrMap == null) {
            return null;
        }

        Map<String, String> map = new HashMap<>();
        for (int index = 0; index < rawAttrMap.getLength(); index++) {
            Node attr = rawAttrMap.item(index);
            String name = attr.getNodeName();
            String value = attr.getNodeValue();
            map.put(name, value);
        }

        return map;
    }

    private boolean isValueEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            String string = (String) value;
            if (string.trim().equals("")) {
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

    private void addAssistedDecisionToClaim(Map<String, Object> values) {
        AssistedDecision assistedDecision = new AssistedDecision(65, values);
        values.put("assistedDecisionReason", assistedDecision.getReason());
        values.put("assistedDecisionDecision", assistedDecision.getDecision());
    }

    public String getNodeValue(String nodepath) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String nodevalue = null;
        try {
            nodevalue = xpath.compile(nodepath).evaluate(document);
        } catch (XPathException e) {
            LOG.error("Exception compiling xpath:{}", e.toString(), e);
        }
        return nodevalue;
    }

    public Document getDocument() {
        return document;
    }
}