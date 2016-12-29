package uk.gov.dwp.carersallowance.sessiondata;

import gov.dwp.exceptions.DwpRuntimeException;

/**
 * Created by peterwhitehead on 28/12/2016.
 */
public class SessionTimeoutException extends DwpRuntimeException {
    public SessionTimeoutException(String message) {
        super(message);
    }
    public SessionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
