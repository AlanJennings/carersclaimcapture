package uk.gov.dwp.carersallowance.xml;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

@Component
public class XmlClaimBuilder extends XmlBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(XmlClaimBuilder.class);

    private final Integer maxAge;

    @Inject
    public XmlClaimBuilder(@Value("${xml.root.node}") final String rootNodeName,
                           final MessageSource messageSource,
                           final ServerSideResolveArgs serverSideResolveArgs,
                           @Value("${xml.mapping}") final String xmlMapping,
                           @Value("${assisted.decision.max.age}") final Integer maxAge) throws ParserConfigurationException, IOException, XPathMappingList.MappingException {
        super(rootNodeName, messageSource, serverSideResolveArgs, xmlMapping);
        this.maxAge = maxAge;
    }

    @Override
    public void loadValuesIntoXML(final Map<String, Object> values) throws ParserConfigurationException {
        super.loadValuesIntoXML(values);
        updateBreaks(getDocument());
    }

    private void updateBreaks(final Document document) {
        int count = 0;
        String hospital = "";
        String respite = "";
        String other = "No";
        Node firstCareBreak = null;

        //get breaks change first if others exist and add new at end for None and No
        Node node = getNamedNode("DWPBody/DWPCATransaction/DWPCAClaim/Caree", null, false, document);
        Node lastNode = getNamedNode("DWPBody/DWPCATransaction/DWPCAClaim/Caree/LiveSameAddress", null, false, document);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node1 = nodeList.item(i);
            if ("CareBreak".equals(node1.getNodeName())) {
                if (count == 0) {
                    firstCareBreak = node1;
                }
                count++;
                NodeList nodeList1 = node1.getChildNodes();
                for (int x = 0; x < nodeList1.getLength(); x++) {
                    Node node2 = nodeList1.item(x);
                    //Answer
                    String value = node2.getFirstChild().getNextSibling().getFirstChild().getNodeValue();
                    if ("DPHospital".equals(value) || "YouHospital".equals(value)) {
                        hospital = "Hospital, ";
                    } else if ("DPRespite".equals(value) || "YouRespite".equals(value)) {
                        respite = "Respite or care home, ";
                    } else if ("Other".equals(value)) {
                        other = "Yes";
                    }
                }
            }
        }
        if (count >= 2) {
            Node copyNode = firstCareBreak.cloneNode(true);
            node.insertBefore(copyNode, lastNode);
            firstCareBreak.getFirstChild().getFirstChild().getNextSibling().getFirstChild().setTextContent(StringUtils.substringBeforeLast(hospital + respite, ",").trim());
            firstCareBreak.getLastChild().getFirstChild().getNextSibling().getFirstChild().setTextContent(other);
        }
    }

    @Override
    protected Map<String, String> getNamespaces() {
        Map<String, String> namespaces = new HashMap<>();
        namespaces.put("xmlns", "http://www.govtalk.gov.uk/dwp/carers-allowance");
        namespaces.put("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
        namespaces.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaces.put("xsi:schemaLocation", "http://www.govtalk.gov.uk/dwp/carers-allowance file:/future/schema/ca/CarersAllowance_Schema.xsd");
        return namespaces;
    }

    @Override
    protected void add(final List<Map<String, Object>> fieldCollectionList, final Document document, final String elementName, final String xPath) {
        // create enclosing node, one per list item using order attribute, then create the inner values
        if (fieldCollectionList == null) {
            return;
        }
        try {
            String elementNameToUse = elementName;
            String xPathToUse = xPath;
            String mappingNameToUse = elementName;
            replaceCarerCaree(fieldCollectionList);
            for (int index = 0; index < fieldCollectionList.size(); index++) {
                Map<String, Object> fieldCollection = fieldCollectionList.get(index);
                if (elementName.startsWith("breaks")) {
                    //remove end of xPath and use CareBreak as element name
                    elementNameToUse = "CareBreak";
                    xPathToUse = StringUtils.substringBeforeLast(xPath, "/");
                    mappingNameToUse = getMappingNameToUse(fieldCollection, elementName);
                    getValueMappings().putIfAbsent(mappingNameToUse, loadXPathMappings(mappingNameToUse, xmlMapping + "." + mappingNameToUse.toLowerCase()).get(mappingNameToUse));
                }
                Element childNode = document.createElement(elementNameToUse);
                addNodes(fieldCollection, mappingNameToUse, childNode);
                Node node = getNamedNode(xPathToUse, null, false, document);
                node.appendChild(childNode);
            }
        } catch (Exception e) {
            LOG.error("Unable to add collection.", e);
        }
    }

    private String getMappingNameToUse(Map<String, Object> fieldCollection, String elementName) {
        String breakType = StringUtils.substringAfter(elementName, "breaks");
        String breaksTypeKeyToCheck = breakType + "BreakWhoIn" + StringUtils.capitalize(breakType);
        if (fieldCollection.get(breaksTypeKeyToCheck) != null) {
            return "breaks" + (fieldCollection.get(breaksTypeKeyToCheck).equals("Carer") ? "you" : "dp") + breakType;
        }
        return elementName;
    }

    @Override
    protected void addAdditionalValues(final Map<String, Object> values) {
        AssistedDecision assistedDecision = new AssistedDecision(maxAge, values);
        values.put("assistedDecisionReason", assistedDecision.getReason());
        values.put("assistedDecisionDecision", assistedDecision.getDecision());
    }

    private void replaceCarerCaree(final List<Map<String, Object>> fieldCollectionList) {
        for (Map<String, Object> fieldCollection : fieldCollectionList) {
            for (String key : fieldCollection.keySet()) {
                Object value = fieldCollection.get(key);
                if ("Carer".equals(value)) {
                    fieldCollection.put(key, "You");
                } else if ("Caree".equals(value)) {
                    fieldCollection.put(key, "@dpname");
                }
            }
        }
    }
}