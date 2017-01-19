package uk.gov.dwp.carersallowance.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.CollectionUtils;
import uk.gov.dwp.carersallowance.utils.LoggingObjectWrapper;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.validations.ValidationSummary;

public class LegacyValidation {
    private static final Logger LOG = LoggerFactory.getLogger(LegacyValidation.class);

    /**
     * In priority order
     */
    private enum ValidationType {
        MANDATORY("mandatory"),
        MAX_LENGTH("maxLength"),
        DATE("date"),
        ADDRESS("address"),
        GROUP_ANY("group_any"),
        GROUP_ALL("group_all"),
        REGEX("regex"),
        CONFIRM_FIELD("confirm_field");

        private String property;

        private ValidationType(String property) {
            this.property = property;
        }

        public String getProperty() {
            return property;
        }
    };

    private MessageSource messageSource;
    private ValidationSummary validationSummary;

    public LegacyValidation(MessageSource messageSource, ValidationSummary validationSummary) {
        this.messageSource = messageSource;
        this.validationSummary = validationSummary;
    }

    public void addFormError(String id, String displayName, String errorMessage) {
        validationSummary.addFormError(id, displayName, errorMessage);
    }

    /**
     * Returns immediately after an error occurs (to avoid multiple errors for the same field)
     *
     * If javascript is in use then we can deduce the fields on screen and automate this but
     * but we need a journey that works without javascript.
     *
     * TODO not sure if this is the best approach or not (requires validation info in messages.properties)
     */
    public void validateField(Map<String, String[]> allfieldValues, String fieldName, Object...args) {

        for(LegacyValidation.ValidationType type: ValidationType.values()) {

            if(validationEnabled(fieldName, type) == false) {
                // support for optional fields
                continue;
            }

            switch(type) {
                case MANDATORY:
                    LOG.debug("field {} is mandatory, validating", fieldName);
                    validateMandatoryField(allfieldValues, fieldName, getFieldLabel(fieldName, args));
                    return;
                case MAX_LENGTH:
                    break;
                case DATE:
                    LOG.debug("field {} is mandatory, validating", fieldName);
                    validateMandatoryDateField(allfieldValues, fieldName, getFieldLabel(fieldName, args));
                    return;
                case ADDRESS:
                    break;
                case GROUP_ANY:
                    break;
                case GROUP_ALL:
                    break;
                case REGEX:
                    break;
                case CONFIRM_FIELD:
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized ValidationType: " + type);
            }
        }
    }

    /**
     * @return the message or null if it does not exist
     */
    private String getFieldLabel(String fieldName, Object... args) {
        Locale locale  = null;
        String fieldTitle = messageSource.getMessage(fieldName + ".label", args, "{" + fieldName + ".label}", Locale.getDefault());
        return fieldTitle;
    }

    /**
     * Validate that all date fields are populated (field is mandatory)
     * @param allfieldValues
     * @param id
     * @param args
     */
    public void validateMandatoryDateField(Map<String, String[]> allfieldValues, String id, Object... args) {
        LOG.trace("Started AbstractFormController.validateMandatoryDateField");
        Parameters.validateMandatoryArgs(new Object[]{allfieldValues, id}, new String[]{"allfieldValues", "id"});

        boolean emptyField = false;
        boolean populatedField = false;
        String[] dateFieldNames = new String[]{id + "_day", id + "_month", id + "_year"};
        for(String dateField: dateFieldNames) {
            String[] fieldValues = allfieldValues.get(dateField);
            LOG.debug("fieldName = {}, fieldValues={}", dateField, new LoggingObjectWrapper(fieldValues));
            if(AbstractFormController.isEmpty(fieldValues)) {
                LOG.debug("missing mandatory field: {}", dateField);
                emptyField = true;
            } else {
                populatedField = true;
            }
        }

        if(emptyField) {
            String fieldTitle = getFieldLabel(id, args);
            if(populatedField) {
                addFormError(id, fieldTitle, "Invalid value");
            } else {
                addFormError(id, fieldTitle, "You must complete this section");
            }
        }

        LOG.trace("Ending AbstractFormController.validateMandatoryDateField");
    }

    /**
     * @return false is the validation does not exist or is set to false/FALSE
     */
    private boolean validationEnabled(String fieldName, LegacyValidation.ValidationType type) {
        Locale locale  = null;
        String defaultValue = null;
        String validation = messageSource.getMessage(fieldName + ".validation." + type.getProperty(), null, defaultValue, Locale.getDefault());
        boolean enabled = Boolean.valueOf(validation);

        return enabled;
    }

    /**
     * TODO temporary, remove this when breaks & employment are fully converted
     */
    public void validateMandatoryField(Map<String, String[]> allfieldValues, String fieldName, Object...args) {
        LOG.trace("Started AbstractFormController.validateMandatoryField");
        Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldName}, new String[]{"allfieldValues", "fieldName"});

        String[] fieldValues = allfieldValues.get(fieldName);
        LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));

        if(AbstractFormController.isEmpty(fieldValues)) {
            LOG.debug("missing mandatory field: {}", fieldName);
            String fieldTitle = getFieldLabel(fieldName, args);
            addFormError(fieldName, fieldTitle, "You must complete this section");
        }

        LOG.trace("Ending AbstractFormController.validateMandatoryField");
    }

    public void validateMandatoryFieldGroupAllFields(Map<String, String[]> allfieldValues, String id, String fieldTitle, String...fieldNames) {
        LOG.trace("Started AbstractFormController.validateMandatoryField");
        Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldTitle}, new String[]{"allfieldValues", "fieldTitle"});

        LOG.debug("fieldNames = {}", new LoggingObjectWrapper(fieldNames));
        for(String fieldName: fieldNames) {
            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));

            if(AbstractFormController.isEmpty(fieldValues)) {
                LOG.debug("missing mandatory field: {}", fieldName);
                addFormError(id, fieldTitle, "You must complete this section");
                break;
            }
        }

        LOG.trace("Ending AbstractFormController.validateMandatoryField");
    }

    /**
     * @param allfieldValues if true all must be populated, if false at least one must be populated
     */
    public void validateMandatoryFieldGroupAnyField(Map<String, String[]> allfieldValues, String id, String fieldTitle, String...fieldNames) {
        LOG.trace("Started AbstractFormController.validateMandatoryField");
        Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldTitle}, new String[]{"allfieldValues", "fieldTitle"});

        LOG.debug("fieldNames = {}", new LoggingObjectWrapper(fieldNames));
        if(fieldNames == null || fieldNames.length == 0) {
            return;
        }

        for(String fieldName: fieldNames) {
            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));
            if(AbstractFormController.isEmpty(fieldValues) == false) {
                return;
            }
        }

        LOG.debug("missing mandatory field: {}", id);
        addFormError(id, fieldTitle, "You must complete this section");

        LOG.trace("Ending AbstractFormController.validateMandatoryField");
    }

    /**
     * At least two out of three address fields should be populated
     */
    public void validateAddressFields(Map<String, String[]> allfieldValues, String fieldTitle, String id, String[] addressFieldNames) {
        LOG.trace("Started AbstractFormController.validateMandatoryField");
        Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldTitle}, new String[]{"allfieldValues", "fieldTitle"});
        if(addressFieldNames == null || addressFieldNames.length == 0) {
            return;
        }

        if(addressFieldNames.length != 3) {
            throw new IllegalArgumentException("There must be three address fields");
        }

        int populated = 0;
        LOG.debug("addressFieldNames = {}", new LoggingObjectWrapper(addressFieldNames));
        for(String fieldName: addressFieldNames) {
            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));

            if(AbstractFormController.isEmpty(fieldValues) == false) {
                populated++;
            }
        }

        if(populated < 2) {
            LOG.debug("missing mandatory address field: {}", new LoggingObjectWrapper(addressFieldNames));
            addFormError(id, fieldTitle, "You must complete this section");
        }

        LOG.trace("Ending AbstractFormController.validateMandatoryField");
    }

    /**
     * Validate that two fields match (e.g. for email address, passwords etc)
     */
    public void validateMatchingValues(Map<String, String[]> allfieldValues, String firstTitle, String firstName, String secondTitle, String secondName, boolean caseInsensitve) {
        LOG.trace("Started AbstractFormController.validateMatchingValues");
        try {
            Parameters.validateMandatoryArgs(new Object[]{allfieldValues, firstTitle, firstName, secondTitle, secondName}, new String[]{"allfieldValues", "firstTitle", "firstName", "secondTitle", "secondName"});

            String[] firstFieldValues = allfieldValues.get(firstName);
            String[] secondFieldValues = allfieldValues.get(secondName);
            LOG.debug("firstName = {}, firstFieldValues={}", firstName, new LoggingObjectWrapper(firstFieldValues));
            LOG.debug("secondName = {}, secondFieldValues={}", firstName, new LoggingObjectWrapper(secondFieldValues));

            if(firstFieldValues == secondFieldValues) {
                return;
            }

            if(firstFieldValues != null && secondFieldValues != null) {
                Set<String> firstValuesSet = new HashSet<>(Arrays.asList(firstFieldValues));
                Set<String> secondValuesSet = new HashSet<>(Arrays.asList(secondFieldValues));

                if(caseInsensitve) {
                    firstValuesSet = CollectionUtils.toUpperCase(firstValuesSet);
                    secondValuesSet = CollectionUtils.toUpperCase(secondValuesSet);
                }
                if(firstValuesSet.equals(secondValuesSet)) {
                    return;
                }
            }

            LOG.debug("{} ({}) does not match {} ({}).", firstName, new LoggingObjectWrapper(firstFieldValues), secondName, new LoggingObjectWrapper(secondFieldValues));
            addFormError(secondName, secondTitle, "Your emails must match");

        } finally {
            LOG.trace("Started AbstractFormController.validateMatchingValues");
        }
    }

    /**
     * Validates non-blank values against the supplied regular expression.
     * DOES NOT CHECK BLANK VALUES
     */
    public void validateRegexField(Map<String, String[]> allfieldValues, String fieldTitle, String fieldName, String regex) {
        LOG.trace("Started AbstractFormController.validateRegexField");
        try {
            Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldTitle, fieldName, regex}, new String[]{"allfieldValues", "fieldTitle", "fieldName", "regex"});

            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));
            if(AbstractFormController.isEmpty(fieldValues)) {
                LOG.debug("nothing to do");
                return;
            }

            Pattern pattern = Pattern.compile(regex);   // this is re-usable
            for(String value: fieldValues) {
                Matcher matcher = pattern.matcher(value);
                if(matcher.matches() == false) {
                    LOG.debug("value: '{}' does not match regex: {}", value, regex);
                    addFormError(fieldName, fieldTitle, "Invalid value");
                    break;
                } else {
                    LOG.debug("value: '{}' matches regex: {}", value, regex);
                }
            }

        } catch(RuntimeException e) {
            LOG.error("Problems checking regular exception field: {}", fieldName);
            throw e;

        } finally {
            LOG.trace("Ending AbstractFormController.validateRegexField");
        }
    }

    /**
     * TODO -> c.f. Hamcrest Matchers
     * Return true if the value is populated and matches the value parameter.  Otherwise return false;
     *
     * @return false if there are no values for this fieldName, or all of them are blank
     * or the supplied value parameter does not match all the non-blank values (after trimming).
     * Otherwise return true;
     */
    public boolean fieldValue_Equals(Map<String, String[]> allfieldValues, String fieldName, String value) {
        LOG.trace("Started AbstractFormController.isValuePopulated");
        try {
            Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldName, value}, new String[]{"allfieldValues", "fieldName", "value"});

            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));
            if(AbstractFormController.isEmpty(fieldValues)) {
                return false;
            }

            boolean match = false;
            boolean mismatch = false;
            for(String fieldValue: fieldValues) {
                if(fieldValue == null || fieldValue.trim().equals("")) {
                    continue;
                }

                if(value.equals(fieldValue.trim())) {
                    match = true;
                } else  {
                    mismatch = true;
                }
            }

            if(mismatch) {
                return false;
            }
            return match;
        } finally {
            LOG.trace("Ending AbstractFormController.isValuePopulated");
        }
    }

    public boolean fieldValue_NotBlank(Map<String, String[]> allfieldValues, String fieldName) {
        return !fieldValue_Blank(allfieldValues, fieldName);
    }

    public boolean fieldValue_Blank(Map<String, String[]> allfieldValues, String fieldName) {
        LOG.trace("Started AbstractFormController.fieldValue_Blank");
        try {
            Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldName}, new String[]{"allfieldValues", "fieldName"});

            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));
            return AbstractFormController.isEmpty(fieldValues);

        } finally {
            LOG.trace("Ending AbstractFormController.fieldValue_Blank");
        }
    }
}