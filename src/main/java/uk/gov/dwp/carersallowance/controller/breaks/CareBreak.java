package uk.gov.dwp.carersallowance.controller.breaks;

import java.util.Date;

public class CareBreak {
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

    public CareBreak() {
        this(null);
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
}
