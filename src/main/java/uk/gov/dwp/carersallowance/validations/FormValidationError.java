package uk.gov.dwp.carersallowance.validations;

public class FormValidationError implements ValidationError {

    private String id;
    private String displayName;
    private String errorMessage;

    public FormValidationError(String id, String displayName, String errorMessage) {
        this.id = id;
        this.displayName = displayName;
        this.errorMessage = errorMessage;
    }

    public String getId()           { return id; }
    public String getDisplayName()  { return displayName; }
    public String getErrorMessage() { return errorMessage; }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("id = ").append(id);
        buffer.append(", displayName = ").append(displayName);
        buffer.append(", errorMessage = ").append(errorMessage);
        buffer.append("]");

        return buffer.toString();
    }
}