package uk.gov.dwp.carersallowance.validations;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.Parameters;

public abstract class AbstractValidation implements Validation {
    private static final String ERROR_TEXT_KEY_FORMAT = "%s.error_text";

    @Override
    public abstract boolean validate(ValidationSummary validationSummary, MessageSource messageSource, String fieldName, Map<String, String[]> allFieldValues);

    /**
     * @return the message or null if it does not exist
     * TODO need a way to configure message arguments, by referencing form values only (which i think we can)
     *      this will obviously need access to all the form values.
     */
    protected String getFieldLabel(MessageSource messageSource, String fieldName, Object... args) {
        Locale locale  = null;
        String fieldTitle = messageSource.getMessage(fieldName + ".label", args, "{" + fieldName + ".label}", locale);
        return fieldTitle;
    }

    protected boolean isEmpty(String[] values) {
        if(values == null) {
            return true;
        }

        // None of them are populated, even if there is more than one
        for(String value: values) {
            if(value != null && value.trim().equals("") == false) {
                return false;
            }
        }
        return true;
    }

    protected void failValidation(ValidationSummary validationSummary, MessageSource messageSource, String fieldName, String errorKeyPrefix) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName}, new String[]{"validationSummary", "messageSource", "fieldName"});

        Object args = null; // TODO need a way to configure message arguments, by referencing form values only (which i think we can) this will obviously need access to all the form values.
        String fieldTitle = getFieldLabel(messageSource, fieldName, args);
        String errorText = getErrorText(messageSource, errorKeyPrefix);
        validationSummary.addFormError(fieldName, fieldTitle, errorText);
    }

    private String getErrorText(MessageSource messageSource, String errorType) {
        String errorTextKey = String.format(ERROR_TEXT_KEY_FORMAT, errorType);
        return messageSource.getMessage(errorTextKey, null, Locale.getDefault());
    }

    protected String getFirstPopulatedValue(String[] values) {
        if(values == null || values.length == 0) {
            return null;
        }

        for(String value: values) {
            if(value != null && value.trim().equals("") == false) {
                return value;
            }
        }

        return null;
    }
}
