package uk.gov.dwp.carersallowance.validations;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.Parameters;

public class MandatoryValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(MandatoryValidation.class);

    public static MandatoryValidation INSTANCE = new MandatoryValidation();

    private MandatoryValidation() {
    }

    /**
     * validate that at least one value corresponding to fieldName is populated
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, TransformationManager transformationManager, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> existingFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, requestFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});
        LOG.trace("Starting MandatoryValidation.validate");
        try {
            String[] fieldValues = requestFieldValues.get(fieldName);
            LOG.debug("fieldValues  {}", fieldValues == null ? null : Arrays.asList(fieldValues));
            if(fieldValues != null) {
                for(String fieldValue: fieldValues) {
                    if(fieldValue != null && fieldValue.trim().equals("") == false) {   // whitespace is not content
                        LOG.debug("populated field({}), returning", fieldValue);
                        return true;
                    }
                }
            }

            LOG.debug("missing mandatory field: {}", fieldName);
            failValidation(validationSummary, messageSource, fieldName, ValidationType.MANDATORY.getProperty(), null, existingFieldValues);

            return false;
        } finally {
            LOG.trace("Ending MandatoryValidation.validate");
        }
    }
}