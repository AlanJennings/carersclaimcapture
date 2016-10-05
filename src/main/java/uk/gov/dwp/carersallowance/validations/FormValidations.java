package uk.gov.dwp.carersallowance.validations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class FormValidations {
    private static final Logger LOG = LoggerFactory.getLogger(FormValidations.class);

    private static final String VALIDATION_DEPENDENCY_KEY_FORMAT = "%s.validation.dependency";
    private static final String DEFAULT_FIELD_NAMES_KEY_FORMAT   = "%s..fields";

    private String                        formName;
    private List<String>                  fields;
    private Map<String, Dependency>       dependencies;
    private Map<String, List<Validation>> validations;

    public FormValidations(MessageSource messageSource, String formName) throws ParseException {
        this(messageSource,
             formName,
             String.format(DEFAULT_FIELD_NAMES_KEY_FORMAT, Parameters.validateMandatoryArgs(formName, "formName")));
    }

    public FormValidations(MessageSource messageSource, String formName, String fieldNamesKey) throws ParseException {
        this(messageSource, formName, getFields(messageSource, fieldNamesKey));
    }

    public FormValidations(MessageSource messageSource, String formName, String[] fieldNames) throws ParseException {
        this(messageSource, formName, Arrays.asList(Parameters.validateMandatoryArgs(fieldNames, "fieldNames")));
    }

    public FormValidations(MessageSource messageSource, String formName, List<String> fieldNames) throws ParseException {
        Parameters.validateMandatoryArgs(new Object[]{messageSource, fieldNames}, new String[]{"messageSource", "fieldNames"});
        LOG.trace("Started FormValidations.FormValidations");
        try {
            this.formName = formName;

            fields = fieldNames;
            dependencies = initDependencies(messageSource, fields);
            validations = initValidations(messageSource, fields);

        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
        } finally {
            LOG.trace("Ending FormValidations.FormValidations");
        }
    }

    public String            getFormName() { return formName; }
    public List<String>      getFields()   { return fields; }

    private static String getMessage(MessageSource messageSource, String key) {
        return messageSource.getMessage(key, null, null, Locale.getDefault());
    }

    private static List<String> getFields(MessageSource messageSource, String fieldNamesKey) {
        Parameters.validateMandatoryArgs(new Object[]{messageSource, fieldNamesKey}, new String[]{"messageSource", "fieldNamesKey"});
        LOG.trace("Started FormValidations.getFields");
        try {
            String allFields = getMessage(messageSource, fieldNamesKey);
            if(allFields == null) {
                return null;
            }

            // get all the fields for this page and iterate over
            // the dependencies and also the validations
            String[] rawFields = allFields.split(",");
            List<String> results = new ArrayList<>();
            for(String field: rawFields) {
                results.add(field.trim());
            }

            return results;
        } finally {
            LOG.trace("Ending FormValidations.getFields");
        }
    }

    private Map<String, List<Validation>> initValidations(MessageSource messageSource, List<String> fields) {
        LOG.trace("Started FormValidations.initValidations");
        try {
            Map<String, List<Validation>> results = new HashMap<>();
            // add mandatory validations
            LOG.debug("Adding mandatory validations");
            Validation mandatoryValidation = ValidationFactory.getValidation(ValidationType.MANDATORY);
            for(String field: fields) {
                String key = String.format(ValidationType.MANDATORY.getKeyFormat(), field);    // e.g. nameAndOrganisation.validation.mandatory
                String value = getMessage(messageSource, key);
                LOG.debug("{} = {}", key, value);
                if(Boolean.parseBoolean(value)) {
                    List<Validation> fieldValidations = results.get(field);
                    if(fieldValidations == null) {
                        fieldValidations = new ArrayList<>();
                        results.put(field,  fieldValidations);
                    } else {
                        LOG.debug("Adding to existing validations: {}", fieldValidations);
                    }
                    LOG.info("Adding field({}) validation({})", field, mandatoryValidation);
                    fieldValidations.add(mandatoryValidation);
                }
            }

            return results;
        } finally {
            LOG.trace("Ending FormValidations.initValidations");
        }
    }

    private Map<String, Dependency> initDependencies(MessageSource messageSource, List<String> fields) throws ParseException {
        LOG.trace("Started FormValidations.initDependencies");
        try {
            // add dependencies
            // e.g. nameAndOrganisation.validation.dependency = thirdParty=no
            Map<String, Dependency> results = new HashMap<>();
            for(String field: fields) {
                String key = String.format(VALIDATION_DEPENDENCY_KEY_FORMAT, field);    // e.g. nameAndOrganisation.validation.dependency
                String rawDependency = getMessage(messageSource, key);                 // e.g. thirdParty=no
                LOG.debug("{} = {}", key, rawDependency);
                if(rawDependency != null) {
                    LOG.info("Adding field({}) dependency {} = {}", field, key, rawDependency);
                    try {
                        Dependency dependency = Dependency.parseSingleLine(rawDependency);
                        dependency.validateFieldNames(fields);
                        Dependency existingDependency = results.get(field);
                        if(existingDependency != null) {
                            LOG.debug("Adding to existing field dependencies");
                            Dependency aggregateDependency = Dependency.AggregateDependency.aggregate(existingDependency, dependency);
                            results.put(field, aggregateDependency);
                        } else {
                            results.put(field, dependency);
                        }
                    } catch (UnknownFieldException e) {
                        LOG.error("Invalid config: ", e);
                        throw new ParseException("Problems parsing dependency information(" + rawDependency + ") for " + key, 0);
                    }
                }
            }

            return results;
        } finally {
            LOG.trace("Ending FormValidations.initDependencies");
        }
    }

    public ValidationSummary validate(ValidationSummary validationSummary, MessageSource messageSource, Map<String, String[]> allFieldValues) {
        LOG.trace("Started FormValidations.validate");
        try{
            if(validationSummary == null) {
                validationSummary = new ValidationSummary();
            }

            for(String field: fields) {
                LOG.debug("validating {}", field);
                if(validations.containsKey(field) == false) {
                    LOG.debug("Skipping. No validations for {}", field);
                    continue;
                }

                if(areDependenciesFulfilled(field, allFieldValues) == false) {
                    LOG.debug("Skipping. Unfulfilled dependencies for field {}: it is not enabled", field);
                    continue;
                }

                List<Validation> fieldValidations = validations.get(field);
                LOG.info("fieldValidations: {}", fieldValidations);
                for(Validation validation: fieldValidations) {
                    validation.validate(validationSummary, messageSource, field, allFieldValues);
                }
            }

            return validationSummary;
        } finally {
            LOG.trace("Ending FormValidations.validate");
        }
    }

    /**
     * Report whether all the dependencies for this field are fulfilled
     * and therefore all the validations are in force (e.g. the conditions
     * for an enclosing fold-out have been fulfilled)
     */
    private boolean areDependenciesFulfilled(String field, Map<String, String[]> fieldValues) {
        Parameters.validateMandatoryArgs(field, "field");

        Dependency dependency = dependencies.get(field);
        if(dependency == null) {
            // this field has no dependencies, so the validations are all enabled
            return true;
        }

        String[] values = fieldValues.get(field);
        boolean fulfilled = dependency.isFulfilled(values);
        if(fulfilled == false) {
            return false;
        }

        // make sure the dependent field is enabled.
        return areDependenciesFulfilled(dependency.getDependantField(), fieldValues);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("formName = ").append(formName);
        buffer.append(", fields = ").append(fields);
        buffer.append(", dependencies = ").append(dependencies);
        buffer.append(", validations = ").append(validations);
        buffer.append("]");

        return buffer.toString();
    }
}