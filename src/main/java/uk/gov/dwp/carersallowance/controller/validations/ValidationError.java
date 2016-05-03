package uk.gov.dwp.carersallowance.controller.validations;

public interface ValidationError {
    public String getId();
    public String getErrorMessage();
}