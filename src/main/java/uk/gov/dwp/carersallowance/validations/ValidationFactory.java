package uk.gov.dwp.carersallowance.validations;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

/**
 * TODO convert this to a spring service at some point (need to convert FormValidations first)
 */
public class ValidationFactory {
    private final static Logger LOG = LoggerFactory.getLogger(ValidationFactory.class);

    private MessageSource messageSource;

    public ValidationFactory(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * @param type                 - the validation type
     * @param key                  - the fully populated message bundle key e.g. nameAndOrganisation.validation.mandatory
     * @param condition            - the validation config, for simple types its just true/false, otherwise maxlength, regex pattern etc etc.
     * @param additionalParameters - config as key/value paramters that do not come from the value of the 'key' field (i.e. additional key.value pairs)
     * @return
     */
    public Validation getValidation(ValidationType type, String key, String condition, Map<String, String> additionalParameters) {
        LOG.debug("type = {}, condition = {}", type, condition);
        if(type == null || condition == null) {
            return null;
        }

        switch(type) {
            case MANDATORY:
                LOG.debug("Mandatory Validation");
                if(isValidationEnabled(condition)) {
                    return MandatoryValidation.INSTANCE;
                }
                break;

            case DATE:
                LOG.debug("Date Validation");
                return new DateValidation(condition, additionalParameters);

            case REGEX:
                LOG.debug("Regex Validation");
                return new RegexValidation(condition, cleanupConditionValue(condition));

            case ADDRESS:
                LOG.debug("Address Validation");
                return AddressValidation.valueOf(condition);

            case CONFIRM_FIELD:
                LOG.debug("Confirm Validation");
                return new ConfirmValidation(condition);

            case MAX_LENGTH:
                LOG.debug("MaxLength Validation");
                return new MaxLengthValidation(condition);

            case GROUP_ANY:
                LOG.debug("GroupAny Validation");
                return new GroupAnyValidation(condition);

            case GROUP_ALL:
                LOG.debug("Confirm Validation");
                return new GroupAllValidation(condition);

            default:
                throw new IllegalArgumentException("Unknown validation type: " + type);
        }

        return null;
    }

    /**
     * true if case-insensitive 'true', otherwise false (uses {@link Boolean#parseBoolean(String)})
     * Note: condition is null safe trimmed before comparison
     */
    private boolean isValidationEnabled(String condition) {
        if(condition != null) {
            condition = condition.trim();
        }
        return Boolean.parseBoolean(condition);
    }

    /**
     * Resolve references and remove enclosing quotes.
     */
    private String cleanupConditionValue(String code) {
        String referencedMessageValue = messageSource.getMessage(trimQuotes(code), null, null, Locale.getDefault());
        if(referencedMessageValue == null) {
            return code;
        }

        LOG.debug("referenced message value = '{}'", referencedMessageValue);
        return trimQuotes(cleanupConditionValue(referencedMessageValue));
    }

    private String trimQuotes(String string) {
        if(string == null) {
            return null;
        }

        string = string.trim();
        if(string.length() >= 2 && string.charAt(0) == '"' && string.charAt(string.length() -1) == '"') {
            LOG.debug("Trimming external quotes");
            return string.substring(1, string.length() -1);
        }

        return string;
    }

    public static void main(String[] args) {
        String[] values = {null, "", "\"\"", "\"hello", "hello\"", "hello \" world \"."};

        ValidationFactory factory = new ValidationFactory(null);
        for(String value: values) {
            System.out.print("'" + value + "'");
            System.out.println(" = '" + factory.trimQuotes(value) + "'");
        }
    }
}