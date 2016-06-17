package uk.gov.dwp.carersallowance.controller.breaks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

public class CareBreak {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm";

    public static enum WHERE_CAREE {
            HOSPITAL("In hospital", "in hospital"),
            RESPITE("In respite care", "in respite care"),
            HOLIDAY("On holiday", "on holiday"),
            HOME("At home", "at home"),
            OTHER("Somewhere else", "somewhere else");

        private String id;
        private String label;
        private WHERE_CAREE(String id, String label) {
             this.id = id;
             this.label = label;
        }

        public String id() {
            return id;
        }

        public String label() {
            return label;
        }

        public WHERE_CAREE valueOfId(String value) {
            for(WHERE_CAREE instance: WHERE_CAREE.values()) {
                if(instance.id.equalsIgnoreCase(value)) {
                    return instance;
                }
            }

            throw new IllegalArgumentException("Unknown id: " + value + ", should be one of " + WHERE_CAREE.values());
        }
    };

    public static enum WHERE_YOU {
            HOSPITAL("In hospital", "in hospital"),
            HOLIDAY("On holiday", "on holiday"),
            HOME("At home", "at home"),
            OTHER("Somewhere else", "somewhere else");

            private String id;
            private String label;

            private WHERE_YOU(String id, String label) {
             this.id = id;
             this.label = label;
        }

        public String id() {
            return id;
        }

        public String label() {
            return label;
        }

        public WHERE_YOU valueOfId(String value) {
            for(WHERE_YOU instance: WHERE_YOU.values()) {
                if(instance.id.equalsIgnoreCase(value)) {
                    return instance;
                }
            }

            throw new IllegalArgumentException("Unknown id: " + value + ", should be one of " + WHERE_YOU.values());
        }
    };

    private String id;
    private Date   startDate;
    private String startTime;
    private String whereCaree;
    private String whereCareeOtherText;
    private String whereYou;
    private String whereYouOtherText;
    private String hasBreakEnded;
    private Date   breakEndedDate;
    private String breakEndedTime;
    private String medicalCareDuringBreak;

    public CareBreak(int id,
                     Date   startDate,
                     String startTime,
                     String whereCaree,
                     String whereCareeOtherText,
                     String whereYou,
                     String whereYouOtherText,
                     String hasBreakEnded,
                     Date   breakEndedDate,
                     String breakEndedTime,
                     String medicalCareDuringBreak) {
        this(id);

        this.startDate = startDate;
        this.startTime = startTime;
        this.whereCaree = whereCaree;
        this.whereCareeOtherText = whereCareeOtherText;
        this.whereYou = whereYou;
        this.whereYouOtherText = whereYouOtherText;
        this.hasBreakEnded = hasBreakEnded;
        this.breakEndedDate = breakEndedDate;
        this.breakEndedTime = breakEndedTime;
        this.medicalCareDuringBreak = medicalCareDuringBreak;
    }

    public CareBreak() {
        this(null);
    }

    public CareBreak(int id) {
        this(Integer.toString(id));
    }

    public CareBreak(String id) {
        if(id == null) {
            this.id = "-1";
        } else {
            this.id = id;
        }
    }

    public String getId()                       { return id; }
    public Date   getStartDate()                { return startDate; }
    public String getStartTime()                { return startTime; }
    public String getWhereCaree()               { return whereCaree; }
    public String getWhereCareeOtherText()      { return whereCareeOtherText; }
    public String getWhereYou()                 { return whereYou; }
    public String getWhereYouOtherText()        { return whereYouOtherText; }
    public String getHasBreakEnded()            { return hasBreakEnded; }
    public Date   getBreakEndedDate()           { return startDate; }
    public String getBreakEndedTime()           { return breakEndedTime; }
    public String getMedicalCareDuringBreak()   { return medicalCareDuringBreak; }

    public void setId(String value)                       { id = value; }
    public void setStartDate(Date value)                  { startDate = value; }
    public void setStartTime(String value)                { startTime = value; }
    public void setWhereCaree(String value)               { whereCaree = value; }
    public void setWhereCareeOtherText(String value)      { whereCareeOtherText = value; }
    public void setWhereYou(String value)                 { whereYou = value; }
    public void setWhereYouOtherText(String value)        { whereYouOtherText = value; }
    public void setHasBreakEnded(String value)            { hasBreakEnded = value; }
    public void setBreakEndedDate(Date value)             { startDate = value; }
    public void setBreakEndedTime(String value)           { breakEndedTime = value; }
    public void setMedicalCareDuringBreak(String value)   { medicalCareDuringBreak = value; }

    public static void setDate(String day, String month, String year) {
        throw new UnsupportedOperationException("setDate");
    }

    public static Date toDate(String value) { return toDate(value, DATE_FORMAT); }
    public static Date toTime(String value) { return toDate(value, TIME_FORMAT); }

    private static Date toDate(String value, String format) {
        if(StringUtils.isEmpty(value)) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(value);
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unrecognised date format, expecting date in format: " + format);
        }

    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        toMap(map, "id", id);
        toMap(map, "startDate", startDate);
        toMap(map, "startTime", startTime);
        toMap(map, "whereCaree", whereCaree);
        toMap(map, "whereCareeOtherText", whereCareeOtherText);
        toMap(map, "whereYou", whereYou);
        toMap(map, "whereYouOtherText", whereYouOtherText);
        toMap(map, "hasBreakEnded", hasBreakEnded);
        toMap(map, "breakEndedDate", breakEndedDate);
        toMap(map, "breakEndedTime", breakEndedTime);
        toMap(map, "medicalCareDuringBreak", medicalCareDuringBreak);

        return map;
    }

    private Map<String, String> toMap(Map<String, String> map, String field, String fieldValue) {
//        String fieldName = root + "_" + index + "_" + field;
        String fieldName = field;
        map.put(fieldName, fieldValue);

        return map;
    }

    private Map<String, String> toMap(Map<String, String> map, String field, Date value) {
//        String fieldName = root + "_" + index + "_" + field;
        String fieldName = field;
        String year = "";
        String month = "";
        String day = "";
        if(value != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(value);

            year = String.format("%04d", calendar.get(Calendar.YEAR));
            month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
            day = String.format("%02d", calendar.get(Calendar.DATE));
        }
        map.put(fieldName + "_year", year);
        map.put(fieldName + "_month", month);
        map.put(fieldName + "_day", day);

        return map;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("id = ").append(id);
        buffer.append("startDate = ").append(startDate);
        buffer.append("startTime = ").append(startTime);
        buffer.append("whereCaree = ").append(whereCaree);
        buffer.append("whereCareeOtherText = ").append(whereCareeOtherText);
        buffer.append("whereYou = ").append(whereYou);
        buffer.append("whereYouOtherText = ").append(whereYouOtherText);
        buffer.append("hasBreakEnded = ").append(hasBreakEnded);
        buffer.append("breakEndedDate = ").append(breakEndedDate);
        buffer.append("breakEndedTime = ").append(breakEndedTime);
        buffer.append("medicalCareDuringBreak = ").append(medicalCareDuringBreak);
        buffer.append("]");

        return buffer.toString();
    }

    public static void main(String[] args) {
        String[] dates = {"01/01/2009", "01/06/2016", "12/12/2017"};
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        CareBreak careBreak = new CareBreak();
        for(String dateValue: dates) {
            Map<String, String> map = new HashMap<>();
            try {
                Date date = formatter.parse(dateValue);
                careBreak.toMap(map, "start_date", date);
                System.out.println("'" + dateValue + "' = " + map);
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
