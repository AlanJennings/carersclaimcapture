package uk.gov.dwp.carersallowance.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class TransactionIdService {
    private final DatabaseService databaseService;
    private final String originTag;

    @Inject
    public TransactionIdService(final DatabaseService databaseService, @Value("${origin.tag}") final String originTag) {
        this.databaseService = databaseService;
        this.originTag = originTag;
    }

    public String getTransactionId() {
        return databaseService.getTransactionId(originTag);
    }

    public String getTransactionStatusById(final String transactionId) {
        return databaseService.getTransactionStatusById(transactionId);
    }

    public Boolean setTransactionStatusById(final String transactionId, final String status) {
        return databaseService.updateTransactionStatus(transactionId, status);
    }

    public Boolean insertTransactionStatus(final String transactionId, final String status, final Integer type, final Integer thirdParty,
                                    final Integer circsType, final String lang, final Integer jsEnabled, final Integer email, final Integer saveForLaterEmail) {
        return databaseService.insertTransactionStatus(transactionId, status, type, thirdParty, circsType, lang, jsEnabled, email, saveForLaterEmail, originTag);
    }
}