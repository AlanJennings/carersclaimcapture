package uk.gov.dwp.carersallowance.encryption;

/**
 * Created by peterwhitehead on 04/01/2017.
 */
public interface FieldEncryptionService {
    String encryptAES(final String fieldValue);
    String decryptAES(final String fieldValue);
}
