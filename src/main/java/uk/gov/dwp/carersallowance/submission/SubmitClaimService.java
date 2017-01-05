package uk.gov.dwp.carersallowance.submission;

public interface SubmitClaimService {
    void sendClaim(final String xml, final String transactionId);
}
