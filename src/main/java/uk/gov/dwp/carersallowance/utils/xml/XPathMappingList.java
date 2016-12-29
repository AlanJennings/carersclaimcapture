package uk.gov.dwp.carersallowance.utils.xml;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.gov.dwp.carersallowance.controller.submission.SubmitClaimController;
import uk.gov.dwp.carersallowance.utils.KeyValue;

public class XPathMappingList {
    private static final String DEFAULT_SEPARATOR = "=";

    private List<XPathMapping>        list;
    private Map<String, XPathMapping> valueMap;   // XPathMapping.value vs XPathMapping
    private Map<String, XPathMapping> xpathMap;  // XPathMapping vs XPathMapping.value

    private List<XPathMapping>        immutableList;
    private Map<String, XPathMapping> immutableValueMap;
    private Map<String, XPathMapping> immutableXPathMap;

    public XPathMappingList() {
        list = new ArrayList<>();
        valueMap = new HashMap<>();
        xpathMap = new HashMap<>();
    }

    public void add(List<String> mappings) throws MappingException {
        add(mappings, DEFAULT_SEPARATOR);
    }

    public void add(List<String> mappings, String separator) throws MappingException {
        if(mappings == null) {
            return;
        }
        for(String mapping : mappings) {
            add(mapping, separator);
        }
    }

    public void add(String line, String separator) throws MappingException {
        if(line == null) {
            return;
        }
        line = line.trim();
        if(line.equals("") || line.startsWith("#")) {
            return;
        }

        KeyValue valueAndXpath = new KeyValue(line, separator);
        String value = valueAndXpath.getKey();
        String rawXPath = valueAndXpath.getValue();

        KeyValue xPathAndProcessingInstruction = new KeyValue(rawXPath, "[", "]");
        String xPath = xPathAndProcessingInstruction.getKey();
        String processingInstruction = xPathAndProcessingInstruction.getValue();

        XPathMapping item = new XPathMapping(value, xPath, processingInstruction);
        add(item);
    }

    public void add(XPathMapping item) throws MappingException {
        if(item == null) {
            return;
        }

        if(valueMap.containsKey(item.getValue())) {
            XPathMapping existingItem = valueMap.get(item.getValue());
            throw new MappingException("value(" + item.getValue() + ") is already mapped to: " + existingItem.getXpath());
        }

        // e.g value                 = carerAddressLineOne
        //     xpath                 = DWPBody/.../Answer/Line
        //     processingInstruction = @order=1
        // if processingInstruction starts with @ then append to xpath e.g. DWPBody/.../Answer/Line[@order="1"]
        // Or may refer to attribute e.g. "/DWPBody/DWPCATransaction/@id"
        String qualifiedXPath = item.getXpath();
        if(qualifiedXPath != null && item.getProcessingInstruction() != null) {
            if(item.getProcessingInstruction().startsWith("messages(") == false) {
                qualifiedXPath = qualifiedXPath + "[" + item.getProcessingInstruction() + "]";
            }
        }
        if(xpathMap.containsKey(qualifiedXPath)) {
            XPathMapping existingItem = xpathMap.get(qualifiedXPath);
            throw new MappingException("value(" + qualifiedXPath + ") is already mapped to: " + existingItem.getValue());
        }

        list.add(item);
        if(item.getValue() != null) {
            valueMap.put(item.getValue(), item);
        }

        xpathMap.put(qualifiedXPath, item);

        immutableList = null;
        immutableValueMap = null;
        immutableXPathMap = null;
    }

    public List<XPathMapping> getList() {
        synchronized(list) {
            if(immutableList == null) {
                immutableList = Collections.unmodifiableList(list);
            }
            return immutableList;
        }
    }

    public Map<String, XPathMapping> getValueMap() {
        synchronized(valueMap) {
            if(immutableValueMap == null) {
                immutableValueMap = Collections.unmodifiableMap(valueMap);
            }
            return immutableValueMap;
        }
    }

    public Map<String, XPathMapping> getXPathMap() {
        synchronized(xpathMap) {
            if(immutableXPathMap == null) {
                immutableXPathMap = Collections.unmodifiableMap(xpathMap);
            }
            return immutableXPathMap;
        }
    }

    public static class MappingException extends Exception {
        private static final long serialVersionUID = 3130605572024786063L;

        public MappingException() {
            super();
        }

        public MappingException(String message) {
            super(message);
        }

        public MappingException(String message, Throwable cause) {
            super(message, cause);
        }

        public MappingException(Throwable cause) {
            super(cause);
        }
    }
}
