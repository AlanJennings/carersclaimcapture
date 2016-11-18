package uk.gov.dwp.carersallowance.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.gov.dwp.carersallowance.utils.xml.XPathMapping;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList.MappingException;
import uk.gov.dwp.carersallowance.utils.xml.XmlPrettyPrinter;

public class XmlClaimReader {
    private static final String PATH_SEPARATOR = "/";

    private XPathMappingList    valueMappings;
    private Map<String, Object> values;

    public XmlClaimReader(String xml, XPathMappingList valueMappings) throws InstantiationException {
        this((Document)XmlPrettyPrinter.stringToNode(xml), valueMappings);
    }

    public XmlClaimReader(Document xml, XPathMappingList valueMappings) {
        this.valueMappings = valueMappings;

        values = new HashMap<>();
        parseXml(values, xml.getFirstChild(), null);
    }

    public Map<String, Object> getValues() {
        return values;
    }

    private void parseXml(Map<String, Object> values, Node xml, String parentPath) {
        if(xml == null) {
            return;
        }

        if(xml instanceof CharacterData) {
            String data = ((CharacterData)xml).getData();
            XPathMapping mapping = valueMappings.getXPathMap().get(parentPath);
            if(mapping == null) {
                throw new RuntimeException("Unknown mapping for " + parentPath);
            }
            String key = mapping.getValue();
            values.put(key, data);
        }

        String nodePath;
        if(parentPath == null) {
            nodePath = xml.getNodeName();
        } else {
            nodePath = parentPath + PATH_SEPARATOR + xml.getNodeName();
        }

        NodeList children = xml.getChildNodes();
        for(int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            parseXml(values, child, nodePath);
        }
    }

    public static void main(String[] args) throws IOException, MappingException, InstantiationException {
        URL claimTemplateUrl = XmlClaimReader.class.getClassLoader().getResource("xml.mapping.claim");
        List<String> xmlMappings = SubmitClaimController.readLines(claimTemplateUrl);
        XPathMappingList valueMappings = new XPathMappingList();
        valueMappings.add(xmlMappings);

        String filename = "/Users/drh/development-java/CarersClaimCapture/CarersClaimCapture/data/decrypted.xml";
        String xml = FileUtils.readFileToString(new File(filename), Charset.defaultCharset());

        XmlClaimReader claimReader = new XmlClaimReader(xml, valueMappings);
        System.out.println(claimReader.getValues());
    }
}