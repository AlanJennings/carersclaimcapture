package uk.gov.dwp.carersallowance.validations;

import java.util.Locale;

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

    public Validation getValidation(ValidationType type, String condition) {
        LOG.debug("type = {}, condition = {}", type, condition);
        if(type == null || condition == null) {
            return null;
        }

        switch(type) {
            case MANDATORY:
                LOG.debug("Mandatory Validation");
                if(MandatoryValidation.isEnabled(condition)) {
                    return MandatoryValidation.INSTANCE;
                }
                break;

            case DATE:
                LOG.debug("Date Validation");
                if(DateValidation.isEnabled(condition)) {
                    return DateValidation.INSTANCE;
                }
                break;
            case REGEX:
                LOG.debug("Regex Validation");
                return new RegexValidation(cleanupConditionValue(condition));

            case ADDRESS:
                LOG.debug("Address Validation");
                if(AddressValidation.isEnabled(condition)) {
                    return AddressValidation.INSTANCE;
                }
                break;

            case CONFIRM_FIELD:
                LOG.debug("Confirm Validation");
                return new ConfirmValidation(condition);

            case MAX_LENGTH:
            case GROUP_ANY:
            case GROUP_ALL:
                LOG.error("Unsupported type: " + type);
                break;

            default:
                throw new IllegalArgumentException("Unknown validation type: " + type);
        }

        return null;
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