package uk.gov.dwp.carersallowance.session;

public class InconsistentFieldValuesException extends IllegalFieldValueException {
    private static final long serialVersionUID = 4020774673056044233L;

    public InconsistentFieldValuesException(String fieldName, String[] fieldValues) {
        super(fieldName, fieldValues);

    }

    public InconsistentFieldValuesException(String message, String fieldName, String[] fieldValues) {
        super(message, fieldName, fieldValues);
    }

    public InconsistentFieldValuesException(String message, String fieldName, String[] fieldValues, Throwable cause) {
        super(message, fieldName, fieldValues, cause);
    }

    public InconsistentFieldValuesException(String fieldName, String[] fieldValues, Throwable cause) {
        super(fieldName, fieldValues, cause);
    }

}