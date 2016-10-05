package uk.gov.dwp.carersallowance.validations;

import java.util.Map;

import org.springframework.context.MessageSource;

public interface Validation {
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, String fieldName, Map<String, String[]> allFieldValues);
}