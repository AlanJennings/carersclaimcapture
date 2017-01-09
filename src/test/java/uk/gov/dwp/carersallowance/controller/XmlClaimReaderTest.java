package uk.gov.dwp.carersallowance.controller;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
import uk.gov.dwp.carersallowance.xml.XmlBuilder;
import uk.gov.dwp.carersallowance.xml.XmlClaimReader;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class XmlClaimReaderTest {
    private static final Logger LOG = LoggerFactory.getLogger(XmlClaimReaderTest.class);
    private XPathMappingList valueMappings;

    @Before
    public void setUp() throws Exception {
        URL claimTemplateUrl = XmlClaimReader.class.getClassLoader().getResource("xml.mapping.claim");
        List<String> xmlMappings = XmlBuilder.readLines(claimTemplateUrl);
        valueMappings = new XPathMappingList();
        valueMappings.add(xmlMappings);
    }

    @Test
    public void simpleXmlLoadTest() throws Exception {
        LOG.debug("Doing simple xml test");
        String simplexml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<DWPBody>\n" +
                "    <Version>0.27</Version>\n" +
                "    <ClaimVersion>3.15</ClaimVersion>\n" +
                "    <Origin>GB</Origin>\n" +
                "    <DWPCATransaction>\n" +
                "        <DWPCAClaim>\n" +
                "            <DateOfClaim>\n" +
                "                <QuestionLabel>Claim date</QuestionLabel>\n" +
                "                <Answer type=\"date\">20-04-2016</Answer>\n" +
                "            </DateOfClaim>\n" +
                "            <QualifyingBenefit>\n" +
                "                <QuestionLabel>What benefit does the person you care for get?</QuestionLabel>\n" +
                "                <Answer>AA</Answer>\n" +
                "            </QualifyingBenefit>\n" +
                "        </DWPCAClaim>\n" +
                "    </DWPCATransaction>\n" +
                "</DWPBody>";

        XmlClaimReader claimReader = new XmlClaimReader(simplexml, valueMappings, true);
        assertThat(claimReader.getErrors().size(), is(0));
        Map<String, Object> sessionValues = claimReader.getValues();
        assertThat(sessionValues.get("xmlVersion"), is("0.27"));
        assertThat(sessionValues.get("origin"), is("GB"));
        assertThat(sessionValues.get("appVersion"), is("3.15"));
        assertThat(sessionValues.get("dateOfClaim_day"), is("20"));
        assertThat(sessionValues.get("dateOfClaim_month"), is("04"));
        assertThat(sessionValues.get("dateOfClaim_year"), is("2016"));
        assertThat(sessionValues.get("benefitsAnswer"), is("AA"));
    }

    @Test
    public void loadXmlThirdPartyTest() throws Exception {
        URL xmlfile = XmlClaimReader.class.getClassLoader().getResource("claimreader-thirdparty.xml");
        String xml = FileUtils.readFileToString(new File(xmlfile.toURI()), Charset.defaultCharset());

        XmlClaimReader claimReader = new XmlClaimReader(xml, valueMappings, true);
        assertThat(claimReader.getErrors().size(), is(0));
        Map<String, Object> sessionValues = claimReader.getValues();
        Map<String, Object> values = claimReader.getValues();
        for (String name : values.keySet()) {
            LOG.debug("VALUE:{}=>{}\n", name, values.get(name));
        }
        assertThat(sessionValues.get("thirdParty"), is("no"));
        assertThat(sessionValues.get("nameAndOrganisation"), is("Jenny Bloggs Preston carers"));
    }
}