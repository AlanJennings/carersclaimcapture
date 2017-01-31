package uk.gov.dwp.carersallowance.validations;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.jsp.Functions;
import uk.gov.dwp.carersallowance.jsp.ResolveArgs;
import uk.gov.dwp.carersallowance.session.InconsistentFieldValuesException;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.KeyValue;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.xml.ServerSideResolveArgs;

public abstract class AbstractValidation implements Validation {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractValidation.class);

    private static final String PARAM_SEPERATOR = "=";
    private static final String ARG_SEPARATOR = "\\|";
    private static final String ERROR_TEXT_KEY_FORMAT = "%s.error_text";
    protected static final String VALIDATION_TRANSFORMATION_KEY_FORMAT = "%s.validation.transformations";

    @Override
    public abstract boolean validate(ValidationSummary validationSummary, MessageSource messageSource, TransformationManager transformationManager, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> existingFieldValues);

    /**
     * @return the message or null if it does not exist
     */
    protected String getFieldLabel(MessageSource messageSource, String fieldName, String...args) {
        String fieldTitle = messageSource.getMessage(fieldName + ".label", args, "{" + fieldName + ".label}", Locale.getDefault());
        return fieldTitle;
    }

    private String[] getFieldLabelArgs(MessageSource messageSource, String fieldName, Map<String, String[]> existingFieldValues) {
        try {
            String fieldTitleArgs = messageSource.getMessage(fieldName + ".label.args", null, null, Locale.getDefault());

            if(StringUtils.isBlank(fieldTitleArgs)) {
                return null;
            }

            LOG.debug("fieldTitleArgs = {}", fieldTitleArgs);
            String[] args = fieldTitleArgs.split(ARG_SEPARATOR);
            String[] decodedArgs = new String[args.length];
            ServerSideResolveArgs serverSideResolveArgs = new ServerSideResolveArgs();
            for(int index = 0; index < args.length; index++) {
                String arg = args[index].trim();
                LOG.debug("arg = {}", arg);
                if(StringUtils.isBlank(arg)) {
                    decodedArgs[index] = "";
                } else if(arg.startsWith("${cads:") && arg.endsWith("}")) {
                    LOG.warn("field label argument({}) is not a fieldName, using literal value", arg);
                    decodedArgs[index] = (String)serverSideResolveArgs.evaluateExpressions(arg, convertToObjectMap(existingFieldValues)).get(0);
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

    private Map<String, Object> convertToObjectMap(Map<String, String[]> existingFieldValues) {
        Map<String, Object> converted = new HashMap<>();
        for (String existingKey : existingFieldValues.keySet()) {
            converted.put(existingKey, existingFieldValues.get(existingKey));
        }
        return converted;
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

    /**
     *
     * @param validationSummary
     * @param messageSource
     * @param fieldName           e.g. carerNationalInsuranceNumber
     * @param validationName      e.g. regex
     * @param validationCondition e.g. regex.pattern.nino
     * @param existingFieldValues
     */
    protected void failValidation(ValidationSummary validationSummary,
                                  MessageSource messageSource,
                                  String fieldName,
                                  String validationName,
                                  String validationCondition,
                                  Map<String, String[]> existingFieldValues) {

        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName}, new String[]{"validationSummary", "messageSource", "fieldName"});
        LOG.trace("Started AbstractValidation.failValidation");
        try {
            LOG.debug("fieldName = {}, validationName = {}, validationCondition = {}", fieldName, validationName, validationCondition);
            String[] args = getFieldLabelArgs(messageSource, fieldName, existingFieldValues);
            LOG.debug("fieldName = {}, args = {}", fieldName, args == null ? null : Arrays.asList(args));
            String fieldTitle = getFieldLabel(messageSource, fieldName, args);
            String fieldErrorKey = fieldName + ".validation." + validationName;                 // e.g. carerDateOfBirth.validation.date
            String fieldConditionErrorKey = fieldName + ".validation." + validationCondition;   // e.g. carerDateOfBirth.validation.date.upperlimit
            String errorText = getErrorText(messageSource, validationName, validationCondition, fieldErrorKey, fieldConditionErrorKey);

            validationSummary.addFormError(Functions.encrypt(fieldName), fieldTitle, errorText);
        } finally {
            LOG.trace("Ending AbstractValidation.failValidation");
        }
    }

    /**
     * @param errorType      e.g. regex
     * @param condition      e.g. regex.pattern.nino
     * @param fieldName      e.g. carerNationalInsuranceNumber.validation.regex
     * @param fieldCondition e.g. carerDateOfBirth.validation.date.upperlimit
     * @return
     */
    private String getErrorText(MessageSource messageSource, String errorType, String condition, String fieldName, String fieldCondition) {
        LOG.trace("Started AbstractValidation.getErrorText");
        try {
            LOG.debug("errorType = {}, condition = {}, fieldName = {}, fieldCondition = {}", errorType, condition, fieldName, fieldCondition);
            String[] errorPrefixes = new String[]{fieldCondition, fieldName, condition, errorType};
            for(String prefix : errorPrefixes) {
                String errorTextKey = String.format(ERROR_TEXT_KEY_FORMAT, StringUtils.defaultString(prefix));
                String message = messageSource.getMessage(errorTextKey, null, null, Locale.getDefault());
                LOG.debug("errorTextKey = {}, message = {}", errorTextKey, message);
                if(StringUtils.isNotBlank(message)) {
                    LOG.debug("using message");
                    return message;
                }
            }

            return "";
        } finally {
            LOG.trace("Ending AbstractValidation.getErrorText");
        }
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

    protected static boolean paramValueToBoolean(String param, String paramName) {
        String value = paramValueToString(param, paramName);
        return Boolean.parseBoolean(value);
    }

    /**
     *
     * @param param     e.g. mandatory=true
     * @param paramName e.g. mandatory
     * @return
     */
    protected static String paramValueToString(String param, String paramName) {
        Parameters.validateMandatoryArgs(paramName,  "paramName");

        KeyValue keyValue = new KeyValue(param, PARAM_SEPERATOR);
        if(paramName.equalsIgnoreCase(keyValue.getKey())) {
            return keyValue.getValue();
        }

        throw new IllegalArgumentException("Expected ParamName: " + paramName + ", but found: " + keyValue.getKey());
    }

    /**
     * @throws InconsistentFieldValuesException if not all the non-null values are the same
     */
    protected static String getSingleValue(String fieldName, String[] fieldValues) throws InconsistentFieldValuesException {

        if(fieldValues == null || fieldValues.length == 0) {
            return null;
        }

        String singleValue = null;
        for(String value: fieldValues) {
            if(value == null) {
                continue;
            }

            value = value.trim();
            if(singleValue == null) {
                singleValue = value;
            } else if(singleValue.equals(value) == false) {
                throw new InconsistentFieldValuesException(fieldName, fieldValues);
            }
        }

        return singleValue;
    }
}
