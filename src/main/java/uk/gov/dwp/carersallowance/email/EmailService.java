package uk.gov.dwp.carersallowance.email;

import uk.gov.dwp.carersallowance.sessiondata.Session;

/**
 * Created by peterwhitehead on 06/01/2017.
 */
public interface EmailService {
    void sendEmail(final String emailBody, final Session session, final String transactionId) throws Exception;
}
