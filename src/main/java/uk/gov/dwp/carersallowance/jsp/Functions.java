package uk.gov.dwp.carersallowance.jsp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import uk.gov.dwp.carersallowance.encryption.FieldEncryptionServiceImpl;
import uk.gov.dwp.carersallowance.utils.Parameters;

import javax.inject.Inject;

@Component
public class Functions {
    private static final Logger LOG = LoggerFactory.getLogger(Functions.class);
    private static final String READ_DATE_FORMAT = "dd-MM-yyyy";
    private static transient MessageSource messageSource;
    private static transient FieldEncryptionServiceImpl fieldEncryptionService;

    @Inject
    private Functions(final MessageSource messageSource, final FieldEncryptionServiceImpl fieldEncryptionService) {
        this.messageSource = messageSource;
        this.fieldEncryptionService = fieldEncryptionService;
    }

    public static String dateOffset(String dayField, String monthField, String yearField, String format, String offset) {
        LOG.trace("Started Functions.dateOffset");
        try {
            LOG.debug("dayField = {}, monthField = {}, yearField = {}, format = {}, offset = {}", dayField, monthField, yearField, format, offset);
            if(StringUtils.isEmpty(format)) {
                LOG.debug("nothing to do: format is empty");
                return "";
            }

            if(StringUtils.isEmpty(dayField) && StringUtils.isEmpty(monthField) && StringUtils.isEmpty(yearField)) {
                LOG.debug("nothing to do: one or more of the date fields are empty");
                return "";
            }

            Parameters.validateMandatoryArgs(new Object[]{dayField, monthField, yearField, format},  new String[]{"dayField", "monthField", "yearField", "format"});
            String dateField = String.format("%s-%s-%s", pad(dayField, 2, '0'), pad(monthField, 2, '0'), pad(yearField, 4, '0'));
            LOG.debug("dateField = {}", dateField);
            SimpleDateFormat dateParser = new SimpleDateFormat(READ_DATE_FORMAT, Locale.getDefault());
            Date date;
            try {
                date = dateParser.parse(dateField);
            } catch (ParseException e) {
                throw new TagException("Unable to parse date field: '" + dateField + "' expected format: " + READ_DATE_FORMAT, e);
            }

            LOG.debug("date = {}", date);
            date = dateOffset(date, offset);

            SimpleDateFormat dateFormatter;
            try {
                dateFormatter = new SimpleDateFormat(format, Locale.getDefault());
            } catch(IllegalArgumentException e) {
                throw new TagException("Do not understand requested date format: '" + format + "'", e);
            }
            String result = dateFormatter.format(date);
            LOG.debug("result = {}", dateField);
            return returnDateMonthConvertedToLocale(result);
        } finally {
            LOG.trace("Ending Functions.dateOffset");
        }
    }

    public static String dateOffsetFromCurrent(String format, String offset) {
        LOG.trace("Started Functions.dateOffsetFromCurrent");
        Date date = dateOffset(Calendar.getInstance().getTime(), offset);
        LOG.debug("date = {}", date);
        try {
            SimpleDateFormat dateFormatter;
            try {
                dateFormatter = new SimpleDateFormat(format, Locale.getDefault());
            } catch(IllegalArgumentException e) {
                throw new TagException("Do not understand requested date format: '" + format + "'", e);
            }

            String result = dateFormatter.format(date);
            LOG.debug("result = {}", result);
            return returnDateMonthConvertedToLocale(result);
        } finally {
            LOG.trace("Ending Functions.dateOffsetFromCurrent");
        }
    }

    private static String returnDateMonthConvertedToLocale(final String result) {
        //get the month and get message
        //return prop(monthCode);
        final String[] dates = result.split(" ");
        if (dates == null || dates.length == 1) {
            return result;
        }

        String newMonth;
        if (dates[1].endsWith(",")) {
            newMonth = prop("datePlaceholder." + StringUtils.substringBefore(dates[1], ",")) + ",";
        } else {
            newMonth = prop("datePlaceholder." + dates[1]);
        }
        if (newMonth != null) {
            dates[1] = newMonth;
        }
        return String.join(" ", dates);
    }

    private static String pad(String string, int minSize, char paddingCharacter) {
        if(string == null) {
            return string;
        }

        if(minSize <= string.length()) {
            return string;
        }

        char[] chars = new char[minSize - string.length()];
        Arrays.fill(chars, paddingCharacter);
        String padding = new String(chars);

        return padding + string;
    }

    /**
     * Apply an offset to the date
     * The offset format is:
     *    [+|-]<integer amount><unit>
     *
     * where unit is one of:
     *   "minute"
     *   "minutes"
     *   "hour"
     *   "hours"
     *   "day"
     *   "days"
     *   "week"
     *   "weeks"
     *   "month"
     *   "months"
     *   "year"
     *   "years"
     */
    private static Date dateOffset(Date date, String offset) {
        LOG.trace("Started Functions.dateOffset");
        try {
            LOG.debug("date = {}, offset = {}", date, offset);
            if(StringUtils.isEmpty(offset) || date == null) {
                LOG.debug("nothing to do");
                return date;
            }

            String current = offset.trim();
            boolean positive = true;

            // sign
            if(current.startsWith("-")) {
                LOG.debug("negative");
                positive = false;
                current = current.substring(1);
            } else if(current.startsWith("+")) {
                current = current.substring(1);
            }

            // amount
            int endOfAmount = 0;
            while(endOfAmount < current.length()) {
                Character character = current.charAt(endOfAmount);
                if(Character.isDigit(character)) {
                    endOfAmount++;
                } else {
                    break;
                }
            }

            String amountStr = current.substring(0,  endOfAmount);
            LOG.debug("amountStr = '{}'", amountStr);
            int amount;
            try {
                amount = Integer.parseInt(amountStr);
            } catch(NumberFormatException e) {
                throw new TagException("Do not understand date offset amount: '" + amountStr + "'", e);
            }

            if(positive == false) {
                amount = -amount;
            }

            // units
            String unit = current.substring(endOfAmount);
            unit = unit.toLowerCase(Locale.getDefault());
            LOG.debug("unit = {}", unit);

            Date result = applyDateOffset(date, amount, unit);
            LOG.debug("result = {}", result);
            return result;
        } finally {
            LOG.trace("Ending Functions.dateOffset");
        }
    }

    private static Date applyDateOffset(Date date, int amount, String unit) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);
        switch(unit) {
            case "minute":
            case"minutes":
                calendar.add(Calendar.MINUTE, amount);
                break;
            case "hour":
            case "hours":
                calendar.add(Calendar.HOUR, amount);
                break;
            case "day":
            case "days":
                calendar.add(Calendar.DATE, amount);
                break;
            case "week":
            case "weeks":
                calendar.add(Calendar.DATE, amount * 7);
                break;
            case "month":
            case "months":
                calendar.add(Calendar.MONTH, amount);
                break;
            case "year":
            case "years":
                calendar.add(Calendar.YEAR, amount);
                break;
            default:
                throw new TagException("Do not undestand date offset unit: " + unit);
        }

        Date result = calendar.getTime();
        return result;
    }

    public static String prop(final String code, final String... args) {
        LOG.trace("Started Functions.prop");
        try {
            return messageSource.getMessage(code, args, null, Locale.getDefault());
        } finally {
            LOG.trace("Ending Functions.prop");
        }
    }

    public static String encrypt(final String value) {
        return fieldEncryptionService.encryptAES(value);
    }

    public static String decrypt(final String value) {
        return fieldEncryptionService.encryptAES(value);
    }
}