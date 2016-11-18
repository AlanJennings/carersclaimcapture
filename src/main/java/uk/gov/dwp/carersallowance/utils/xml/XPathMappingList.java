package uk.gov.dwp.carersallowance.utils.xml;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.gov.dwp.carersallowance.controller.SubmitClaimController;

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

        XPathMapping item;
        if(separator == null) {
            item = new XPathMapping(line, null);
        } else {
            int pos = line.indexOf(separator);
            if(pos < 0) {
                item = new XPathMapping(line, null);
            } else {
                String fieldName = line.substring(0, pos).trim();
                String xpath;
                if(pos == (line.length() -1)) {
                    xpath = null;
                } else {
                    xpath = line.substring(pos + 1).trim();
                }

                item = new XPathMapping(fieldName, xpath);
            }
        }
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
        if(xpathMap.containsKey(item.getXpath())) {
            XPathMapping existingItem = xpathMap.get(item.getXpath());
            throw new MappingException("value(" + item.getXpath() + ") is already mapped to: " + existingItem.getValue());
        }

        list.add(item);
        valueMap.put(item.getValue(), item);
        xpathMap.put(item.getXpath(), item);

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

    public static void main(String[] args) throws IOException, MappingException {
        URL claimTemplateUrl = XPathMappingList.class.getClassLoader().getResource("xml.mapping.claim");
        List<String> xmlMappings = SubmitClaimController.readLines(claimTemplateUrl);
        XPathMappingList valueMappings = new XPathMappingList();
        valueMappings.add(xmlMappings);
        System.out.println(valueMappings.getList());
    }
}
