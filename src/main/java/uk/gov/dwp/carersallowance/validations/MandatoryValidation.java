package uk.gov.dwp.carersallowance.validations;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class MandatoryValidation extends AbstractValidation {
    public static MandatoryValidation INSTANCE = new MandatoryValidation();

    private MandatoryValidation() {
    }

    /**
     * validate that at least one value corresponding to fieldName is populated
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, String fieldName, Map<String, String[]> allFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, allFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});

        String[] fieldValues = allFieldValues.get(fieldName);
        if(fieldValues == null) {

            return false;
        }

        for(String fieldValue: fieldValues) {
            if(StringUtils.isBlank(fieldValue) == false) {
                return true;
            }
        }

        ValidationFactory.LOG.debug("missing mandatory field: {}", fieldName);
        failValidation(validationSummary, messageSource, fieldName);

        return false;
    }

    private void failValidation(ValidationSummary validationSummary, MessageSource messageSource, String fieldName) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName}, new String[]{"validationSummary", "messageSource", "fieldName"});

        Object args = null; // TODO need a way to configure message arguments, by referencing form values only (which i think we can) this will obviously need access to all the form values.
        String fieldTitle = getFieldLabel(messageSource, fieldName, args);
        String errorText = getErrorText(messageSource, ValidationType.MANDATORY);
        validationSummary.addFormError(fieldName, fieldTitle, errorText);
    }
}