package uk.gov.dwp.carersallowance.validations;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class AddressValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(AddressValidation.class);

    public static AddressValidation INSTANCE = new AddressValidation();

    private AddressValidation() {
    }

    /**
     * validate that at least two address rows are populated
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, String fieldName, Map<String, String[]> allFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, allFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});
        LOG.trace("Starting AddressValidation.validate");
        try {
            String[] addressFields = new String[]{fieldName + "LineOne", fieldName + "LineTwo", fieldName + "LineThree"};
            LOG.debug("addressFields = {}", Arrays.asList(addressFields));

            int count = 0;
            for(String addressField: addressFields) {
                if(isEmpty(allFieldValues.get(addressField)) == false) {
                    LOG.debug("Address field({}) is populated", addressField);
                    count++;
                }
            }

            if(count >= 2) {
                LOG.debug("At least two lines({}) of the address are populated", count);
                return true;
            }

            LOG.debug("insufficient address fields: {}", fieldName);
            failValidation(validationSummary, messageSource, fieldName, ValidationType.ADDRESS_MANDATORY.getProperty());

            return false;
        } finally {
            LOG.trace("Ending AddressValidation.validate");
        }
    }
}