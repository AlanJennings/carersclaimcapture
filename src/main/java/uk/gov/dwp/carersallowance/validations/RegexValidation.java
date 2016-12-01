package uk.gov.dwp.carersallowance.validations;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class RegexValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(RegexValidation.class);

    private String  regex;
    private Pattern pattern;

    public RegexValidation(String regex) {
        Parameters.validateMandatoryArgs(regex, "regex");

        this.regex = regex;
        pattern = Pattern.compile(regex) ;
    }

    /**
     * validate that at least one value corresponding to fieldName is populated
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> allFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, requestFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});
        LOG.trace("Starting MandatoryValidation.validate");
        try {
            String[] fieldValues = requestFieldValues.get(fieldName);
            LOG.debug("fieldValues  {}", fieldValues == null ? null : Arrays.asList(fieldValues));
            if(fieldValues != null) {
                for(String fieldValue: fieldValues) {
                    if(StringUtils.isBlank(fieldValue)) {
                        // we don't check null or empty strings
                        // if it needs to be mandatory, handle this with an additional Mandatory validation
                        continue;
                    }

                    Matcher matcher = pattern.matcher(fieldValue);
                    if(matcher.matches() == false) {
                        LOG.debug("field value({}) does not match regular expression: {}", fieldValue, regex);
                        failValidation(validationSummary, messageSource, fieldName, ValidationType.REGEX.getProperty(), allFieldValues);
                        return false;
                    }
                }
            }

            return true;
        } finally {
            LOG.trace("Ending MandatoryValidation.validate");
        }
    }
}