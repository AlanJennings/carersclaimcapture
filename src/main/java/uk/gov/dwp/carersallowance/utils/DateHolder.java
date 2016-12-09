package uk.gov.dwp.carersallowance.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateHolder {
    private boolean now;
    private Date    date;

    public DateHolder(String value, String format) {
        if("now()".equalsIgnoreCase(value)) {
            now = true;
        } else {
            if(StringUtils.isBlank(format)) {
                throw new IllegalArgumentException("format is not populated.");
            }
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                date = formatter.parse(value);
            } catch(IllegalArgumentException e) {
                throw new IllegalArgumentException("format: " + format + " is not valid", e);
            } catch (ParseException e) {
                throw new IllegalArgumentException("value: " + value + " does not adhere to the specified format: " + format, e);
            }
        }
    }

    public boolean isNow() {
        return now;
    }

    public Date getActiveDate() {
        if(now) {
            return new Date();
        }
        return date;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("now = ").append(now);
        buffer.append(", date = ").append(date);
        buffer.append("]");

        return buffer.toString();
    }
}