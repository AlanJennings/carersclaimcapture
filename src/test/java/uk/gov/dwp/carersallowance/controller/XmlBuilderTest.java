package uk.gov.dwp.carersallowance.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.utils.xml.ClaimXmlUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class XmlBuilderTest {
    private static final Logger LOG = LoggerFactory.getLogger(XmlBuilderTest.class);
    private Map<String, Object> sessionMap;

    private String appVersion = "3.14";
    private String xmlVersion = "0.27";
    private String transactionId = "16011234";
    private String origin = "GB";
    private String language = "English";

    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        sessionMap = new HashMap<>();
        sessionMap.put("transactionId", transactionId);
        sessionMap.put("transactionIdAttr", transactionId);
        sessionMap.put("dateTimeGenerated", ClaimXmlUtil.currentDateTime("dd-MM-yyyy HH:mm"));
        sessionMap.put("xmlVersion", xmlVersion);
        sessionMap.put("appVersion", appVersion);
        sessionMap.put("origin", origin);
        sessionMap.put("language", language);
    }

    @Test
    public void testDocument() {
        try {
            XmlBuilder xmlBuilder = new XmlBuilder("DWPBody", sessionMap);
            assertThat(xmlBuilder.getNodeValue("/DWPBody/Version"), is(appVersion));
        } catch (Exception e) {
            LOG.error("Exception calling XmlBuilder :", e);
        }
    }

    @Test
    public void testXml() {
        try {
            XmlBuilder xmlBuilder = new XmlBuilder("DWPBody", sessionMap);
            String xml = xmlBuilder.render(true, false);
            assertTrue(xml.startsWith("<?xml"));
            assertTrue(xml.contains("<DWPBody"));
        } catch (Exception e) {
            LOG.error("Exception calling XmlBuilder :", e);
        }
    }
}