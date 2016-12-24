package uk.gov.dwp.carersallowance.xml;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.carersallowance.utils.xml.ClaimXmlUtil.getNodeValue;

public class ClaimAssistedDecisionTest {
    private Document document;
    private String reason = "Send DS790/790B COMB to customer.";
    private String recommendedDecision = "None,show table";

    @Before
    public void setUp() throws Exception {
        /*
        ClaimXml claimXml = new ClaimXml();
        //"DateOfClaim"="10-10-2016";
        document = claimXml.buildXml(null, "transid", "appversion", "schemaversion", "origin", "language");
        */
    }

    @Test
    public void checkClaimXmlContainsAssistedDecisionReason() {
//        assertThat(getNodeValue(document, "/DWPBody/DWPCATransaction/DWPCAClaim/AssistedDecisions/AssistedDecision/Reason"), is(reason));
    }

    @Test
    public void checkClaimXmlContainsAssistedDecisionRecommendedDecision() {
 //       assertThat(getNodeValue(document, "/DWPBody/DWPCATransaction/DWPCAClaim/AssistedDecisions/AssistedDecision/RecommendedDecision"), is(recommendedDecision));
    }
}
