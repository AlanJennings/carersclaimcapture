package uk.gov.dwp.carersallowance.submission;

import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface SubmitClaimService {
    void sendClaim(final HttpServletRequest request) throws IOException, InstantiationException, ParserConfigurationException, XPathMappingList.MappingException;
}
