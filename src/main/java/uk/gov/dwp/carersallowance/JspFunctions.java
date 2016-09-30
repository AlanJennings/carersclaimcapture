package uk.gov.dwp.carersallowance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.util.StringUtils;

import uk.gov.dwp.carersallowance.utils.Parameters;

// TODO remove these and re-analyze
public class JspFunctions {
    private static final String READ_DATE_FORMAT = "dd-MM-yyyy";

    private JspFunctions() {}

    public static String dateOffset(String dayField, String monthField, String yearField, String format, String offset) {
        if(StringUtils.isEmpty(format)) {
            return "";
        }

        if(StringUtils.isEmpty(dayField) && StringUtils.isEmpty(monthField) && StringUtils.isEmpty(yearField)) {
            return "";
        }

        Parameters.validateMandatoryArgs(new Object[]{dayField, monthField, yearField, format},  new String[]{"dayField", "monthField", "yearField", "format"});
        String dateField = String.format("%s-%s-%s", pad(dayField, 2, '0'), pad(monthField, 2, '0'), pad(yearField, 4, '0'));
        SimpleDateFormat dateParser = new SimpleDateFormat(READ_DATE_FORMAT, Locale.getDefault());
        Date date;
        try {
            date = dateParser.parse(dateField);
        } catch (ParseException e) {
            throw new TagException("Unable to parse date field: '" + dateField + "' expected format: " + READ_DATE_FORMAT, e);
        }

        date = dateOffset(date, offset);

        SimpleDateFormat dateFomatter;
        try {
            dateFomatter = new SimpleDateFormat(format, Locale.getDefault());
        } catch(IllegalArgumentException e) {
            throw new TagException("Do not understand requested date format: '" + format + "'", e);
        }
        String result = dateFomatter.format(date);
        return result;
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
        if(StringUtils.isEmpty(offset) || date == null) {
            return date;
        }

        String current = offset.trim();
        boolean positive = true;

        // sign
        if(current.startsWith("-")) {
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
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch(NumberFormatException e) {
            throw new TagException("Do not undestand date offset amount: " + amountStr, e);
        }

        if(positive == false) {
            amount = -amount;
        }

        // units
        String unit = current.substring(endOfAmount);
        unit = unit.toLowerCase(Locale.getDefault());

        Date result = applyDateOffset(date, amount, unit);
        return result;
    }

    private static Date applyDateOffset(Date date, int amount, String unit) {
        Calendar calendar = Calendar.getInstance();
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

    public static class TagException extends RuntimeException {
        private static final long serialVersionUID = 6776036081694457928L;

        public TagException() {
            super();
        }

        public TagException(String message) {
            super(message);
        }

        public TagException(String message, Throwable cause) {
            super(message, cause);
        }

        public TagException(Throwable cause) {
            super(cause);
        }
    }
}