package uk.gov.dwp.carersallowance.xml;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import uk.gov.dwp.carersallowance.utils.C3Constants;
import uk.gov.dwp.carersallowance.utils.LoadFile;
import uk.gov.dwp.carersallowance.utils.xml.XPathMapping;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
import uk.gov.dwp.carersallowance.utils.xml.XmlPrettyPrinter;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by peterwhitehead on 10/02/2017.
 */
public class AbstractXMLReader {
    private static final Logger LOG = LoggerFactory.getLogger(XmlClaimReader.class);


    private static final Set<String> ACTIVE_ATTRS = new HashSet<>(Arrays.asList(new String[]{"type", "order"}));
    private static final String PATH_SEPARATOR = "/";

    protected Set<String> IGNORE_MAPPING = new HashSet<>();
    protected Set<String> NOT_SUPPORTED = new HashSet<>();
    protected final List<CollectionDetails> collections = new ArrayList<>();

    private Map<String, Object> values;
    private List<String> errors;
    private boolean sessionVariablesOnly;
    protected String mappingFileName;
    private Document document;
    private Map<String, XPathMappingList> valueMappings;

    public AbstractXMLReader(final String xmlFile, final Boolean sessionVariablesOnly, final URL mappingFile) throws Exception {
        String xml = IOUtils.toString(XmlClaimReader.class.getClassLoader().getResourceAsStream(xmlFile), Charset.defaultCharset());
        createSessionValues(createMappings(mappingFile), sessionVariablesOnly, xml);
    }

    private XPathMappingList createMappings(final URL mappingFile) throws Exception {
        List<String> xmlMappings = LoadFile.readLines(mappingFile);
        XPathMappingList xPathMappingList = new XPathMappingList();
        xPathMappingList.add(xmlMappings);
        this.mappingFileName = StringUtils.substringAfterLast(mappingFile.getFile(), "/");
        return xPathMappingList;
    }

    protected void createAdditionalMappings(final String fileName, final String mappingName) {
        try {
            final URL xmlMappingFile = XmlClaimReader.class.getClassLoader().getResource(fileName);
            if (xmlMappingFile != null) {
                List<String> xmlMappings = LoadFile.readLines(xmlMappingFile);
                if (xmlMappings != null) {
                    XPathMappingList xPathMappingList = new XPathMappingList();
                    xPathMappingList.add(xmlMappings);
                    valueMappings.put(mappingName, xPathMappingList);
                }
            }
        } catch (Exception e) {
            LOG.error("Error loading file.", e);
        }
    }

    public AbstractXMLReader(final String xml, final XPathMappingList valueMapping, final Boolean sessionVariablesOnly) throws InstantiationException {
        createSessionValues(valueMapping, sessionVariablesOnly, xml);
    }

    private void createSessionValues(final XPathMappingList valueMapping, final Boolean sessionVariablesOnly, final String xml) throws InstantiationException {
        this.sessionVariablesOnly = sessionVariablesOnly;
        document = (Document) XmlPrettyPrinter.stringToNode(xml);
        valueMappings = new HashMap<>();
        errors = new ArrayList<>();
        values = new HashMap<>();
        createCollectionDetails();
        valueMappings.put("default", valueMapping);
        parseXml(values, document.getFirstChild(), null, ACTIVE_ATTRS);
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public List<String> getErrors() {
        return errors;
    }

    private void parseXml(final Map<String, Object> values, final Node node, final String parentPath, final Set<String> activeAttrsLowerCase) {
        if (node == null) {
            return;
        }

        if (NOT_SUPPORTED.contains(parentPath)) {
            return;
        }

        // found text node, create entry
        LOG.debug("parentPath = {}", parentPath);
        if (node instanceof CharacterData) {
            createMapping(values, node, parentPath);
        }

        // otherwise create current node name
        String nodePath = buildNodePath(node, parentPath, activeAttrsLowerCase);

        // iterate over node children
        NodeList children = node.getChildNodes();
        Map<String, Integer> siblingTotals = getSiblingCount(children);
        Map<String, Integer> currentSiblingCount = new HashMap<>();

        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            String childName = child.getNodeName();

            Integer childOrder;
            if (siblingTotals.get(childName) == null || siblingTotals.get(childName) == Integer.valueOf(1)) {
                // when there is just a single instance we don't bother with an order attribute,
                // which is most of the time
            } else {
                // we use order attribute, to maintain a one to one mapping (and maintain the order)
                childOrder = currentSiblingCount.get(childName);
                if (childOrder == null) {
                    childOrder = Integer.valueOf(1);
                } else {
                    childOrder = Integer.valueOf(childOrder.intValue() + 1);
                }
                currentSiblingCount.put(childName, childOrder);
            }
            parseXml(values, child, nodePath, activeAttrsLowerCase);
        }
    }

    private void createMapping(final Map<String, Object> values, final Node xml, final String parentPath) {
        String data = ((CharacterData) xml).getData();
        LOG.debug("Found Text: {}", data);

        XPathMapping mapping = findMapping(parentPath, "default");

        if (IGNORE_MAPPING.contains(parentPath)) {
            return; // do nothing
        } else if (mapping == null) {
            if (!createCollection(values, xml, parentPath, data)) {
                LOG.error("Unknown mapping for {}", parentPath);
                errors.add("Unknown mapping for " + parentPath);
                throw new IllegalStateException("Unknown mapping for " + parentPath);
            }
        } else {
            processMapping(mapping, values, data);
        }
    }

    protected void processMapping(final XPathMapping mapping, final Map<String, Object> values, final String data) {
        String key = mapping.getValue();
        String processingInstruction = mapping.getProcessingInstruction();
        if (sessionVariablesOnly && key != null && (key.startsWith("/") || key.contains("."))) {
            // do not store non-session variables
        } else if (processingInstruction != null && processingInstruction.equals("@type=\"date\"")) {
            LOG.info("XmlClaim adding date:" + key + "->" + data);
            createDateMapping(values, key, data);
        } else {
            LOG.info("XmlClaim adding string:" + key + "->" + data);
            // TODO Fixup case on yes / no to be consistent between claim and webapp
            values.put(key, data.replace("No", C3Constants.NO).replace("Yes", C3Constants.YES));
        }
    }

    protected XPathMapping findMapping(final String path, final String mappingName) {
        final String newPath = StringUtils.substringBefore(path, "[");
        final XPathMappingList xPathMappingList = valueMappings.get(mappingName);
        if (xPathMappingList != null) {
            for (XPathMapping m : xPathMappingList.getList()) {
                if (m != null && m.getXpath() != null && m.getXpath().equals(newPath)) {
                    LOG.error("BIZARRE found mapping for " + newPath + " by looping but not by map lookup. Needs further investigation");
                    LOG.info("valueMappings :" + valueMappings.toString());
                    LOG.debug("Loop found matching xpath for parent:{}", newPath);
                    return m;
                }
            }
        }
        return null;
    }

    private void createDateMapping(final Map<String, Object> values, final String keystub, final String datestring) {
        try {
            values.put(keystub, null);
            values.put(keystub + "_day", datestring.substring(0, 2));
            values.put(keystub + "_month", datestring.substring(3, 5));
            values.put(keystub + "_year", datestring.substring(6, 10));
        } catch (Exception e) {
            LOG.error("Failed to parse xml key:" + keystub + " from datestring:" + datestring);
        }
    }

    private String buildNodePath(final Node xml, final String parentPath, final Set<String> activeAttrsLowerCase) {
        String nodeName = xml.getNodeName();
        if (xml instanceof Element) {
            nodeName = nodeName + getAttrsAsString((Element) xml, activeAttrsLowerCase);
        }

        String nodePath;
        if (parentPath == null) {
            nodePath = nodeName;
        } else {
            nodePath = parentPath + PATH_SEPARATOR + nodeName;
        }

        // TODO put order in here
        return nodePath;
    }

    private Map<String, Integer> getSiblingCount(final NodeList nodes) {
        if (nodes == null) {
            return null;
        }

        Map<String, Integer> siblingCount = new HashMap<>();
        for (int index = 0; index < nodes.getLength(); index++) {
            Node node = nodes.item(index);
            if (node instanceof CharacterData) {
                continue;
            }

            String nodeName = node.getNodeName();
            Integer count = siblingCount.get(nodeName);
            if (count == null) {
                siblingCount.put(nodeName, Integer.valueOf(1));
            } else {
                siblingCount.put(nodeName, Integer.valueOf(count.intValue() + 1));
            }
        }
        return siblingCount;
    }

    private String getAttrsAsString(final Element element, final Set<String> activeAttrsLowerCase) {
        NamedNodeMap attrMap = element.getAttributes();
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < attrMap.getLength(); index++) {
            Node attr = attrMap.item(index);
            String attrName = attr.getNodeName();
            if (activeAttrsLowerCase.contains(attrName.toLowerCase())) {
                String attrValue = attr.getNodeValue();

                if (buffer.length() > 0) {
                    buffer.append(",");
                }
                buffer.append("[@").append(attrName).append("=\"").append(attrValue).append("\"]");
            }
        }
        return buffer.toString();
    }

    private static String join(final Map<String, ? extends Object> map, final String separator) {
        if (map == null) {
            return null;
        }
        String sep = separator;
        if (sep == null) {
            sep = "";
        }

        Set<String> keySet = map.keySet();
        keySet.remove(null);    // null causes a problem for sort
        List<String> keys = new ArrayList<String>(keySet);

        Collections.sort(keys);
        StringBuffer buffer = new StringBuffer();
        for (String key : keys) {
            if (buffer.length() > 0) {
                buffer.append(sep);
            }
            buffer.append(key).append(" = ").append(map.get(key));
        }
        return buffer.toString();
    }

    protected void createCollectionDetails() {}

    protected boolean createCollection(final Map<String, Object> values, final Node node, final String parentPath, final String data) {
        return true;
    }

    class CollectionDetails {
        private final String startWith;
        private final String newMapKey;
        private final String collectionName;
        public CollectionDetails(final String collectionName, final String startWith, final String newMapKey) {
            this.collectionName = collectionName;
            this.startWith = startWith;
            this.newMapKey = newMapKey;
        }

        public String getStartWith() {
            return startWith;
        }

        public String getNewMapKey() {
            return newMapKey;
        }

        public String getCollectionName() {
            return collectionName;
        }
    }
}
