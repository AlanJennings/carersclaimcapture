package uk.gov.dwp.carersallowance.validations;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.utils.PropertyUtils;

/**
 * @author David Hutchinson (drh@elegantsolutions.co.uk) on 4 Jan 2017.
 */
public class Dependencies {
    private static final Logger LOG = LoggerFactory.getLogger(Dependencies.class);

    private Map<String, Dependency> dependencies;

    public Dependencies(MessageSource messageSource, String dependencyKeyFormat, List<String> fields, List<String> referenceFields) throws ParseException {
        dependencies = initDependencies(messageSource, dependencyKeyFormat, fields, referenceFields);
    }

    private Map<String, Dependency> initDependencies(MessageSource messageSource, String dependencyKeyFormat, List<String> fields, List<String> referenceFields) throws ParseException {
        LOG.trace("Started FormValidations.initDependencies");
        try {
            // add dependencies
            // e.g. nameAndOrganisation.validation.dependency = thirdParty=no
            Map<String, Dependency> results = new HashMap<>();
            for(String field: fields) {
                String key = String.format(dependencyKeyFormat, field);                                 // e.g. nameAndOrganisation.validation.dependency
                String rawDependency = messageSource.getMessage(key, null, null, Locale.getDefault());  // e.g. thirdParty=no
                LOG.debug("{} = {}", key, rawDependency);
                if(rawDependency != null) {
                    rawDependency = PropertyUtils.trimQuotes(rawDependency);
                    LOG.info("Adding field({}) dependency {} = {}", field, key, rawDependency);
                    try {
                        String[] dependencyData = rawDependency.split("&");
                        for (final String dependencyData1 : dependencyData) {
                            Dependency dependency = Dependency.parseSingleLine(dependencyData1);
                            dependency.validateFieldNames(referenceFields);
                            Dependency existingDependency = results.get(field);
                            if (existingDependency != null) {
                                LOG.debug("Adding to existing field dependencies");
                                Dependency aggregateDependency = Dependency.AggregateDependency.aggregate(existingDependency, dependency);
                                results.put(field, aggregateDependency);
                            } else {
                                results.put(field, dependency);
                            }
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

    /**
     * Report whether all the dependencies for this field are fulfilled
     * and therefore all the validations are in force (e.g. the conditions
     * for an enclosing fold-out have been fulfilled)
     */
    public boolean areDependenciesFulfilled(String field, Map<String, String[]> fieldValues) {
        LOG.trace("Started FormValidations.areDependenciesFulfilled");
        try {
            Parameters.validateMandatoryArgs(field, "field");
            LOG.debug("Checking dependencies for field: '{}'", field);

            Dependency dependency = dependencies.get(field);
            if (dependency == null) {
                // this field has no dependencies, so the validations are all enabled
                LOG.debug("No dependencies for field, skipping");
                return true;
            }

            boolean fulfilled;
            Dependency.AggregateDependency aggregateDependency = null;
            if (dependency instanceof Dependency.AggregateDependency) {
                aggregateDependency = (Dependency.AggregateDependency)dependency;
                fulfilled = aggregateDependency.isFulfilled(fieldValues);
            } else {
                String[] values = fieldValues.get(dependency.getDependantField());
                LOG.debug("dependent field({}) required value = {}, actual value = {}", dependency.getDependantField(), dependency.getFieldValue(), values == null ? null : Arrays.asList(values));
                fulfilled = dependency.isFulfilled(values);
            }
            if (fulfilled == false) {
                LOG.debug("condition is not fulfilled");
                return false;
            }

            // make sure the dependent field is enabled.
            LOG.debug("Condition is met, checking parent dependencies");
            if (aggregateDependency != null) {
                return true;
            }
            return areDependenciesFulfilled(dependency.getDependantField(), fieldValues);
        } finally {
            LOG.trace("Ending FormValidations.areDependenciesFulfilled");
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("dependencies = ").append(dependencies);
        buffer.append("]");

        return buffer.toString();
    }
}