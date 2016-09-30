package uk.gov.dwp.carersallowance.session;

public class MultipleValuesException extends IllegalStateException {
    private static final long serialVersionUID = -4246716347053857751L;

    public MultipleValuesException() {
        super();
    }

    public MultipleValuesException(String message) {
        super(message);
    }

    public MultipleValuesException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleValuesException(Throwable cause) {
        super(cause);
    }
}