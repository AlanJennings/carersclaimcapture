package uk.gov.dwp.carersallowance.validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationFactory {
    static final Logger LOG = LoggerFactory.getLogger(ValidationFactory.class);

    public static Validation getValidation(ValidationType type) {
        if(type == null) {
            return null;
        }

        switch(type) {
        case MANDATORY:
            return MandatoryValidation.INSTANCE;

            case MAX_LENGTH:
            case DATE:
            case ADDRESS:
            case GROUP_ANY:
            case GROUP_ALL:
            case REGEX:
            case CONFIRM_FIELD:
                throw new UnsupportedOperationException("Unsupported type: " + type);

            default:
                throw new IllegalArgumentException("Unknown validation type: " + type);
        }
    }
}