package uk.gov.dwp.carersallowance.validations;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class MaxLengthValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(MaxLengthValidation.class);

    private int maxLength;

    public MaxLengthValidation(String condition) {
        Parameters.validateMandatoryArgs(condition, "condition");
        try {
            maxLength = Integer.parseInt(condition);
            if(maxLength < 1) {
                throw new IllegalArgumentException("maxLength cannot be less than 1");
            }
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("maxLength(" + condition + ") must be an integer");
        }
    }

    /**
     * validate the field is no more than maxLength
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> allFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, requestFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});
        LOG.trace("Starting GroupAllValidation.validate");
        try {
            LOG.debug("fieldName = {}", fieldName);
            String[] fieldValues = requestFieldValues.get(fieldName);
            for(String fieldValue : fieldValues) {
                if(fieldValue != null && fieldValue.length() > maxLength) {
                    LOG.debug("field({}) value({}) is longer than maxLength: {}", fieldName, fieldValue, maxLength);
                    failValidation(validationSummary, messageSource, fieldName, ValidationType.MAX_LENGTH.getProperty(), allFieldValues);
                    return false;
                }
            }

            return true;
        } finally {
            LOG.trace("Ending GroupAllValidation.validate");
        }
    }
}