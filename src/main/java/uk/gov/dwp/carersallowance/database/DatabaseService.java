package uk.gov.dwp.carersallowance.database;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
public interface DatabaseService {
    String getTransactionId(final String originTag);
    String getTransactionStatusById(final String transactionId);
    Boolean health();
    Boolean updateTransactionStatus(final String transactionId, final String status);
    Boolean insertTransactionStatus(final String transactionId, final String status, final Integer type, final Integer thirdParty,
                                    final Integer circsType, final String lang, final Integer jsEnabled, final Integer email,
                                    final Integer saveForLaterEmail, final String originTag);
    Boolean updateEmailStatus(final String transactionId, final Integer emailStatus);
    Boolean updateSaveForLaterEmailStatus(final String transactionId, final Integer emailSaveForLaterStatus);
    Boolean recordMi(final String transactionId, final Boolean thirdParty, final Integer circsChange, final String lang);
}
