package uk.gov.dwp.carersallowance.validations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.Parameters;

public abstract class AbstractValidation implements Validation {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractValidation.class);

    private static final String ARG_SEPARATOR = "\\|";
    private static final String ERROR_TEXT_KEY_FORMAT = "%s.error_text";

    @Override
    public abstract boolean validate(ValidationSummary validationSummary, MessageSource messageSource, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> existingFieldValues);

    /**
     * @return the message or null if it does not exist
     */
    protected String getFieldLabel(MessageSource messageSource, String fieldName, String...args) {
        Locale locale  = null;
        String fieldTitle = messageSource.getMessage(fieldName + ".label", args, "{" + fieldName + ".label}", locale);
        return fieldTitle;
    }

    private String[] getFieldLabelArgs(MessageSource messageSource, String fieldName, Map<String, String[]> existingFieldValues) {
        try {
            Locale locale  = null;

            String fieldTitleArgs = messageSource.getMessage(fieldName + ".label.args", null, null, locale);

            if(StringUtils.isBlank(fieldTitleArgs)) {
                return null;
            }

            LOG.debug("fieldTitleArgs = {}", fieldTitleArgs);
            String[] args = fieldTitleArgs.split(ARG_SEPARATOR);
            String[] decodedArgs = new String[args.length];
            for(int index = 0; index < args.length; index++) {
                String arg = args[index].trim();
                LOG.debug("arg = {}", arg);
                if(StringUtils.isBlank(arg)) {
                    decodedArgs[index] = "";
                } else if(arg.startsWith("${") && arg.endsWith("}")) {
                    LOG.debug("extracting field value");
                    String argFieldName = arg.substring("${".length(), arg.length() - "}".length());
                    LOG.debug("argFieldName = {}", argFieldName);
                    String[] values = existingFieldValues.get(argFieldName);
                    if(values == null || values.length == 0) {
                        decodedArgs[index] = "";
                    } else if(values.length == 1) {
                        decodedArgs[index] = values[0];
                    } else {
                        LOG.error("Multiple field values not supported for: {}", fieldName);
                        throw new IllegalArgumentException("Multiple field values not supported");
                    }
                } else {
                    LOG.warn("field label argument({}) is not a fieldName, using literal value", arg);
                    decodedArgs[index] = arg;
                }
                LOG.debug("decodeArg = {}", decodedArgs[index]);    // TODO remove this later, leaks claim data
            }

            return decodedArgs;
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException: ", e);
            throw e;
        }
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

    protected void failValidation(ValidationSummary validationSummary,
                                  MessageSource messageSource,
                                  String fieldName,
                                  String errorKeyPrefix,
                                  Map<String, String[]> existingFieldValues) {

        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName}, new String[]{"validationSummary", "messageSource", "fieldName"});
        LOG.trace("Started AbstractValidation.failValidation");
        try {
            String[] args = getFieldLabelArgs(messageSource, fieldName, existingFieldValues);
            LOG.debug("fieldName = {}, args = {}", fieldName, args == null ? null : Arrays.asList(args));
            String fieldTitle = getFieldLabel(messageSource, fieldName, args);
            String errorText = getErrorText(messageSource, errorKeyPrefix);
            validationSummary.addFormError(fieldName, fieldTitle, errorText);
        } finally {
            LOG.trace("Ending AbstractValidation.failValidation");
        }
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

    public List<String> csvToList(String string) {
        if(string == null) {
            return null;
        }

        String[] strings = string.split(",");
        List<String> results = new ArrayList<>();
        for(String result : strings) {
            result = result.trim();
            if(StringUtils.isEmpty(result) == false) {
                results.add(result);
            }
        }

        return results;
    }
}
