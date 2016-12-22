package uk.gov.dwp.carersallowance.xml;

import gov.dwp.exceptions.DwpRuntimeException;

public class AssistedDecisionException extends DwpRuntimeException {
    public AssistedDecisionException(String message) {
        super(message);
    }
}