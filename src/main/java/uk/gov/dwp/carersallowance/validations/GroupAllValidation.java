package uk.gov.dwp.carersallowance.validations;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class GroupAllValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(GroupAllValidation.class);

    private List<String> groupFields;

    public GroupAllValidation(String condition) {
        Parameters .validateMandatoryArgs(condition, "condition");
        groupFields = csvToList(condition);
    }

    /**
     * validate that all the fields are populated
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> allFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, requestFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});
        LOG.trace("Starting GroupAllValidation.validate");
        try {
            LOG.debug("groupFields = {}", groupFields);
            for(String field: groupFields) {
                String[] fieldValues = requestFieldValues.get(field);
                if(getFirstPopulatedValue(fieldValues) == null) {
                    LOG.debug("Found empty value for: {}", field);
                    failValidation(validationSummary, messageSource, fieldName, ValidationType.GROUP_ALL.getProperty(), allFieldValues);
                    return false;
                }
            }

            LOG.debug("All fields populated for group: {}, fields = {}", fieldName, groupFields);
            return true;
        } finally {
            LOG.trace("Ending GroupAllValidation.validate");
        }
    }
}