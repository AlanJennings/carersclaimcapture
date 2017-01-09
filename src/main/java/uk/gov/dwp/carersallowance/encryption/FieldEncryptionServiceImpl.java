package uk.gov.dwp.carersallowance.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by peterwhitehead on 29/12/2016.
 */
@Component
public class FieldEncryptionServiceImpl implements FieldEncryptionService {
    private static final Logger LOG = LoggerFactory.getLogger(FieldEncryptionServiceImpl.class);

    private final String privateKey;
    private final Boolean encryptFields;

    public FieldEncryptionServiceImpl(@Value("${c3.crypto.secret}") final String privateKey, @Value("${encryptFields}") final Boolean encryptFields) {
        this.privateKey = privateKey.substring(0, 16);
        this.encryptFields = encryptFields;
    }

    public String encryptAES(final String fieldValue) {
        try {
            if (encryptFields) {
                Cipher cipher = getCipher(getPrivateKey(privateKey), Cipher.ENCRYPT_MODE);
                return DatatypeConverter.printHexBinary(cipher.doFinal(fieldValue.getBytes("utf-8")));
            }
            return fieldValue;
        } catch (Exception e) {
            LOG.debug("Could not encrypt string, {}", e.getMessage());
            return fieldValue;
        }
    }

    public String decryptAES(final String fieldValue) {
        try {
            if (encryptFields) {
                Cipher cipher = getCipher(getPrivateKey(privateKey), Cipher.DECRYPT_MODE);
                return new String(cipher.doFinal(DatatypeConverter.parseHexBinary(fieldValue)));
            }
            return fieldValue;
        } catch (Exception e) {
            LOG.debug("Could not decrypt string, {}", e.getMessage());
            return fieldValue;
        }
    }

    private byte[] getPrivateKey(final String privateKey) throws Exception {
        return privateKey.getBytes("utf-8");
    }

    private Cipher getCipher(final byte[] key, final Integer mode) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(mode, secretKeySpec);
        return cipher;
    }
}
