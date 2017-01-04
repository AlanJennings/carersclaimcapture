package uk.gov.dwp.carersallowance.encryption;

import uk.gov.dwp.carersallowance.sessiondata.Session;

import java.util.Map;

/**
 * Created by peterwhitehead on 29/12/2016.
 */
public interface ClaimEncryptionService {
    Session encryptClaim(final Session session);
    Session decryptClaim(final Session session);
}
