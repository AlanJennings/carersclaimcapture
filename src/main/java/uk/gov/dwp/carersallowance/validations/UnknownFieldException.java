package uk.gov.dwp.carersallowance.validations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnknownFieldException extends Exception {
    private static final long serialVersionUID = 4863487420470404482L;

    private List<String> fieldNames;

    public List<String> getFieldName() { return fieldNames; }

    public UnknownFieldException() {
        super();
    }

    public UnknownFieldException(String message) {
        super(message);
    }

    public UnknownFieldException(String message, List<String> fieldNames) {
        super(message);

        if(fieldNames == null) {
            this.fieldNames = new ArrayList<>();
        } else {
            this.fieldNames = fieldNames;
        }
    }

    public UnknownFieldException(String message, String...fieldNames) {
        this(message, fieldNames == null ? null : Arrays.asList(fieldNames));
    }

    public UnknownFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownFieldException(Throwable cause) {
        super(cause);
    }
}