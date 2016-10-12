package uk.gov.dwp.carersallowance.jsp;

public class TagException extends RuntimeException {
    private static final long serialVersionUID = 6776036081694457928L;

    public TagException() {
        super();
    }

    public TagException(String message) {
        super(message);
    }

    public TagException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagException(Throwable cause) {
        super(cause);
    }
}