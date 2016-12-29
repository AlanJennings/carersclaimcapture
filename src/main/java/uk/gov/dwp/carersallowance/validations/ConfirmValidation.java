package uk.gov.dwp.carersallowance.validations;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.Parameters;

public class ConfirmValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(ConfirmValidation.class);

    private String comparisonField;

    public ConfirmValidation(String condition) {
        Parameters .validateMandatoryArgs(condition, "condition");
        this.comparisonField = condition;
    }

    /**
     * validate that every non-null value of the confirmation field matches every non-null value of the comparison field
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, TransformationManager transformationManager, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> existingFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, requestFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});
        LOG.trace("Starting ConfirmValidation.validate");
        try {
            LOG.debug("Comparing with {}", comparisonField);
            String comparisonValue = getFirstPopulatedValue(requestFieldValues.get(comparisonField));
            LOG.debug("comparison value = '{}'", comparisonValue);
            if(comparisonValue == null) {
                LOG.debug("Nothing to compare to, bailing");
                return true;
            }

            // only compare the values for this field
            String[] fieldValues = requestFieldValues.get(fieldName);
            LOG.debug("fieldValues  {}", fieldValues == null ? null : Arrays.asList(fieldValues));
            if(fieldValues != null) {
                for(String fieldValue: fieldValues) {
                    LOG.debug("Comparing '{}'", fieldValue);
                    if(StringUtils.isBlank(fieldValue) == false) {
                        if(comparisonValue.equals(fieldValue) == false) {
                            LOG.debug("field value({}) does not match comparison field value({})", fieldValue, comparisonValue);
                            failValidation(validationSummary, messageSource, fieldName, ValidationType.CONFIRM_FIELD.getProperty(), null, existingFieldValues);
                            return false;
                        }
                    }
                }
            }

            return true;
        } finally {
            LOG.trace("Ending ConfirmValidation.validate");
        }
    }
}