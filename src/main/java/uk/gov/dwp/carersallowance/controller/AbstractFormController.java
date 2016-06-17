package uk.gov.dwp.carersallowance.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.gov.dwp.carersallowance.session.IllegalFieldValueException;
import uk.gov.dwp.carersallowance.session.InconsistentFieldValuesException;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.session.SessionManager.Session;
import uk.gov.dwp.carersallowance.utils.CollectionUtils;
import uk.gov.dwp.carersallowance.utils.LoggingObjectWrapper;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.validations.FormValidationError;
import uk.gov.dwp.carersallowance.validations.ValidationError;

public abstract class AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFormController.class);

    private static final String[] PAGES = {
            "/allowance/benefits",
            "/allowance/eligibility",
            "/allowance/approve",
            "/disclaimer/disclaimer",
            "/third-party/third-party",
            "/your-claim-date/claim-date",
            "/about-you/your-details",
            "/about-you/marital-status",
            "/about-you/contact-details",
            "/about-you/nationality-and-residency",
            "/about-you/other-eea-state-or-switzerland",
            "/your-partner/personal-details",
            "/care-you-provide/their-personal-details",
            "/care-you-provide/more-about-the-care",
            "/breaks/breaks-in-care",
            "/education/your-course-details",
            "/your-income/your-income",
            "/your-income/employment/been-employed",
            "/your-income/self-employment/self-employment-dates",
            "/your-income/self-employment/pensions-and-expenses",
            "/your-income/employment/additional-info",
            "/your-income/statutory-sick-pay",
            "/your-income/smp-spa-sap",
            "/your-income/fostering-allowance",
            "/your-income/direct-payment",
            "/your-income/other-income",
            "/pay-details/how-we-pay-you",
            "/information/additional-info",
            "/preview",
            "/consent-and-declaration/declaration",
            "/thankyou/apply-carers"
        };

    public static final String   PHONE_REGEX = "[ 0123456789]{0,20}";       // probably convert to an enum
    public static final String   EMAIL_REGEX = "[ 0123456789]{0,20}";       // probably convert to an enum

    private SessionManager    sessionManager;
    private ValidationSummary validationSummary;
    private List<String>      pageList;

    public AbstractFormController(SessionManager sessionManager) {
        validationSummary = new ValidationSummary();
        pageList = new ArrayList<>(Arrays.asList(PAGES));
    }

    /************************ START ABSTRACT METHODS **************************/

    public abstract String   getCurrentPage();           // e.g. /allowance/benefits
    public abstract String   getPageTitle();
    public abstract String[] getFields();

    protected abstract void validate(Map<String, String[]> fieldValues, String[] fields);

    /*********************** END ABSTRACT METHODS ******************************/

    public Session           getSession(String sessionId)   { return sessionManager.getSession(sessionId); }
    public ValidationSummary getValidationSummary()         { return validationSummary; }

    public String getPreviousPage(HttpServletRequest request) {
        String currentPage = getCurrentPage();
        int index = pageList.indexOf(currentPage);
        if(index == -1 || index == 0) {
            // not found or first
            return null;
        }
        String previousPage = pageList.get(index - 1);
        return previousPage;
    }

    /**
     * @param request TODO will almost definitely replace request with a reference to
     *                     the internalized application state (cf. session)
     * @return
     */
    public String getNextPage(HttpServletRequest request) {
        String currentPage = getCurrentPage();
        int index = pageList.indexOf(currentPage);
        if(index == -1 || index == (pageList.size() -1)) {
            // not found or last
            return null;
        }
        String nextPage = pageList.get(index + 1);
        return nextPage;
    }

    protected String showForm(HttpServletRequest request, Model model) {

        LOG.trace("Starting AbstractFormController.showForm");
        try {
            LOG.debug("model = {}", model);

            model.addAttribute("previousPage", getPreviousPage(request));   // not sure if we can use this as the request data is not available yet
            model.addAttribute("currentPage", getCurrentPage());
            model.addAttribute("nextPage", getNextPage(request));           // not sure if we can use this as the request data is not available yet
            model.addAttribute("pageTitle", getPageTitle());

            copyFromSessionToModel(request, getFields(), model);

            return getCurrentPage();        // returns the view name
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AbstractFormController.showForm");
        }
    }

    /**
     * Including Model as a parameter gives us access to the model, but does not
     * populate it with the request data.  To populate the model with request data
     * you need to include @ModelAttributes for the specific values you want persisted
     * which must be hard coded and therefore does not work for a configurable approach
     *
     * If you want access to the session you can include the session as a parameter
     * or include the request as a parameter and get it from there.  If you wish to
     * store the intermediate values in the session you obviously need access, though
     * this will be temporary as we can't maintain a consistent session for mobile
     * clients and will move to an application cache, probably memcached.
     * We could use @SessionAttributes which have a configurable persistence
     * mechanism that defaults to the session, except they are completely untestable
     * without a fully working application which is itself not workable.
     *
     * So since request gives us what we want in term of the parameter map, and Model
     * does not it seems the only reason to include model is so we can set values in it.
     *
     * Including RedirectAttributes is needed if we wish redirects to work at all
     * even if we never use it!!
     */
    protected String postForm(HttpServletRequest request,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttrs) {

        LOG.trace("Starting AbstractFormController.postForm");
        try {
            LOG.debug("redirectAttrs = {}, model = {}", redirectAttrs, model);
            LOG.debug("session = {}", session);
            LOG.debug("request.getParameterMap() = {}", request.getParameterMap());

            copyFromRequestToSession(request, getFields());

            getValidationSummary().reset();
            validate(request.getParameterMap(), getFields());

            if(hasErrors()) {
                LOG.info("there are validation errors, re-showing form");
                copyFromRequestToModel(request, getFields(), model);
                model.addAttribute("validationErrors", getValidationSummary());
                return showForm(request, model);
            }

            String nextPage = "redirect:" + getNextPage(request);
            LOG.debug("next page = {}", nextPage);

            finalizePostForm(request);

            return nextPage;
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AbstractFormController.postForm");
        }
    }

    protected void finalizePostForm(HttpServletRequest request) {
        // do nothing
    }

    /**
     * Copy the named values from the session to the model
     */
    protected void copyFromSessionToModel(HttpServletRequest request, String[] fieldNames, Model model) {
        LOG.trace("Started AbstractFormController.syncSessionToModel");
        try {
            Parameters.validateMandatoryArgs(new Object[]{request, model}, new String[]{"request", "model"});
            if(fieldNames == null) {
                return;
            }

            LOG.debug("fieldNames = {}", Arrays.asList(fieldNames));
            HttpSession session = request.getSession();
            for(String fieldName: fieldNames) {
                Object fieldValue = session.getAttribute(fieldName);
                LOG.debug("fieldName = {}, fieldValue = {}", fieldName, fieldValue);
                model.addAttribute(fieldName, fieldValue);
            }
        } finally {
            LOG.trace("Ending AbstractFormController.syncSessionToModel");
        }
    }

    protected void copyFromRequestToModel(HttpServletRequest request, String[] fieldNames, Model model) {
        LOG.trace("Started AbstractFormController.syncRequestToModel");
        try {
            Parameters.validateMandatoryArgs(new Object[]{request, model}, new String[]{"request", "model"});
            if(fieldNames == null) {
                return;
            }

            LOG.debug("fieldNames = {}", Arrays.asList(fieldNames));
            for(String fieldName: fieldNames) {
                Object fieldValue;
                String[] requestValues = request.getParameterMap().get(fieldName);
                if(requestValues == null || requestValues.length > 1) {
                    fieldValue = requestValues;
                } else {
                    fieldValue = requestValues[0];
                }
                LOG.debug("fieldName = {}, fieldValue = {}", fieldName, fieldValue);
                model.addAttribute(fieldName,  fieldValue);
            }
        } finally {
            LOG.trace("Ending AbstractFormController.syncRequestToModel");
        }
    }

    /**
     * Copy the named values from the model to the session
     */
    protected void copyFromRequestToSession(HttpServletRequest request, String[] fieldNames) {
        LOG.trace("Started AbstractFormController.copyFromRequestToSession");
        try {
            Parameters.validateMandatoryArgs(request, "request");
            if(fieldNames == null) {
                return;
            }

            LOG.debug("fieldNames = {}", Arrays.asList(fieldNames));
            HttpSession session = request.getSession();
            for(String fieldName: fieldNames) {
                Object fieldValue;
                String[] requestValues = request.getParameterMap().get(fieldName);
                if(requestValues == null || requestValues.length > 1) {
                    fieldValue = requestValues;
                } else {
                    fieldValue = requestValues[0];
                }
                LOG.debug("fieldName = {}, fieldValue = {}", fieldName, fieldValue);
                session.setAttribute(fieldName,  fieldValue);
            }
        } finally {
            LOG.trace("Ending AbstractFormController.copyFromRequestToSession");
        }
    }

    protected void removeFromSession(HttpSession session, String[] fieldNames) {
        LOG.trace("Started AbstractFormController.removeFromSession");
        try {
            Parameters.validateMandatoryArgs(session, "session");
            if(fieldNames == null) {
                return;
            }

            LOG.debug("fieldNames = {}", Arrays.asList(fieldNames));
            for(String fieldName: fieldNames) {
                session.removeAttribute(fieldName);
            }
        } finally {
            LOG.trace("Ending AbstractFormController.removeFromSession");
        }
    }

    protected void validateMandatoryDateField(Map<String, String[]> allfieldValues, String fieldTitle, String id, String[] dateFieldNames) {
        LOG.trace("Started AbstractFormController.validateMandatoryDateField");
        Parameters.validateMandatoryArgs(new Object[]{allfieldValues, id, dateFieldNames, fieldTitle}, new String[]{"allfieldValues", "id", "dateFieldNames", "fieldTitle"});

        boolean emptyField = false;
        boolean populatedField = false;
        for(String dateField: dateFieldNames) {
            String[] fieldValues = allfieldValues.get(dateField);
            LOG.debug("fieldName = {}, fieldValues={}", dateField, new LoggingObjectWrapper(fieldValues));
            if(isEmpty(fieldValues)) {
                LOG.debug("missing mandatory field: {}", dateField);
                emptyField = true;
            } else {
                populatedField = true;
            }
        }
        if(emptyField) {
            if(populatedField) {
                addFormError(id, fieldTitle, "Invalid value");
            } else {
                addFormError(id, fieldTitle, "You must complete this section");
            }
        }
        LOG.trace("Ending AbstractFormController.validateMandatoryDateField");
    }

    protected void validateMandatoryFields(Map<String, String[]> allfieldValues, String fieldTitle, String...fieldNames) {
        LOG.trace("Started AbstractFormController.validateMandatoryField");
        Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldTitle}, new String[]{"allfieldValues", "fieldTitle"});

        LOG.debug("fieldNames = {}", new LoggingObjectWrapper(fieldNames));
        for(String fieldName: fieldNames) {
            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));

            if(isEmpty(fieldValues)) {
                LOG.debug("missing mandatory field: {}", fieldName);
                addFormError(fieldName, fieldTitle, "You must complete this section");
                break;
            }
        }

        LOG.trace("Ending AbstractFormController.validateMandatoryField");
    }

    /**
     * At least two out of three address fields should be populated
     */
    protected void validateAddressFields(Map<String, String[]> allfieldValues, String fieldTitle, String id, String[] addressFieldNames) {
        LOG.trace("Started AbstractFormController.validateMandatoryField");
        Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldTitle}, new String[]{"allfieldValues", "fieldTitle"});
        if(addressFieldNames == null || addressFieldNames.length == 0) {
            return;
        }

        if(addressFieldNames.length != 3) {
            throw new IllegalArgumentException("There must be three address fields");
        }

        int populated = 0;
        LOG.debug("addressFieldNames = {}", new LoggingObjectWrapper(addressFieldNames));
        for(String fieldName: addressFieldNames) {
            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));

            if(isEmpty(fieldValues) == false) {
                populated++;
            }
        }

        if(populated < 2) {
            LOG.debug("missing mandatory address field: {}", new LoggingObjectWrapper(addressFieldNames));
            addFormError(id, fieldTitle, "You must complete this section");
        }

        LOG.trace("Ending AbstractFormController.validateMandatoryField");
    }

    /**
     * Validate that two fields match (e.g. for email address, passwords etc)
     */
    protected void validateMatchingValues(Map<String, String[]> allfieldValues, String firstTitle, String firstName, String secondTitle, String secondName, boolean caseInsensitve) {
        LOG.trace("Started AbstractFormController.validateMatchingValues");
        try {
            Parameters.validateMandatoryArgs(new Object[]{allfieldValues, firstTitle, firstName, secondTitle, secondName}, new String[]{"allfieldValues", "firstTitle", "firstName", "secondTitle", "secondName"});

            String[] firstFieldValues = allfieldValues.get(firstName);
            String[] secondFieldValues = allfieldValues.get(secondName);
            LOG.debug("firstName = {}, firstFieldValues={}", firstName, new LoggingObjectWrapper(firstFieldValues));
            LOG.debug("secondName = {}, secondFieldValues={}", firstName, new LoggingObjectWrapper(secondFieldValues));

            if(firstFieldValues == secondFieldValues) {
                return;
            }

            if(firstFieldValues != null && firstFieldValues != null) {
                Set<String> firstValuesSet = new HashSet<>(Arrays.asList(firstFieldValues));
                Set<String> secondValuesSet = new HashSet<>(Arrays.asList(secondFieldValues));

                if(caseInsensitve) {
                    firstValuesSet = CollectionUtils.toUpperCase(firstValuesSet);
                    secondValuesSet = CollectionUtils.toUpperCase(secondValuesSet);
                }
                if(firstValuesSet.equals(secondValuesSet)) {
                    return;
                }
            }

            LOG.debug("{} ({}) does not match {} ({}).", firstName, new LoggingObjectWrapper(firstFieldValues), secondName, new LoggingObjectWrapper(secondFieldValues));
            addFormError(secondName, secondTitle, "Your emails must match");

        } finally {
            LOG.trace("Started AbstractFormController.validateMatchingValues");
        }
    }

    /**
     * Validates non-blank values against the supplied regular expression.
     * DOES NOT CHECK BLANK VALUES
     */
    protected void validateRegexField(Map<String, String[]> allfieldValues, String fieldTitle, String fieldName, String regex) {
        LOG.trace("Started AbstractFormController.validateRegexField");
        try {
            Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldTitle, fieldName, regex}, new String[]{"allfieldValues", "fieldTitle", "fieldName", "regex"});

            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));
            if(isEmpty(fieldValues)) {
                LOG.debug("nothing to do");
                return;
            }

            Pattern pattern = Pattern.compile(regex);   // this is re-usable
            for(String value: fieldValues) {
                Matcher matcher = pattern.matcher(value);
                if(matcher.matches() == false) {
                    LOG.debug("value: '{}' does not match regex: {}", value, regex);
                    addFormError(fieldName, fieldTitle, "Invalid value");
                    break;
                } else {
                    LOG.debug("value: '{}' matches regex: {}", value, regex);
                }
            }

        } catch(RuntimeException e) {
            LOG.error("Problems checking regular exception field: {}", fieldName);
            throw e;

        } finally {
            LOG.trace("Ending AbstractFormController.validateRegexField");
        }
    }

    /**
     * TODO -> c.f. Hamcrest Matchers
     * Return true if the value is populated and matches the value parameter.  Otherwise return false;
     *
     * @return false if there are no values for this fieldName, or all of them are blank
     * or the supplied value parameter does not match all the non-blank values (after trimming).
     * Otherwise return true;
     */
    protected boolean fieldValue_Equals(Map<String, String[]> allfieldValues, String fieldName, String value) {
        LOG.trace("Started AbstractFormController.isValuePopulated");
        try {
            Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldName, value}, new String[]{"allfieldValues", "fieldName", "value"});

            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));
            if(isEmpty(fieldValues)) {
                return false;
            }

            boolean match = false;
            boolean mismatch = false;
            for(String fieldValue: fieldValues) {
                if(fieldValue == null || fieldValue.trim().equals("")) {
                    continue;
                }

                if(value.equals(fieldValue.trim())) {
                    match = true;
                } else  {
                    mismatch = true;
                }
            }

            if(mismatch) {
                return false;
            }
            return match;
        } finally {
            LOG.trace("Ending AbstractFormController.isValuePopulated");
        }
    }

    protected boolean fieldValue_NotBlank(Map<String, String[]> allfieldValues, String fieldName) {
        return !fieldValue_Blank(allfieldValues, fieldName);
    }

    protected boolean fieldValue_Blank(Map<String, String[]> allfieldValues, String fieldName) {
        LOG.trace("Started AbstractFormController.fieldValue_Blank");
        try {
            Parameters.validateMandatoryArgs(new Object[]{allfieldValues, fieldName}, new String[]{"allfieldValues", "fieldName"});

            String[] fieldValues = allfieldValues.get(fieldName);
            LOG.debug("fieldName = {}, fieldValues={}", fieldName, new LoggingObjectWrapper(fieldValues));
            return isEmpty(fieldValues);

        } finally {
            LOG.trace("Ending AbstractFormController.fieldValue_Blank");
        }
    }

    public String safeTrim(String value) {
        if(value == null) {
            return null;
        }
        return value.trim();
    }

    private boolean isEmpty(String[] values) {
        if(values == null) {
            return true;
        }

        // None of them are populated, even if there is more than one
        for(String value: values) {
            if(StringUtils.isEmpty(value) == false) {
                return false;
            }
        }
        return true;
    }

    protected void addFormError(String id, String displayName, String errorMessage) {
        validationSummary.addFormError(id, displayName, errorMessage);
    }

    protected boolean hasErrors() {
        return validationSummary.hasFormErrors();
    }

    protected void logRequest(HttpServletRequest request) {
        LOG.info("request = {}", request);
        if(request == null) {
            return;
        }

        LOG.debug("\trequest URL = '{}{}'", request.getRequestURL(), request.getQueryString() == null ? "" : "?" + request.getQueryString());
        LOG.debug("\tmethod = {}, contentType = {}, character encoding = {}, async context = {}", request.getMethod(), request.getContentType(), request.getCharacterEncoding(), request.getAsyncContext());

        List<String> paramNames = Collections.list(request.getParameterNames());
        LOG.debug("\tparameter names = {}", paramNames);
        for(String paramName: paramNames) {
            LOG.debug("\tparameter: '{}' = '{}'", paramName, new LoggingObjectWrapper(request.getParameterValues(paramName)));
        }

        List<String> attrNames = Collections.list(request.getAttributeNames());
        LOG.debug("\tattribute names = {}", attrNames);
        for(String attrName: attrNames) {
            String attrValueStr = null;
            Object attrValue = request.getAttribute(attrName);
            if(attrValue != null) {
                attrValueStr = attrValue.getClass().getName() + "@" + System.identityHashCode(attrValue);
            }
            LOG.debug("\tattribute: '{}' = '{}'", attrName, attrValueStr);
        }

        List<String> headerNames = Collections.list(request.getHeaderNames());
        LOG.debug("\theader names = {}", headerNames);
        for(String header: headerNames) {
            LOG.debug("\theader: '{}' = '{}'", header, request.getHeader(header));
        }

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies) {
            LOG.debug("\tcookie: '{}' = '{}'", cookie.getName(), cookie.getValue());  // there are other properties as well
        }
    }

    protected void logSession(HttpSession session) {
        LOG.info("session = {}", session);
        if(session == null) {
            return;
        }

        session.getAttributeNames();

        LOG.debug("\tsession ID = '{}', creation time = {}, last accessed time = {}", session.getId(), session.getCreationTime(), session.getLastAccessedTime());

        List<String> attrNames = Collections.list(session.getAttributeNames());
        LOG.debug("\tattribute names = {}", attrNames);
        for(String attrName: attrNames) {
            String attrValueStr = null;
            Object attrValue = session.getAttribute(attrName);
            if(attrValue != null) {
                attrValueStr = attrValue.getClass().getName() + "@" + System.identityHashCode(attrValue);
            }
            LOG.debug("\tattribute: '{}' = {}:'{}'", attrName, attrValueStr, attrValue);
        }
    }

    protected List<Map<String, String>> getFieldCollections(HttpSession session, String collectionName) {
        return getFieldCollections(session, collectionName, false);
    }

    /**
     * make the map the field collection
     * make the list the field collection list? (yuk)
     */
    @SuppressWarnings("unchecked")
    protected List<Map<String, String>> getFieldCollections(HttpSession session, String collectionName, boolean create) {
        LOG.trace("Started AbstractFormController.getFieldCollections");
        try {
            Parameters.validateMandatoryArgs(session, "session");
            LOG.debug("collectionName = {}, create = {}", collectionName, create);

            if(collectionName == null) {
                return null;
            }

            Object value = session.getAttribute(collectionName);
            LOG.debug("value = {}", value);
            if(value == null) {
                if(create) {
                    LOG.debug("Creating new Field Collection");
                    List<Map<String, String>>newFieldCollection = new ArrayList<>();
                    session.setAttribute(collectionName,  newFieldCollection);
                    return newFieldCollection;
                } else {
                    return null;
                }
            }

            if(value instanceof List) {
                return (List<Map<String, String>>)value;
            }

            throw new IllegalStateException("FieldCollection List not of expected type:" + value.getClass().getName() + ", expecting " + List.class.getName());

        } finally {
            LOG.trace("Ending AbstractFormController.getFieldCollections");
        }
    }

    protected void setFieldCollections(HttpSession session, String collectionName, List<Map<String, String>> values) {
        Parameters.validateMandatoryArgs(new Object[]{session, collectionName}, new String[]{"session", "collectionName"});
        session.setAttribute(collectionName,  values);
    }

    protected Boolean getYesNoBooleanFieldValue(HttpServletRequest request, String fieldName) {
        return getBooleanFieldValue(request, fieldName, "yes", "no");
    }

    /**
     * case-insensitive
     *
     * @throws IllegalFieldValueException
     */
    protected Boolean getBooleanFieldValue(HttpServletRequest request, String fieldName, String trueValue, String falseValue)
            throws IllegalFieldValueException {

                Parameters.validateMandatoryArgs(new Object[]{request, fieldName, trueValue, falseValue}, new String[]{"request", "fieldName", "trueValue", "falseValue"});
                String[] values = request.getParameterValues(fieldName);
                if(values == null || values.length == 0) {
                    return null;
                }

                boolean yes = false;
                boolean no = false;
                boolean other = false;
                for(String value : values) {
                    if(trueValue.equalsIgnoreCase(value)) {
                        yes = true;
                    } else if(falseValue.equalsIgnoreCase(value)) {
                        no = true;
                    } else if(StringUtils.isEmpty(value) == false) {
                        other = true;
                    }
                }

                if(other == false && no == false && yes == false) {
                    return null;
                }

                if(other == false) {
                    if(yes == true && no == false) {
                        return Boolean.TRUE;
                    } else if(yes == false && no == true) {
                        return Boolean.FALSE;
                    }
                }

                if(other == true) {
                    throw new IllegalFieldValueException(fieldName, values);
                }
                throw new InconsistentFieldValuesException(fieldName, values);
            }

    protected String getNextIdValue(List<Map<String, String>> fieldCollectionList, String idFieldName) {
        int value = 0;
        for(int index = 0; index < fieldCollectionList.size(); index++) {
            Map<String, String> existingFieldCollection = fieldCollectionList.get(index);
            String id = existingFieldCollection.get(idFieldName);
            if(id == null) {
                LOG.error("Missing ID field: " + idFieldName + " for record(" + index + "): " + existingFieldCollection);
                throw new IllegalArgumentException("Missing ID field: " + idFieldName + " for record(" + index + ")");
            }
            try {
                int idInt = Integer.parseInt(id.trim());
                if(idInt > value) {
                    value = idInt;
                }
            } catch(NumberFormatException e) {
                LOG.error("ID field: " + idFieldName + " value is not an integer(" + id + ")");
                throw new IllegalArgumentException("ID field: " + idFieldName + " value is not an integer(" + id + ")");
            }
        }

        String result = Integer.toString(value + 1);
        return result;
    }

    protected void saveFormattedDate(HttpSession session, String dateFieldName, String format, String yearFieldName, String monthFieldName, String dayFieldName) {
    
        Parameters.validateMandatoryArgs(new Object[]{session, dateFieldName, format, yearFieldName, monthFieldName, dayFieldName},
                                         new String[]{"session", "dateFieldName", "format", "yearFieldName", "monthFieldName", "dayFieldName"});
    
        Integer day = getSessionInt(session, dayFieldName, "dayFieldName");
        Integer month = getSessionInt(session, monthFieldName, "monthFieldName");
        Integer year = getSessionInt(session, yearFieldName, "yearFieldName");
        if(day == null && month == null && year == null) {
            session.removeAttribute(dateFieldName);
        }
        if(day == null || month == null || year == null) {
            throw new IllegalArgumentException("All date fields(day:'" + day + "', month:'" + month + "', year:'" + year + "') are not populated");
        }
    
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH, month + 1);
        calendar.set(Calendar.YEAR, year);
        Date date = calendar.getTime();
    
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        session.setAttribute(dateFieldName, dateString);
    }

    protected Integer getSessionInt(HttpSession session, String field, String fieldName) {
        Parameters.validateMandatoryArgs(new Object[]{session, field, fieldName}, new String[]{"session", "field", "fieldName"});
        Object object = session.getAttribute(field);
        if(StringUtils.isEmpty(object)) {
            return null;
        }
    
        if((object instanceof String) == false) {
            throw new IllegalArgumentException("Expecting a 'String' session value:" + fieldName + " but found a " + object.getClass().getName());
        }
        String value = (String)object;
        try {
            return Integer.valueOf(value.trim());
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Expecting a 'numerical' session value:" + fieldName + " but found: '" + value + "'");
        }
    }

    public static class ValidationSummary {
        private List<ValidationError> formErrors;

        public ValidationSummary() {
            formErrors = new ArrayList<>();
        }

        public List<ValidationError> getFormErrors()  { return formErrors; }

        protected void addFormError(String id, String displayName, String errorMessage) {
            formErrors.add(new FormValidationError(id, displayName, errorMessage));
        }

        public void reset() {
            formErrors.clear();
        }

        public boolean hasFormErrors() {
            return !formErrors.isEmpty();
        }

        public String getErrorDisplayName(String id) {
            ValidationError error = getError(id);
            if(error == null) {
                return null;
            }
            return error.getDisplayName();
        }

        public String getErrorMessage(String id) {
            ValidationError error = getError(id);
            if(error == null) {
                return null;
            }
            return error.getErrorMessage();
        }

        public ValidationError getError(String id) {
            if(formErrors == null || StringUtils.isEmpty(id)) {
                return null;
            }

            for(ValidationError error: formErrors) {
                if(id.equals(error.getId())) {
                    return error;
                }
            }

            return null;
        }

        public boolean hasError(String id) {
            ValidationError error = getError(id);
            return error != null;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();

            buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
            buffer.append("=[");
            buffer.append(", formErrors = ").append(formErrors);
            buffer.append("]");

            return buffer.toString();
        }
    }

    public static class ValidationPatterns {

        public static int    ACCOUNT_HOLDER_NAME_MAX_LENGTH = 60;
        public static int    ACCOUNT_NUMBER_MAX_LENGTH      = 10;
        public static int    ACCOUNT_NUMBER_MIN_LENGTH      = 6;
        public static int    CURRENCY_REGEX_MAX_LENGTH      = 12;
        public static int    FULL_NAME_MAX_LENGTH           = 60;
        public static int    NAME_MAX_LENGTH                = 35;
        public static int    NATIONALITY_MAX_LENGTH         = 35;
        public static int    PHONE_NUMBER_MAX_LENGTH        = 20;

        public static String RESTRICTED_CHARS               = "^[A-Za-zÀ-ƶ\\s0-9\\(\\)&£€\\\"\\'!\\-_:;\\.,/\\?\\@]*$";
        public static String PHONE_NUMBER_SEQ               = "^[0-9 \\-]$";
        public static String NATIONALITY_SEQ                = "^[a-zA-ZÀ-ƶ \\-\\']$";

        public static String ACCOUNT_HOLDER_NAME_REGEX      = "^[" + RESTRICTED_CHARS + "]{1," + ACCOUNT_HOLDER_NAME_MAX_LENGTH + "}$$";
        public static String ACCOUNT_NUMBER_REGEX           = "^\\d{" + ACCOUNT_NUMBER_MIN_LENGTH + "," + ACCOUNT_NUMBER_MAX_LENGTH + "}$$";
        public static String CURRENCY_REGEX                 = "^\\£?[0-9]{1,8}(\\.[0-9]{1,2})?$";
        public static String DECIMAL_REGEX                  = "^[0-9]{1,12}(\\.[0-9]{1,2})?$";
        public static String EMAIL_REGEX                    = "^[a-zA-Z0-9\\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])*(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9]))+$";
        public static String FULL_NAME_REGEX                = "^[" + RESTRICTED_CHARS + "]{1, " + FULL_NAME_MAX_LENGTH + "}$$";
        public static String NATIONALITY_REGEX              = "^[" + NATIONALITY_SEQ + "]{1," + NATIONALITY_MAX_LENGTH + "}$$";
        public static String NINO_REGEX                     = "(([A-CEHJ-MOPRSW-Y]{1}[A-CEGHJ-NPR-TW-Z]{1}[0-9]{6})|([G]{1}[ACEGHJ-NPR-TW-Z]{1}[0-9]{6})|([N]{1}[A-CEGHJL-NPR-TW-Z]{1}[0-9]{6})|([T]{1}[A-CEGHJ-MPR-TW-Z]{1}[0-9]{6})|([Z]{1}[A-CEGHJ-NPR-TW-Y]{1}[0-9]{6}))(([A-D ]{1})|([\\S\\s\\d]{0,0}))";
        public static String NUMBER_OR_SPACE_REGEX          = "^[0-9 ]*$";
        public static String NUMBER_REGEX                   = "^[0-9]*$";
        public static String PHONE_NUMBER_REGEX             = "^[" + PHONE_NUMBER_SEQ + "]{7," + PHONE_NUMBER_MAX_LENGTH + "}$$";
        public static String POSTCODE_REGEX                 = "^(?i)(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) ?[0-9][A-Z]{2})$";
        public static String RESTRICTED_POSTCODE_REGEX      = "^[A-Za-zÀ-ƶ\\s0-9]*$";
        public static String SORT_CODE_REGEX                = "^\\d{6}$";
        public static String SURNAME_REGEX                  = "^[" + RESTRICTED_CHARS + "]{1," + NAME_MAX_LENGTH + "}$$";
    }
}
