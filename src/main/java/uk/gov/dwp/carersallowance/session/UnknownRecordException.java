package uk.gov.dwp.carersallowance.session;

public class UnknownRecordException extends IllegalArgumentException {
    private static final long serialVersionUID = -3332785078234893796L;

    public UnknownRecordException() {
        super();
    }

    public UnknownRecordException(String s) {
        super(s);
    }

    public UnknownRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownRecordException(Throwable cause) {
        super(cause);
    }
}