package uk.gov.dwp.carersallowance.validations;

import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.session.InconsistentFieldValuesException;
import uk.gov.dwp.carersallowance.utils.Parameters;

public class DateValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(DateValidation.class);

    public static DateValidation INSTANCE = new DateValidation();

    private DateValidation() {
    }

    /**
     * Validate that all date fields are populated (field is mandatory) and they form a valid date
     * @param allfieldValues
     * @param id
     * @param fieldTitle
     */
    public boolean validate(ValidationSummary validationSummary, MessageSource messageSource, String id, Map<String, String[]> allFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, id, allFieldValues}, new String[]{"validationSummary", "messageSource", "id", "allFieldValues"});
        LOG.trace("Starting MandatoryValidation.validate");
        try {
            String dayFieldName = id + "_day";
            String monthFieldName = id + "_month";
            String yearFieldName = id + "_year";

            String day = getSingleValue(dayFieldName, allFieldValues.get(dayFieldName));
            String month = getSingleValue(monthFieldName, allFieldValues.get(monthFieldName));
            String year = getSingleValue(yearFieldName, allFieldValues.get(yearFieldName));

            boolean emptyField = false;
            boolean populatedField = false;

            String[] dateFields = new String[]{day, month, year};
            for(String dateField: dateFields) {
                LOG.debug("dateField = {}", dateField);
                if(StringUtils.isEmpty(dateField)) {
                    LOG.debug("missing mandatory field: {}", dateField);
                    emptyField = true;
                } else {
                    populatedField = true;
                }
            }

            if(emptyField) {
                LOG.debug("Empty field");
                if(populatedField) {
                    LOG.debug("also populated field");
                    failValidation(validationSummary, messageSource, id, ValidationType.DATE.getProperty());
                } else {
                    failValidation(validationSummary, messageSource, id, ValidationType.MANDATORY.getProperty());
                }

                LOG.debug("Return false, date is incomplete");
                return false;
            }

            if(isValidDate(day, month, year) == false) {
                LOG.debug("date is not valid, returning false");
                failValidation(validationSummary, messageSource, id, ValidationType.DATE.getProperty());
                return false;
            }

            LOG.debug("date is valid");
            return true;
        } finally {
            LOG.trace("Ending AbstractFormController.validateMandatoryDateField");
        }
    }

    private boolean isValidDate(String day, String month, String year) {
        Parameters.validateMandatoryArgs(new Object[]{day, month, year}, new String[]{"day", "month", "year"});

        try {
            int dayInt = parseDateField("day", day, 1, 31);
            int monthInt = parseDateField("month", month, 1, 12);
            int yearInt = parseDateField("year", year, 1000, 9999);

            Calendar calendar = Calendar.getInstance();
            calendar.setLenient(false);
            calendar.set(yearInt, monthInt, dayInt, 0, 0, 0);
            try {
                // lenient has no effect until we call getTime()
                calendar.getTime();
            } catch(IllegalArgumentException e) {
                LOG.debug("Invalid date argument: {}", e.getMessage());
                return false;
            }

            return true;

        } catch(NumberFormatException e) {
            return false;
        }

    }

    private int parseDateField(String fieldName, String fieldValue, int min, int max) {
        Parameters.validateMandatoryArgs(new Object[]{fieldName, fieldValue}, new String[]{"fieldName", "fieldValue"});
        try {
            int result = Integer.parseInt(fieldValue);
            if(result < min || result > max) {
                LOG.error("Invalid date field({}): {}, outside the valid range({} - {})", fieldName, fieldValue, min, max);
                throw new NumberFormatException("Invalid date field(" + fieldName + "): " + fieldValue + ", outside the valid range(" + min + " - " + max + ")");
            }
            return result;
        } catch(NumberFormatException e) {
            LOG.error("Invalid date field({}): {}, not an integer", fieldName, fieldValue);
            throw e;
        }
    }

    /**
     * @throws InconsistentFieldValuesException if not all the non-null values are the same
     */
    protected static String getSingleValue(String fieldName, String[] fieldValues)
            throws InconsistentFieldValuesException {

        if(fieldValues == null || fieldValues.length == 0) {
            return null;
        }

        String singleValue = null;
        for(String value: fieldValues) {
            if(value == null) {
                continue;
            }

            value = value.trim();
            if(singleValue == null) {
                singleValue = value;
            } else if(singleValue.equals(value) == false) {
                throw new InconsistentFieldValuesException(fieldName, fieldValues);
            }
        }

        return singleValue;
    }

    /**
     * true if case-insensitive 'true', otherwise false (uses {@link Boolean#parseBoolean(String)})
     * Note: condition is null safe trimmed before comparison
     */
    public static boolean isEnabled(String condition) {
        if(condition != null) {
            condition = condition.trim();
        }
        return Boolean.parseBoolean(condition);
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();

        System.out.println("calendar = " + calendar.getTime());

        System.out.println("first go");
        calendar.set(Calendar.MONTH, 20);
        System.out.println("calendar = " + calendar.getTime());

        calendar.setLenient(false);
        System.out.println("second go");
        calendar.set(Calendar.MONTH, 20);
        System.out.println("calendar = " + calendar.getTime());
    }
}