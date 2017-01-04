package uk.gov.dwp.carersallowance.validations;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.Parameters;

public class GroupAnyValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(GroupAnyValidation.class);

    private List<String> groupFields;

    public GroupAnyValidation(String condition) {
        Parameters .validateMandatoryArgs(condition, "condition");
        groupFields = csvToList(condition);
    }

    /**
     * validate that at least one of the fields is populated
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, TransformationManager transformationManager, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> existingFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, requestFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});
        LOG.trace("Starting GroupAnyValidation.validate");
        try {
            LOG.debug("groupFields = {}", groupFields);
            for(String field: groupFields) {
                String[] fieldValues = requestFieldValues.get(field);
                if(getFirstPopulatedValue(fieldValues) != null) {
                    LOG.debug("Found populated value for: {}", field);
                    return true;
                }
            }

            LOG.debug("No populated field for group: {}, fields = {}", fieldName, groupFields);
            failValidation(validationSummary, messageSource, fieldName, ValidationType.GROUP_ANY.getProperty(), null, existingFieldValues);
            return false;
        } finally {
            LOG.trace("Ending GroupAnyValidation.validate");
        }
    }
}