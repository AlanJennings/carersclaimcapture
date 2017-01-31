package uk.gov.dwp.carersallowance.validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.Parameters;

import java.util.List;
import java.util.Map;

public class GroupAnyOrValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(GroupAnyOrValidation.class);

    private List<String> groupAnyFields;
    private List<String> groupOrFields;

    public GroupAnyOrValidation(String condition, Map<String, String> additionalParameter) {
        Parameters.validateMandatoryArgs(condition, "condition");
        groupAnyFields = csvToList(condition);
        groupOrFields = csvToList(additionalParameter.get("or"));
    }

    /**
     * validate that at least one of the fields is populated
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, TransformationManager transformationManager, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> existingFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, requestFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});
        LOG.trace("Starting GroupAnyValidation.validate");
        try {
            LOG.debug("groupAnyFields = {}, groupOrFields = {}", groupAnyFields, groupOrFields);
            Boolean anyPopulated = isPopulated(groupAnyFields, requestFieldValues);
            Boolean orPopulated = isPopulated(groupOrFields, requestFieldValues);

            if ((orPopulated && anyPopulated == Boolean.FALSE) || (orPopulated == Boolean.FALSE && anyPopulated)) {
                return Boolean.TRUE;
            }
            LOG.debug("No populated field for groupAny: {} or groupOr: {}, fields = {}", fieldName, groupAnyFields, groupOrFields);
            failValidation(validationSummary, messageSource, fieldName, ValidationType.GROUP_ANY_OR.getProperty(), null, existingFieldValues);
            return false;
        } finally {
            LOG.trace("Ending GroupAnyValidation.validate");
        }
    }

    private Boolean isPopulated(List<String> group, Map<String, String[]> requestFieldValues) {
        Boolean populated = Boolean.FALSE;
        for (final String field: group) {
            String[] fieldValues = requestFieldValues.get(field);
            if (getFirstPopulatedValue(fieldValues) != null) {
                LOG.debug("Found populated value for: {}", field);
                populated = Boolean.TRUE;
                break;
            }
        }
        return populated;
    }
}