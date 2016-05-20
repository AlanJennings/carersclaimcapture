package uk.gov.dwp.carersallowance.validations;

public interface ValidationError {
    public String getId();
    public String getDisplayName();
    public String getErrorMessage();
}