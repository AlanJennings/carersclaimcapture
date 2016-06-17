package uk.gov.dwp.carersallowance.controller.breaks;

import java.util.Arrays;

/**
 * TODO decide whether this should be a runtime exception or not
 * (it is the spring way, but then again)
 */
public class IllegalFieldValueException extends RuntimeException {
    private static final long serialVersionUID = 2173688946186363846L;

    private String   fieldName;
    private String[] fieldValues;

    public IllegalFieldValueException(String fieldName, String[] fieldValues) {
        super();

        this.fieldName = fieldName;
        this.fieldValues = fieldValues;
    }

    public IllegalFieldValueException(String message, String fieldName, String[] fieldValues) {
        super(message);

        this.fieldName = fieldName;
        this.fieldValues = fieldValues;
    }

    public IllegalFieldValueException(String message, String fieldName, String[] fieldValues, Throwable cause) {
        super(message, cause);

        this.fieldName = fieldName;
        this.fieldValues = fieldValues;
    }

    public IllegalFieldValueException(String fieldName, String[] fieldValues, Throwable cause) {
        super(cause);

        this.fieldName = fieldName;
        this.fieldValues = fieldValues;
    }

    public String   getFieldName()   { return fieldName; }
    public String[] getFieldValues() { return fieldValues; }

    public String getMessage() {
        return super.getMessage() + " fieldName = " + fieldName + " values = " + (fieldValues == null ? null : Arrays.asList(fieldValues));
    }
}