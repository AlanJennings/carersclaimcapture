package uk.gov.dwp.carersallowance.validations;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.DateHolder;
import uk.gov.dwp.carersallowance.utils.Parameters;

public class DateValidation extends AbstractValidation {
    private static final Logger LOG = LoggerFactory.getLogger(DateValidation.class);

    private static final String UPPERLIMIT_PARAM = "upperlimit";
    private static final String LOWERLIMIT_PARAM = "lowerlimit";
    private static final String MANDATORY_PARAM  = "mandatory";
    private static final String DATE_FORMAT      = "yyyy-MM-dd";

    private boolean    mandatory;
    private DateHolder upperLimit;
    private DateHolder lowerLimit;

    public DateValidation(String condition, Map<String, String> additionalParams) {
        this.mandatory = paramValueToBoolean(condition, MANDATORY_PARAM);

        if(additionalParams != null) {
            String lowerLimitStr = additionalParams.get(LOWERLIMIT_PARAM);
            if(StringUtils.isNotBlank(lowerLimitStr)) {
                lowerLimit = new DateHolder(lowerLimitStr, DATE_FORMAT);
            }

            String upperLimitStr = additionalParams.get(UPPERLIMIT_PARAM);
            if(StringUtils.isNotBlank(upperLimitStr)) {
                upperLimit = new DateHolder(upperLimitStr, DATE_FORMAT);
            }
        }
    }

    /**
     * Validate that all date fields are populated and they form a valid date
     * We handle mandatory validation here as it is multiple fields, so can't use the mandatory validation
     */
    public boolean validate(ValidationSummary validationSummary,
                            MessageSource messageSource,
                            TransformationManager transformationManager,
                            String id,
                            Map<String, String[]> requestFieldValues,
                            Map<String, String[]> existingFieldValues) {
        Parameters.validateMandatoryArgs(new Object[]{validationSummary, messageSource, id, requestFieldValues}, new String[]{"validationSummary", "messageSource", "id", "allFieldValues"});
        LOG.trace("Starting DateValidation.validate");
        try {
            String dayFieldName = id + "_day";
            String monthFieldName = id + "_month";
            String yearFieldName = id + "_year";

            String day = getSingleValue(dayFieldName, requestFieldValues.get(dayFieldName));
            String month = getSingleValue(monthFieldName, requestFieldValues.get(monthFieldName));
            String year = getSingleValue(yearFieldName, requestFieldValues.get(yearFieldName));

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
                    failValidation(validationSummary, messageSource, id, ValidationType.DATE.getProperty(), null, existingFieldValues);
                    LOG.debug("date is incomplete, returning false");
                    return false;

                } else if(mandatory) {
                    failValidation(validationSummary, messageSource, id, ValidationType.MANDATORY.getProperty(), null, existingFieldValues);
                    LOG.debug("date is mandatory, but empty, returning false");
                    return false;
                } else {
                    LOG.debug("skipping date validation");
                    return true;
                }

            } else {
                Date date = getValidDate(day, month, year);

                LOG.debug("date = {}, lowerLimit = {}, upperLimit = {}", date, lowerLimit, upperLimit);
                String condition;
                if(date == null) {
                    LOG.debug("date is not valid, returning false");
                    condition = null;
                } else if(upperLimit != null && date.after(upperLimit.getActiveDate())) {
                    LOG.debug("date({}) is beyond the upper limit: {}", date, upperLimit.getActiveDate());  // strictly speaking this will differ if its now(), but only by ns at the most
                    condition = ValidationType.DATE.getProperty() + "." + UPPERLIMIT_PARAM;
                } else if(upperLimit != null && date.before(lowerLimit.getActiveDate())) {
                    LOG.debug("date({}) is below the lower limit: {}", date, lowerLimit.getActiveDate());  // strictly speaking this will differ if its now(), but only by ns at the most
                    condition = ValidationType.DATE.getProperty() + "." + LOWERLIMIT_PARAM;
                } else {
                    LOG.debug("date is valid");
                    return true;
                }

                failValidation(validationSummary, messageSource, id, ValidationType.DATE.getProperty(), condition, existingFieldValues);
                return false;
            }

        } finally {
            LOG.trace("Ending DateValidation.DateValidation");
        }
    }

    private Date getValidDate(String day, String month, String year) {
        Parameters.validateMandatoryArgs(new Object[]{day, month, year}, new String[]{"day", "month", "year"});

        try {
            int dayInt = parseDateField("day", day, 1, 31);
            int monthInt = parseDateField("month", month, 1, 12);
            int yearInt = parseDateField("year", year, 1000, 9999);

            Calendar calendar = Calendar.getInstance();
            calendar.setLenient(false);
            calendar.set(yearInt, monthInt - 1, dayInt, 0, 0, 0);
            try {
                // lenient has no effect until we call getTime()
                Date date = calendar.getTime();
                return date;
            } catch(IllegalArgumentException e) {
                LOG.debug("Invalid date argument: {}", e.getMessage());
                return null;
            }

        } catch(NumberFormatException e) {
            LOG.debug("Unable to parse date fields", e);
            return null;
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

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("mandatory = ").append(mandatory);
        buffer.append(", lowerLimit = ").append(lowerLimit);
        buffer.append(", upperLimit = ").append(upperLimit);
        buffer.append("]");

        return buffer.toString();
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