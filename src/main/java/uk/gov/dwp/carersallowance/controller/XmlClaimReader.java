package uk.gov.dwp.carersallowance.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.gov.dwp.carersallowance.controller.submission.SubmitClaimController;
import uk.gov.dwp.carersallowance.utils.xml.XPathMapping;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList.MappingException;
import uk.gov.dwp.carersallowance.utils.xml.XmlPrettyPrinter;

/**
 * Read the claim data from the XML document
 *
 * When writing the XML document, we write the questions as well (labels), we don't need to read these
 * as they come from another source (messageSource)
 *
 * When we read duplicates from the XML they should be annotated with an order attribute, so all xpaths are unique
 * When we read a node with a @type attribute it implies an instruction (nominally for date parsing)
 */
public class XmlClaimReader {
    private static final Logger LOG = LoggerFactory.getLogger(XmlClaimReader.class);

    private Set<String> NOT_SUPPORTED = new HashSet<>(Arrays.asList(new String[]{"DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak"}));
    private static final Set<String> ACTIVE_ATTRS = new HashSet<>(Arrays.asList(new String[]{"type", "order"}));
    private static final String PATH_SEPARATOR = "/";
    private static final Set<String> IGNORE_MAPPING = new HashSet<>(Arrays.asList(new String[]{
            "DWPBody/DWPCATransaction/DWPCAClaim/EvidenceList/Evidence/Title"       // TODO
    }));

    private XPathMappingList valueMappings;
    private Map<String, Object> values;
    private List<String> errors;
    private boolean sessionVariablesOnly;

    public XmlClaimReader(String xml, XPathMappingList valueMappings, boolean sessionVariablesOnly) throws InstantiationException {
        this((Document) XmlPrettyPrinter.stringToNode(xml), valueMappings, sessionVariablesOnly);
    }

    public XmlClaimReader(Document xml, XPathMappingList valueMappings, boolean sessionVariablesOnly) {
        this.valueMappings = valueMappings;
        this.sessionVariablesOnly = sessionVariablesOnly;

        errors = new ArrayList<>();
        values = new HashMap<>();
        parseXml(values, xml.getFirstChild(), null, 0, ACTIVE_ATTRS);
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public List<String> getErrors() {
        return errors;
    }

    private void parseXml(Map<String, Object> values, Node node, String parentPath, Integer parentOrder, Set<String> activeAttrsLowerCase) {
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
        String nodePath = buildNodePath(node, parentPath, parentOrder, activeAttrsLowerCase);

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
                childOrder = null;
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
            parseXml(values, child, nodePath, childOrder, activeAttrsLowerCase);
        }
    }

    private void createMapping(Map<String, Object> values, Node xml, String parentPath) {
        String data = ((CharacterData) xml).getData();
        LOG.debug("Found Text: {}", data);

        XPathMapping mapping = valueMappings.getXPathMap().get(parentPath);

        // TODO ... fix this so dont need to loop around looking for DateOfClaim ... which exists but was not returned by get
        // BIZARRE ??? Test works OK but running in app doesnot find mapping for DWPBody/DWPCATransaction/DWPCAClaim/DateOfClaim/Answer
        if(mapping == null){
            List<XPathMapping> mappings=valueMappings.getList();
            for( XPathMapping m : mappings){
                if(m!=null && m.getXpath()!=null && m.getXpath().equals(parentPath)){
                    LOG.error("BIZARRE found mapping for "+parentPath+" by looping but not by map lookup. Needs further investigation");
                    System.out.println("Loop found matching xpath for parent:"+parentPath);
                    mapping=m;
                }
            }
        }

        if (IGNORE_MAPPING.contains(parentPath)) {
            return; // do nothing
        } else if (mapping == null) {
            LOG.error("Unknown mapping for {}", parentPath);
            errors.add("Unknown mapping for " + parentPath);
            throw new IllegalStateException("Unknown mapping for " + parentPath);
        } else {
            String key = mapping.getValue();
            String processingInstruction = mapping.getProcessingInstruction();
            if (sessionVariablesOnly && key != null && (key.startsWith("/") || key.contains("."))) {
                // do not store non-session variables
            } else if (processingInstruction != null && processingInstruction.equals("@type=\"date\"")) {
                LOG.info("XmlClaim adding date:" + key + "->" + data);
                createDateMapping(key, data);
            } else {
                LOG.info("XmlClaim adding string:" + key + "->" + data);
                // TODO Fixup case on yes / no to be consistent between claim and webapp
                values.put(key, data.replace("No", "no").replace("Yes", "yes"));
            }
        }
    }

    private void createDateMapping(String keystub, String datestring) {
        try {
            values.put(keystub, null);
            values.put(keystub + "_day", datestring.substring(0, 2));
            values.put(keystub + "_month", datestring.substring(3, 5));
            values.put(keystub + "_year", datestring.substring(6, 10));
        } catch (Exception e) {
            LOG.error("Failed to parse xml key:" + keystub + " from datestring:" + datestring);
        }
    }

    private String buildNodePath(Node xml, String parentPath, Integer parentOrder, Set<String> activeAttrsLowerCase) {
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

    private Map<String, Integer> getSiblingCount(NodeList nodes) {
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

    private String getAttrsAsString(Element element, Set<String> activeAttrsLowerCase) {
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

    public static String join(Map<String, ? extends Object> map, String separator) {
        if (map == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }

        Set<String> keySet = map.keySet();
        keySet.remove(null);    // null causes a problem for sort
        List<String> keys = new ArrayList<String>(keySet);

        Collections.sort(keys);
        StringBuffer buffer = new StringBuffer();
        for (String key : keys) {
            if (buffer.length() > 0) {
                buffer.append(separator);
            }
            buffer.append(key).append(" = ").append(map.get(key));
        }
        return buffer.toString();
    }
}