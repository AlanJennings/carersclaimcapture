package uk.gov.dwp.carersallowance.controller;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.utils.C3Constants;
import uk.gov.dwp.carersallowance.utils.LoadFile;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;
import uk.gov.dwp.carersallowance.xml.XmlClaimReader;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class XmlCarerContactDetailsTest {
    private static final Logger LOG = LoggerFactory.getLogger(XmlCarerContactDetailsTest.class);
    private XPathMappingList valueMappings;

    @Before
    public void setUp() throws Exception {
        URL claimTemplateUrl = XmlClaimReader.class.getClassLoader().getResource("xml.mapping.claim");
        List<String> xmlMappings = LoadFile.readLines(claimTemplateUrl);
        valueMappings = new XPathMappingList();
        valueMappings.add(xmlMappings);
    }

    @Test
    public void loadCarerContactDetailsTest() throws Exception {
        URL xmlfile = XmlClaimReader.class.getClassLoader().getResource("claimreader-carercontactdetails.xml");
        String xml = FileUtils.readFileToString(new File(xmlfile.toURI()), Charset.defaultCharset());

        XmlClaimReader claimReader = new XmlClaimReader(xml, valueMappings, true);
        assertThat(claimReader.getErrors().size(), is(0));
        Map<String, Object> sessionValues = claimReader.getValues();
        Map<String, Object> values = claimReader.getValues();
        for (String name : values.keySet()) {
            LOG.debug("VALUE:{}=>{}\n", name, values.get(name));
        }
        assertThat(sessionValues.get("carerWantsEmailContact"), is(C3Constants.YES));
        assertThat(sessionValues.get("carerMail"), is("cads.dwp@gmail.com"));
    }
}