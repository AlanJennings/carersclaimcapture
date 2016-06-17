package uk.gov.dwp.carersallowance.session;

public class NoSessionException extends IllegalStateException {
    private static final long serialVersionUID = -2504927384032834080L;

    public NoSessionException() {
        super();
    }

    public NoSessionException(String s) {
        super(s);
    }

    public NoSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSessionException(Throwable cause) {
        super(cause);
    }
}