package uk.gov.dwp.carersallowance.encryption;

import gov.dwp.carers.security.encryption.EncryptorAES;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import uk.gov.dwp.carersallowance.sessiondata.Session;

import javax.xml.bind.DatatypeConverter;
import java.util.*;

/**
 * Created by peterwhitehead on 29/12/2016.
 */
@Service
public class ClaimEncryptionServiceImpl implements ClaimEncryptionService {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimEncryptionServiceImpl.class);

    private EncryptorAES encryptorAES;
    private final Boolean cacheEncryptionEnabled;
    private final static String ENCRYPTION_FIELDS = "claim.encryption.fields";
    private final List<String> fieldsToEncrypt;

    public ClaimEncryptionServiceImpl(@Value("${cacheEncryptionEnabled}") final Boolean cacheEncryptionEnabled, final MessageSource messageSource) {
        this.cacheEncryptionEnabled = cacheEncryptionEnabled;
        encryptorAES = new EncryptorAES();
        final String fields = messageSource.getMessage(ENCRYPTION_FIELDS, null, null, Locale.getDefault());
        if (StringUtils.isEmpty(fields)) {
            fieldsToEncrypt = new ArrayList<>();
        } else {
            String[] array = fields.split(",");
            Arrays.parallelSetAll(array, (i) -> array[i].trim());
            fieldsToEncrypt = Arrays.asList(array);
        }
    }

    @Override
    public Session encryptClaim(final Session session) {
        return processList(session, true);
    }

    @Override
    public Session decryptClaim(final Session session) {
        return processList(session, false);
    }

    private Session processList(final Session session, final Boolean encrypt) {
        if (cacheEncryptionEnabled) {
            for (final String field : fieldsToEncrypt) {
                Object value = session.getAttribute(field);
                if (value != null) {
                    if (encrypt) {
                        session.setAttribute(field, encrypt(value));
                    } else {
                        session.setAttribute(field, decrypt(value));
                    }
                }
            }
        }
        return session;
    }

    private String decrypt(Object fieldValue) {
        try {
            if (fieldValue == null) {
                return null;
            }
            final String value = (String)fieldValue;
            if (StringUtils.isEmpty(value)) {
                return value;
            }
            return encryptorAES.decrypt(DatatypeConverter.parseBase64Binary(value));
        } catch (ClassCastException cce) {
            LOG.debug("Could not decrypt string, {}", cce.getMessage());
            return fieldValue.toString();
        } catch (Exception e) {
            LOG.debug("Could not decrypt string, {}", e.getMessage());
            return (String)fieldValue;
        }
    }

    private String encrypt(Object fieldValue) {
        try {
            if (fieldValue == null) {
                return null;
            }
            final String value = (String)fieldValue;
            if (StringUtils.isEmpty(value)) {
                return value;
            }
            return DatatypeConverter.printBase64Binary(encryptorAES.encrypt(value));
        } catch (ClassCastException cce) {
            LOG.debug("Could not encrypt string, {}", cce.getMessage());
            return fieldValue.toString();
        } catch (Exception e) {
            LOG.debug("Could not encrypt string, {}", e.getMessage());
            return (String)fieldValue;
        }
    }
}
