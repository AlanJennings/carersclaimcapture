package uk.gov.dwp.carersallowance.validations;

import java.util.Map;

import org.springframework.context.MessageSource;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;

public interface Validation {
    public boolean validate(ValidationSummary validationSummary,
                            MessageSource messageSource,
                            TransformationManager transformationManager,
                            String fieldName,
                            Map<String, String[]> requestFieldValues,
                            Map<String, String[]> existingFieldValues);
}