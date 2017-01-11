package uk.gov.dwp.carersallowance.submission;

import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;


import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface SubmitClaimService {
    Session getSession(final HttpServletRequest request);
    String retrieveTransactionId(final Session session);
    String getEmailBody(final HttpServletRequest request, final Session session);
    Boolean isClaim(final Session session);
    void sendClaim(final Session session, final String transactionId, final String emailBody) throws IOException, InstantiationException, ParserConfigurationException, XPathMappingList.MappingException;
}
