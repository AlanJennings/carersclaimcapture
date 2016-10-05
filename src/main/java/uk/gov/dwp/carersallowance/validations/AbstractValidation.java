package uk.gov.dwp.carersallowance.validations;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

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
            if(StringUtils.isEmpty(value) == false) {
                return false;
            }
        }
        return true;
    }

    protected String getErrorText(MessageSource messageSource, ValidationType type) {
        String errorTextKey = String.format(ERROR_TEXT_KEY_FORMAT, type.getProperty());
        return messageSource.getMessage(errorTextKey, null, Locale.getDefault());
    }
}
