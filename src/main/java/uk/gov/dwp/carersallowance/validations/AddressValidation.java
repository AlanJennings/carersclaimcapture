package uk.gov.dwp.carersallowance.validations;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class AddressValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(AddressValidation.class);

    public static AddressValidation MANDATORY_INSTANCE = new AddressValidation(true);
    public static AddressValidation OPTIONAL_INSTANCE  = new AddressValidation(false);

    private static final String MANDATORY_PARAM = "mandatory";

    private boolean mandatory;

    private AddressValidation(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public static AddressValidation valueOf(String mandatoryParam) {
        boolean mandatory = paramValueToBoolean(mandatoryParam, MANDATORY_PARAM);
        if(mandatory) {
            return MANDATORY_INSTANCE;
        } else {
            return OPTIONAL_INSTANCE;
        }
    }

    /**
     * validate that at least two address rows are populated
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, String fieldName, Map<String, String[]> requestFieldValues, Map<String, String[]> existingFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, fieldName, requestFieldValues}, new String[]{"validationSummary", "messageSource", "fieldName", "allFieldValues"});
        LOG.trace("Starting AddressValidation.validate");
        try {
            String[] addressFields = new String[]{fieldName + "LineOne", fieldName + "LineTwo", fieldName + "LineThree"};
            LOG.debug("addressFields = {}", Arrays.asList(addressFields));

            int count = 0;
            for(String addressField: addressFields) {
                if(isEmpty(requestFieldValues.get(addressField)) == false) {
                    LOG.debug("Address field({}) is populated", addressField);
                    count++;
                }
            }

            if(mandatory == false && count == 0) {
                return true;
            }

            if(count >= 2) {
                LOG.debug("At least two lines({}) of the address are populated", count);
                return true;
            }

            LOG.debug("insufficient address fields: {}", fieldName);
            failValidation(validationSummary, messageSource, fieldName, ValidationType.ADDRESS.getProperty(), null, existingFieldValues);

            return false;
        } finally {
            LOG.trace("Ending AddressValidation.validate");
        }
    }
}