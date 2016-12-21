package uk.gov.dwp.carersallowance.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import uk.gov.dwp.carersallowance.session.FieldCollection;
import uk.gov.dwp.carersallowance.session.IllegalFieldValueException;
import uk.gov.dwp.carersallowance.session.InconsistentFieldValuesException;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.session.SessionManager.Session;
import uk.gov.dwp.carersallowance.session.UnknownRecordException;
import uk.gov.dwp.carersallowance.transformations.Transformation;
import uk.gov.dwp.carersallowance.transformations.TransformationType;
import uk.gov.dwp.carersallowance.utils.LoggingObjectWrapper;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.validations.FormValidations;
import uk.gov.dwp.carersallowance.validations.ValidationSummary;

// TODO SuppressWarnings temporary, all complaints largely true. Remove them later and re-analyze
public class AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFormController.class);

    private static final String SESSION_TRANSFORMATIONS_KEY = "%s.transformations";

    protected static final String HTTP_POST = "POST";
    protected static final String HTTP_GET = "GET";

    @Value("${application.version}")
    private String applicationVersion;

    public static final String   PHONE_REGEX = "[ 0123456789]{0,20}";       // probably convert to an enum
    public static final String   EMAIL_REGEX = "[ 0123456789]{0,20}";       // probably convert to an enum

    private MessageSource     messageSource;
    private SessionManager    sessionManager;
    private ValidationSummary validationSummary;
    protected List<String>      pageList;

    protected static final String[] PAGES = {
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
            //removed to get through journey
//            "/your-income/employment/been-employed",
//            "/your-income/self-employment/self-employment-dates",
//            "/your-income/self-employment/pensions-and-expenses",
//            "/your-income/employment/additional-info",
//            "/your-income/statutory-sick-pay",
//            "/your-income/smp-spa-sap",
//            "/your-income/fostering-allowance",
//            "/your-income/direct-payment",
//            "/your-income/other-income",
            "/pay-details/how-we-pay-you",
            "/information/additional-info",
            "/preview",
            "/consent-and-declaration/declaration",
            "/submit-claim"
    };

    private LegacyValidation  legacyValidation; //  TODO remove this

    public AbstractFormController(SessionManager sessionManager, MessageSource messageSource) {
        this.sessionManager = sessionManager;
        this.messageSource = messageSource;

        validationSummary = new ValidationSummary();
        legacyValidation = new LegacyValidation(messageSource, validationSummary);
        pageList = Arrays.asList(PAGES);
    }

    protected LegacyValidation getLegacyValidation() { return legacyValidation; }

    protected String getPageName() {
        return null;
    }

    public MessageSource     getMessageSource()             { return messageSource; }
    public Session           getSession(String sessionId)   { return sessionManager.getSession(sessionId); }
    public ValidationSummary getValidationSummary()         { return validationSummary; }

    public String getCurrentPage(HttpServletRequest request) {
        String path = request.getServletPath();
        return path;
    }

    public String[] getFields(String pageName) {
        return getFields(messageSource, pageName);
    }

    public static String[] getFields(MessageSource messageSource, String pageName) {
        LOG.debug("pageName = {}", pageName);
        if(pageName == null) {
            return null;
        }

        String fieldNameList = messageSource.getMessage(pageName + ".fields", null, null, null);
        LOG.debug("fieldNameList = {}", fieldNameList);
        if(fieldNameList == null) {
            return null;
        }

        String[] fieldNames = fieldNameList.split(",");
        for(int index = 0; index < fieldNames.length; index++) {
            fieldNames[index] = fieldNames[index].trim();
        }

        LOG.debug("fieldNames = {}", new LoggingObjectWrapper(fieldNames));
        return fieldNames;
    }

    public String[] getSharedFields() {
        return null;
    }

    public String[] getReadOnlyFields() {
        return null;
    }

    public String getPreviousPage(HttpServletRequest request) {
        return getPreviousPage(request, pageList);
    }

    public String getPreviousPage(HttpServletRequest request, List<String> currentPageList) {
        String currentPage = getCurrentPage(request);
        int index = currentPageList.indexOf(currentPage);
        if(index == -1 || index == 0) {
            // not found or first
            return null;
        }
        String previousPage = currentPageList.get(index - 1);
        return previousPage;
    }

    public String getNextPage(HttpServletRequest request) {
        return getNextPage(request, pageList);
    }
    /**
     * @param request TODO will almost definitely replace request with a reference to
     *                     the internalized application state (cf. session)
     * @return
     */
    public String getNextPage(HttpServletRequest request, List<String> currentPageList) {
        String currentPage = getCurrentPage(request);
        int index = currentPageList.indexOf(currentPage);
        if(index == -1 || index == (currentPageList.size() -1)) {
            // not found or last
            return null;
        }
        String nextPage = currentPageList.get(index + 1);
        return nextPage;
    }

    protected String showForm(HttpServletRequest request, Model model) {
        return showFormInternal(request, model);
    }

    private String showFormInternal(HttpServletRequest request, Model model) {
        LOG.trace("Starting AbstractFormController.showFormInternal");
        try {
            LOG.debug("model = {}", model);

            String path = request.getServletPath();
            LOG.info("path = {}", path);

            model.addAttribute("previousPage", getPreviousPage(request));   // not sure if we can use this as the request data is not available yet
            model.addAttribute("currentPage", getCurrentPage(request));
            model.addAttribute("nextPage", getNextPage(request));           // not sure if we can use this as the request data is not available yet
//            model.addAttribute("pageTitle", getPageTitle());

            // if we copy everything then we don't need a static list
            // in which case the fields used are defined by the jsp, which is far less visible
            // it would also remove the distinction between shared & readonly fields and ordinary fields
            // so we probably could verify if a field name was duplicated.
            // So probably best to stick to a list of fields, but make it data driven (in messages.properties)
            String[] fields = getFields(getCurrentPage(request));
            if(fields == null || fields.length == 0) {
                fields = getFields(getPageName());
            }

            copyFromSessionToModel(request, fields, model);
            copyFromSessionToModel(request, getSharedFields(), model);
            copyFromSessionToModel(request, getReadOnlyFields(), model);

            return getCurrentPage(request);        // returns the view name
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AbstractFormController.showFormInternal");
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
    protected String postForm(HttpServletRequest request, HttpSession session, Model model) {

        LOG.trace("Starting AbstractFormController.postForm");
        try {
            LOG.debug("session = {}", session);
            LOG.debug("request.getParameterMap() = {}", request.getParameterMap());

            String pageName = request.getParameter("pageName");
            String[] fields = getFields(pageName);

            getValidationSummary().reset();

            Map<String, String[]> existingFieldValues = getAllFieldValues(request);
            validate(fields, request.getParameterMap(), existingFieldValues);

            if(hasErrors()) {
                LOG.info("there are validation errors, re-showing form");
                String form = showForm(request, model);

                // add the values of the current form to the model as well as the session values
                copyFromRequestToModel(request, fields, model);
                copyFromRequestToModel(request, getSharedFields(), model);
                model.addAttribute("validationErrors", getValidationSummary());

                return form;
            }

            copyFromRequestToSession(request, fields);
            copyFromRequestToSession(request, getSharedFields());

            finalizePostForm(request);

            // add the fieldCollection id field to the url (if its populated)

            // we need to add the # to the end of the redirect url to stop the browser from inheriting the hash url
            // from the previous page ('cos its a redirect), see https://www.w3.org/TR/cuap#uri
            // we needed to do a redirect so that we show the page we are on, rather than the page we submitted to
            // and we needed to have submitted to the previous page, so we can validate and update the form, which
            // is part of the previous screen and not the one we are going to.  We don't know if the previous page
            // had a hash location, as the hash location is never submitted to the server
            // there are various things we *could* do with javascript, but none of them will work in the no-javascript journey
            String nextPage = "redirect:" + getNextPage(request) + "#";
            LOG.debug("next page = {}", nextPage);

            return nextPage;
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AbstractFormController.postForm");
        }
    }

    private Map<String, String[]> getAllFieldValues(HttpServletRequest request) {
        LOG.trace("Started AbstractFormController.getAllFieldValues");
        try {
            if(request == null) {
                return null;
            }

            StringBuffer buffer = new StringBuffer();

            Map<String, String[]> map = new HashMap<>();
            HttpSession session = request.getSession();
            List<String> sessionNames = Collections.list(session.getAttributeNames());
            Collections.sort(sessionNames);
            for(String attrName: sessionNames) {
                Object attrValue = session.getAttribute(attrName);
                if(attrValue instanceof String) {
                    buffer.append(attrName).append(" = ").append((String)attrValue).append(", ");
                    String[] attrValues = new String[]{(String)attrValue};
                    map.put(attrName, attrValues);
                } else if(attrValue instanceof String[]) {
                    buffer.append(attrName).append(" = ").append(Arrays.asList((String[])attrValue)).append(", ");
                    map.put(attrName, (String[])attrValue);
                } else {
                    // do nothing
                }
            }

            LOG.debug("all field values = {}", buffer.toString());
            return map;
        } finally {
            LOG.trace("Ending AbstractFormController.getAllFieldValues");
        }
    }

    protected void finalizePostForm(HttpServletRequest request) {
        // do nothing
    }

    protected void copyMapToSession(Map<String, String> map, String[] fieldNames, HttpSession session) {
        LOG.trace("Started AbstractFormController.copyMapToSession");
        try {
            Parameters.validateMandatoryArgs(new Object[]{map, session}, new String[]{"map", "session"});
            if(fieldNames == null) {
                return;
            }

            for(String fieldName: fieldNames) {
                String fieldValue = map.get(fieldName);
                LOG.debug("fieldName = {}, fieldValue = {}", fieldName, fieldValue);
                session.setAttribute(fieldName, fieldValue);
            }
        } finally {
            LOG.trace("Ending AbstractFormController.copyMapToSession");
        }
    }

    /**
     * Copy the named values from the session to the model
     */
    protected void copyFromSessionToModel(HttpServletRequest request, String[] fieldNames, Model model) {
        LOG.trace("Started AbstractFormController.copyFromSessionToModel");
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
            LOG.trace("Ending AbstractFormController.copyFromSessionToModel");
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
                String transformationsKey = String.format(SESSION_TRANSFORMATIONS_KEY, fieldName);
                List<Transformation> transformations = getTransformations(transformationsKey);
                fieldValue = applySessionTransformations(fieldName, fieldValue, transformations);
                session.setAttribute(fieldName,  fieldValue);
            }
        } finally {
            LOG.trace("Ending AbstractFormController.copyFromRequestToSession");
        }
    }

    private List<Transformation> getTransformations(String transformationsKey) {
        LOG.trace("Started AbstractFormController.getTransformations");
        try {
            LOG.debug("transformationsKey = {}", transformationsKey);
            if(StringUtils.isBlank(transformationsKey)) {
                return null;
            }

            List<Transformation> list = new ArrayList<>();
            String transformationList = messageSource.getMessage(transformationsKey, null, null, Locale.getDefault());
            if(transformationList != null) {
                String[] transformationNames = transformationList.split(",");
                for(String transformationName : transformationNames ) {
                    transformationName = transformationName.trim();
                    LOG.debug("transformationName = '{}'", transformationName);
                    TransformationType type = TransformationType.valueOfName(transformationName);
                    list.add(type.getTransformation());
                }
            }

            LOG.debug("list = {}", list);
            return list;
        } catch(RuntimeException e) {
            LOG.error("Problems getting transformations for " + transformationsKey, e);
            throw e;
        } finally {
            LOG.trace("Ending AbstractFormController.getTransformations");
        }
    }

    private Object applySessionTransformations(String fieldName, Object fieldValue, List<Transformation> transformations) {
        Parameters.validateMandatoryArgs(fieldName,  "fieldName");
        if(fieldValue == null) {
            return null;
        }

        if(fieldValue instanceof String[]) {
            String[] fieldValues = (String[])fieldValue;
            String[] transformedValues = new String[fieldValues.length];
            for(int index = 0; index < fieldValues.length; index++) {
                Object value = applySessionTransformations(fieldName, fieldValues[index], transformations);
                if(value == null || value instanceof String) {
                    transformedValues[index] = (String)value;
                } else {
                    throw new IllegalArgumentException("Expected value of type String when transforming " + fieldName + ", but received " + value.getClass().getName());
                }
            }
            return transformedValues;
        }

        if((fieldValue instanceof String) == false) {
            throw new IllegalArgumentException("Expected fieldValue of type String when transforming " + fieldName + ", but received " + fieldValue.getClass().getName());
        }

        String value = (String)fieldValue;
        for(Transformation transformation: transformations) {
            value = transformation.transform(value);
        }

        return value;
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

    public static boolean isEmpty(String[] values) {
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

    public String safeTrim(String value) {
        if(value == null) {
            return null;
        }
        return value.trim();
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
    @SuppressWarnings({ "unchecked" })
    protected List<Map<String, String>> getFieldCollections(HttpSession session, String collectionName, boolean create) {
        LOG.trace("Started AbstractFormController.getFieldCollections");
        try {
            Parameters.validateMandatoryArgs(session, "session");
            LOG.debug("collectionName = {}, create = {}", collectionName, create);

            if(collectionName == null) {
                return null;
            }

            Object value = session.getAttribute(collectionName);
            if(value != null) {
                LOG.debug("found collection({}) = {}", collectionName, value);
            } else {
                LOG.debug("No collection found");
                if(create) {
                    LOG.debug("Creating new Field Collection");
                    List<Map<String, String>>newFieldCollection = new ArrayList<>();
                    session.setAttribute(collectionName,  newFieldCollection);
                    return newFieldCollection;
                } else {
                    LOG.debug("returning null");
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

    public static Boolean getYesNoBooleanFieldValue(HttpServletRequest request, String fieldName) {
        return getBooleanFieldValue(fieldName, "yes", "no", request.getParameterValues(fieldName));
    }

    public static Boolean getYesNoBooleanFieldValue(HttpSession session, String fieldName) {
        return getBooleanFieldValue(fieldName, "yes", "no", getSessionStringAttribute(session, fieldName));
    }

    public static String[] getSessionStringAttribute(HttpSession session, String fieldName) {
        Object object = session.getAttribute(fieldName);
        if(object == null) {
            return null;
        }

        if(object instanceof String[]) {
            return (String[])object;
        } else if(object instanceof String) {
            return new String[]{(String)object};
        } else {
            throw new IllegalFieldValueException("value is not a String or String[] instance, but: " + object.getClass().getName(), null);
        }
    }

    /**
     * Return True or False object if the values match the trueValue or falseValue parameters
     *
     * all comparisons are case-insensitive
     *
     * @return null if the inputs are null,
     *         Boolean.TRUE if all the values the same as the 'trueValue' parameter
     *         Boolean.FALSE if all the values the same as the 'falseValue' parameter
     *         or throw an expcetion if it is confused
     *
     * @throws IllegalFieldValueException if one or more of the values is neither the 'trueFalse' or 'falseValue'
     * @throws InconsistentFieldValuesException if the values are not consistent, i.e. both true and false
     */
    public static Boolean getBooleanFieldValue(String fieldName, String trueValue, String falseValue, String[] values) {
        Parameters.validateMandatoryArgs(new Object[]{trueValue, falseValue}, new String[]{"trueValue", "falseValue"});
        if(values == null || values.length == 0) {
            return null;
        }

        boolean yes = false;
        boolean no = false;
        boolean other = false;

        // not a single valued boolean, so check all values
        for(String value : values) {
            if(trueValue.equalsIgnoreCase(value)) {
                yes = true;
            } else if(falseValue.equalsIgnoreCase(value)) {
                no = true;
            } else if(StringUtils.isEmpty(value) == false) {
                other = true;
            }
        }

        // there were no matches, i.e. all null or empty, so return null
        if(other == false && no == false && yes == false) {
            return null;
        }

        // there was nothing un-recognised
        if(other == false) {
            if(yes == true && no == false) {
                return Boolean.TRUE;        // we only had matches against trueValue
            } else if(yes == false && no == true) {
                return Boolean.FALSE;       // we only had matches against trueValue
            }
        }

        if(other == true) {
            // we had unrecognized values that did not match either trueValue or falseValue
            throw new IllegalFieldValueException(fieldName, values);
        }

        // we had matches to both trueValue AND falseValue
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
                throw new IllegalArgumentException("ID field: " + idFieldName + " value is not an integer(" + id + ")", e);
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

        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        String dateString = formatter.format(date);
        session.setAttribute(dateFieldName, dateString);
    }

    protected Integer getSessionInt(HttpSession session, String field, String fieldName) {
        Parameters.validateMandatoryArgs(new Object[]{session, field, fieldName}, new String[]{"session", "field", "fieldName"});
        Object object = session.getAttribute(field);
        if(object == null) {
            return null;
        }

        if((object instanceof String) == false) {
            throw new IllegalArgumentException("Expecting a 'String' session value:" + fieldName + " but found a " + object.getClass().getName());
        }
        String value = (String)object;
        if(value.trim().equals("")) {
            return null;
        }

        try {
            return Integer.valueOf(value.trim());
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Expecting a 'numerical' session value:" + fieldName + " but found: '" + value + "'", e);
        }
    }

//    protected String getFieldCollectionName() {
//        return null;
//    }

//    protected String getFieldCollectionIdField() {
//        return null;
//    }

    /**
     * Populate a fieldCollection entry from session field values as identified by the "fields"
     * parameter with the specific entry being identified by the "idField" value.
     *
     * If the value corresponding to idField is blank then its a new record and it is added to
     * the fieldCollection list, otherwise it is editing an existing record in the fieldCollection
     * list.  The existing record is completely cleared before being overwritten by the new data.
     *
     * If the matching id cannot be found an IllegalArgumentException is thrown (TODO replace
     * this with a proper exception).
     *
     * @throws IllegalArgumentException if the record being edited cannot be found.
     */
    protected void populateFieldCollectionEntry(HttpSession session, String fieldCollectionName, String[] fields, String idField) {
        LOG.trace("Starting AbstractFormController.populateFieldCollectionEntry");
        try {
            Parameters.validateMandatoryArgs(session,  "session");

            if(StringUtils.isEmpty(fieldCollectionName)) {
                LOG.debug("fieldCollectionName empty, nothing to do");
                return;
            }

            // get the fieldCollection list from the session (create if needed)
            List<Map<String, String>> fieldCollection = getFieldCollections(session, fieldCollectionName, true);
            LOG.debug("fieldCollection before = {}", fieldCollection);

            // get ALL field values from the session
            Map<String, String[]> attributes = getSessionStringAttributes(session);
            // and filter them to only these keys we are interested in
            Map<String, String> record = FieldCollection.getFieldValues(attributes, fields);
            LOG.debug("record = {}", record);

            // identify the record id field, and retrieve its value (if it exists)
            String recordId = record.get(idField);

            // if there is no current recordId, then we are editing a new record, so create a new record id
            // and update the record, then add the new record to the fieldCollection list.
            if(StringUtils.isEmpty(recordId)) {
                LOG.debug("Creating new record");
                String newRecordId = getNextIdValue(fieldCollection, idField);

                record.put(idField, newRecordId);
                fieldCollection.add(record);

            // if there is a current recordId, then we are editing an existing record, iterate over the
            // existing records and match the recordId. On match, update the existing record, IF the record
            // ids cannot be matched log and error and throw an exception
            } else {
                boolean existingRecordFound = false;
                if(StringUtils.isEmpty(recordId) == false) {
                    for(Map<String, String> existingRecord: fieldCollection) {
                        String existingRecordId = existingRecord.get(idField);
                        if(recordId.equals(existingRecordId)) {
                            // copy over the existing record (this assumes a complete record in hand)
                            existingRecord.clear();
                            existingRecord.putAll(record);
                            existingRecordFound = true;
                            break;
                        }
                    }
                }

                if(existingRecordFound == false) {
                    LOG.error("Unknown record ID: " + recordId);
                    throw new IllegalArgumentException("Unknown record ID: " + recordId);
                }
            }
            LOG.debug("fieldCollection after = {}", fieldCollection);
            LOG.debug("getFieldCollections('<fieldCollectionName>') = {}", getFieldCollections(session, fieldCollectionName, false));

            // clean up the session by removing the individual field values for the item create/edit screens
            removeFromSession(session, fields);

        } finally {
            LOG.trace("Ending AbstractFormController.populateFieldCollectionEntry");
        }
    }

    protected String showFormEditFieldCollection(HttpServletRequest request, Model model, String fieldCollectionName, String idField) {
        LOG.trace("Started EmploymentDetailsController.showForm");
        try {
            String destination = showFormInternal(request, model);

            // if the ID field is populated then we are editing an existing record
            // and we should load the data, but only if we have not failed validation
            // otherwise it is a new record and everything is already up to date.
            String editIdValue = request.getParameter(idField);
            LOG.debug("editIdValue = {}", editIdValue);
            if(StringUtils.isEmpty(editIdValue) == false && getValidationSummary().hasFormErrors() == false) {
                LOG.debug("populating edit data");
                List<Map<String, String>> records = getFieldCollections(request.getSession(), fieldCollectionName, true);
                Map<String, String> record = FieldCollection.getFieldCollection(records, idField, editIdValue);
                LOG.debug("record = {}", record);
                model.addAllAttributes(record);
            }
            return destination;
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending EmploymentDetailsController.showForm\n");
        }
    }

    protected String urlWithArguments(String url, String argumentName, String argumentValue) {
        if(StringUtils.isEmpty(url) || StringUtils.isEmpty(argumentName)) {
            return url;
        }

        if(url.contains("?")) {
            return url + "&" + argumentName + "=" + argumentValue;
        } else {
            return url + "?" + argumentName + "=" + argumentValue;
        }
    }

    protected String editFieldCollectionRecord(HttpServletRequest request, String idToChange, String fieldCollectionName, String idField, String editingPage) {
        Parameters.validateMandatoryArgs(new Object[]{idToChange, request}, new String[]{"idToChange", "request"});

        getValidationSummary().reset();

        // copy the record values into the edit fields in the session
        List<Map<String, String>> records = getFieldCollections(request.getSession(), fieldCollectionName, true);
        Map<String, String> record = FieldCollection.getFieldCollection(records, idField, idToChange);
        if(record == null) {
            throw new UnknownRecordException("Unknown record id: " + idToChange);
        } else {
            String[] fields = record.keySet().toArray(new String[]{});  // TODO instead of BreakInCareDetailController.FIELDS (?)
            copyMapToSession(record, fields, request.getSession());
        }

        return "redirect:" + editingPage;
    }

    /**
     * Note: this does not validate the form
     * @param fieldCollectionName TODO
     */
    protected String deleteFieldCollectionRecord(String idToDelete, HttpServletRequest request, String fieldCollectionName, String idField) {
        LOG.trace("Starting EmploymentHistoryController.deleteEmployment");
        try {
            Parameters.validateMandatoryArgs(new Object[]{idToDelete, request}, new String[]{"idToDelete", "request"});

            Integer foundIndex = null;
            List<Map<String, String>> fieldCollectionList = getFieldCollections(request.getSession(), fieldCollectionName);
            for(int index = 0; index < fieldCollectionList.size(); index++) {
                Map<String, String> map = fieldCollectionList.get(index);
                if(idToDelete.equals(map.get(idField))) {
                    foundIndex = Integer.valueOf(index);
                    break;
                }
            }

            getValidationSummary().reset();

            if(foundIndex != null) {
                fieldCollectionList.remove(foundIndex.intValue());
            } else {
                throw new UnknownRecordException("Unknown record id: " + idToDelete);
            }

            return "redirect:" + getCurrentPage(request);
        } finally {
            LOG.trace("Ending EmploymentHistoryController.deleteEmployment");
        }
    }

    public static String getRequestValue(String argumentName, HttpServletRequest request) {
        String argumentValue = request.getParameter(argumentName);
        if(StringUtils.isEmpty(argumentValue)) {
            argumentValue = (String)request.getAttribute(argumentName);
        }
        return argumentValue;
    }

    /**
     * Get the session attributes as if they were a request parameter map, i.e.
     * return those that have String or String[] values along with their keys in a map.
     * @return return a map of values or null if session is null
     */
    public static Map<String, String[]> getSessionStringAttributes(HttpSession session) {
        if(session == null) {
            return null;
        }

        Map<String, String[]> map = new HashMap<>();

        List<String> attrNames = Collections.list(session.getAttributeNames());
        for(String attrName: attrNames) {
            Object object = session.getAttribute(attrName);
            if(object == null || object instanceof String[]) {
                map.put(attrName, (String[])object);
            } else if(object instanceof String) {
                map.put(attrName, new String[]{(String)object});
            } else {
                // ignore it
            }
        }

        return map;
    }

    /**
     * @param fields the names of the fields the form uses (from the resource bundle)
     * @param fieldValues all the values from the form being validated
     * @param existingFieldValues the current session values
     */
    protected void validate(String[] fields, Map<String, String[]> fieldValues, Map<String, String[]> existingFieldValues) {
        LOG.trace("Starting AbstractFormController.validate");

        if(fields == null) {
            return;
        }

        try {
            FormValidations validations = new FormValidations(this.getMessageSource(), "pageName not known", fields);
            LOG.info("validations = {}", validations);

            LOG.debug("\tfieldValue Names = {}", fieldValues.keySet());
            for(String fieldValueName: fieldValues.keySet()) {
                LOG.debug("\tfieldValues: '{}' = '{}'", fieldValueName, new LoggingObjectWrapper(fieldValues.get(fieldValueName)));
            }

            LOG.debug("validation summary before = {}", getValidationSummary());
            validations.validate(getValidationSummary(), getMessageSource(), fieldValues, existingFieldValues);
            LOG.debug("validation summary after = {}", getValidationSummary());
        } catch (ParseException e) {
            throw new IllegalStateException("Unable to read validation configuration.", e);
        }

        LOG.trace("Ending AbstractFormController.validate");
    }

    public interface Validation {
        boolean validate(String value);
    }

    public boolean supportsRequest(HttpServletRequest request) {
        LOG.info("Started AbstractFormController.supportsRequest");
        Parameters.validateMandatoryArgs(request, "request");
        try {
            String method = request.getMethod();
            if (HTTP_GET.equalsIgnoreCase(method) == false && HTTP_POST.equalsIgnoreCase(method) == false) {
                LOG.error("Unsupported request method: {}", method);
                return false;
            }

            String path = request.getServletPath();
            LOG.info("method = {}, path = {}", method, path);
            String fieldsKey = path + ".fields";
            String fields = getMessageSource().getMessage(fieldsKey, null, null, Locale.getDefault()); // If there are no fields, but is an entry do we get null or ""?
            if (fields == null) {
                LOG.info("Unsupported request: {}", path);
                return false;
            }
            return true;

        } finally {
            LOG.info("Ending DefaultFormController.supportsRequest");
        }
    }

    /**
     * Data driven request holder
     * @throws NoSuchRequestHandlingMethodException
     */
    public String handleRequest(HttpServletRequest request, HttpServletResponse response, Model model) throws NoSuchRequestHandlingMethodException {
        LOG.info("Started DefaultFormController.handleRequest");
        Parameters.validateMandatoryArgs(request, "request");
        try {
            checkVersionCookie(request);
            addVersionCookie(response);

            String path = request.getServletPath();
            String method = request.getMethod();

            String page = null;
            LOG.info("method = {}, path = {}", method, path);
            if (HTTP_GET.equalsIgnoreCase(method)) {
                return showForm(request, model);
            } else if (HTTP_POST.equalsIgnoreCase(method)) {
                return postForm(request, request.getSession(), model);
            } else {
                LOG.error("Request method {} is not supported in request: {}", method, path);
                throw new NoSuchRequestHandlingMethodException(request);
            }
        } catch (NoSuchRequestHandlingMethodException e) {
            LOG.error("NoSuchRequestHandlingMethodException", e);
            throw e;
        } catch (RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.info("Ending DefaultFormController.handleRequest");
        }
    }

    private final String APPVERSIONCOOKIENAME = "C3Version";

    protected void checkVersionCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (int n = 0; n < (cookies==null ? 0 : cookies.length); n++) {
            if (cookies[n].getName().equals(APPVERSIONCOOKIENAME)) {
                // What to do if incorrect version ?? Let just log the error and write the new cookie version.
                if (!cookies[n].getValue().equals(appVersionNumber())) {
                    LOG.error("ApplicationVersion cookie {}  value:{} does not match expected version:{}", APPVERSIONCOOKIENAME, cookies[n].getValue(), appVersionNumber());
                }
            }
        }
    }

    protected void addVersionCookie(HttpServletResponse response) {
        response.addCookie(new Cookie(APPVERSIONCOOKIENAME, appVersionNumber()));
    }

    private String appVersionNumber() {
        return (applicationVersion.replaceAll("-.*", ""));
    }
}
