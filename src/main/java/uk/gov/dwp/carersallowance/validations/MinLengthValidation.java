package uk.gov.dwp.carersallowance.validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.Parameters;

import java.util.Map;

public class MinLengthValidation extends AbstractValidation {

    private int minLength;
    private static final Logger LOG = LoggerFactory.getLogger(MinLengthValidation.class);
    public MinLengthValidation(String condition) {
        Parameters.validateMandatoryArgs(condition, "condition");
        try {
            minLength = Integer.parseInt(condition);
            if (minLength < 1) {
                throw new IllegalArgumentException("Minimum length cannot be less than one");
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("minLength must be a number");
        }
    }

    @Override
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, TransformationManager transformationManager, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> existingFieldValues) {

        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, requestFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});
        LOG.trace("Starting minlength validation");
        boolean isValid = true;

        try {
            LOG.debug("fieldname = {}", fieldName);
            String[] fieldValues = requestFieldValues.get(fieldName);
            for (String fieldValue : fieldValues) {
                if (fieldValue != null && fieldValue.length() < minLength) {
                    LOG.debug("field({}) value({}) is less than minimum length ({})", fieldName, fieldValue, minLength);
                    failValidation(validationSummary, messageSource, fieldName, ValidationType.MIN_LENGTH.getProperty(), null, existingFieldValues);
                    isValid = false;
                    break;
                }
            }
        }
        finally {
            LOG.trace("Ending minlength validation");
        }

        return isValid;
    }
}
