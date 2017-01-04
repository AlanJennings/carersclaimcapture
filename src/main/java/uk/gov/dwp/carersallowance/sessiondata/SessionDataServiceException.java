package uk.gov.dwp.carersallowance.sessiondata;

import gov.dwp.exceptions.DwpRuntimeException;

/**
 * Created by peterwhitehead on 23/12/2016.
 */
public class SessionDataServiceException extends DwpRuntimeException {
    private static final long serialVersionUID = -4383391694077075756L;

    public SessionDataServiceException(String message) {
        super(message);
    }
    public SessionDataServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
