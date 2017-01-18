package uk.gov.dwp.carersallowance.validations;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;

import java.util.Map;

public class SortCodeValidation extends AbstractValidation {

    private static final Logger LOG = LoggerFactory.getLogger(SortCodeValidation.class);

    private final String SORT_CODE_REGEX = ValidationPatterns.SORT_CODE_REGEX;
    private final boolean mandatory;

    public SortCodeValidation(String mandatory) {
        this.mandatory = Boolean.getBoolean(mandatory);
    }

    /**
     * Checks whether the sortcode specified is a valid sort code (i.e. contains exactly six numbers. If so
     * returns true, otherwise returns false
     */
    @Override
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, TransformationManager transformationManager, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> existingFieldValues) {
        LOG.trace("Starting sort code validation...");
        boolean isValid;

        //  Get full sort code from three separate fields
        String sortcode;
        sortcode = getSingleValue(fieldName, requestFieldValues.get(fieldName + "_1"));
        sortcode += getSingleValue(fieldName, requestFieldValues.get(fieldName + "_2"));
        sortcode += getSingleValue(fieldName, requestFieldValues.get(fieldName + "_3"));

        // Check the sort code against the regex pattern
        isValid = sortcode.matches(SORT_CODE_REGEX);

        if (!isValid) {
            LOG.info(fieldName + " field failed sortcode validation");
            failValidation(validationSummary, messageSource, fieldName, ValidationType.SORT_CODE.getProperty(), null, existingFieldValues);
        }

        LOG.trace("Finished sort code validation - " + isValid);
        return isValid;
    }
}
