package uk.gov.dwp.carersallowance.xml;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.utils.xml.ClaimXmlUtil;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ClaimAssistedDecisionTest {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimHeaderItemsTest.class);

    private Map<String, Object> sessionMap;

    private String appVersion = "3.14";
    private String xmlVersion = "0.27";
    private String transactionId = "16011234";
    private String origin = "GB";
    private String language = "English";

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
    public void checkClaimXmlContainsDefaultAssistedDecisionReason() throws Exception{
        XmlBuilder xmlBuilder = new XmlBuilder("DWPBody", sessionMap);
        String reason = xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/AssistedDecisions/AssistedDecision/Reason");
        String decision = xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/AssistedDecisions/AssistedDecision/RecommendedDecision");
        assertThat(reason, is("Check CIS for benefits. Send Pro517 if relevant."));
        assertThat(decision, is("Potential award,show table"));
    }

    @Test
    public void checkClaimXmlContainsAFIPAssistedDecisionReason() throws Exception{
        sessionMap.put("benefitsAnswer", "AFIP");
        XmlBuilder xmlBuilder = new XmlBuilder("DWPBody", sessionMap);
        String reason = xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/AssistedDecisions/AssistedDecision/Reason");
        String decision = xmlBuilder.getNodeValue("/DWPBody/DWPCATransaction/DWPCAClaim/AssistedDecisions/AssistedDecision/RecommendedDecision");
        assertThat(reason, is("Assign to AFIP officer on CAMLite workflow."));
        assertThat(decision, is("None,show table"));
    }
}
