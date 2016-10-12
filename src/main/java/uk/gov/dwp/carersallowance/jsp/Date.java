package uk.gov.dwp.carersallowance.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class Date extends TagSupport {
    private static final long serialVersionUID = 6228061819142597868L;

    private final static Logger LOG = LoggerFactory.getLogger(Date.class);

    private String dateName;
    private String dateFormat;

    public void setDateName(String value) { dateName = Parameters.validateMandatoryArgs(value,  "dateName"); }
    public void setFormat(String value)   { dateFormat = Parameters.validateMandatoryArgs(value,  "dateFormat"); }

    @Override
    public int doStartTag() throws JspException {
        LOG.trace("Started Date.doStartTag");
        try {
            LOG.debug("dateName = {}, dateFormat = {}", dateName, dateFormat);

            // read the attributes, get the date values from the request and return them as a formatted date
            String dayField = getDateField(dateName + "_day");
            String monthField = getDateField(dateName + "_month");
            String yearField = getDateField(dateName + "_year");

            LOG.debug("dayField = {}, monthField = {}, yearField = {}", dayField, monthField, yearField);
            String dateString = Functions.dateOffset(dayField, monthField, yearField, dateFormat, null);

            LOG.debug("dateString = {}", dateString);

            try {
                pageContext.getOut().print(dateString);
            } catch(IOException | RuntimeException e) {
                throw new JspException("Problems formatting date(" + dateName + ") with format: " + dateFormat, e);
            }

            return SKIP_BODY;
        } finally {
            LOG.trace("Started Date.doStartTag");
        }
    }

    private String getDateField(String field) {
        return (String)pageContext.getSession().getAttribute(field);
    }
}
