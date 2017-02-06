package uk.gov.dwp.carersallowance.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import uk.gov.dwp.carersallowance.session.*;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.*;
import uk.gov.dwp.carersallowance.validations.FormValidations;
import uk.gov.dwp.carersallowance.validations.ValidationSummary;

// TODO SuppressWarnings temporary, all complaints largely true. Remove them later and re-analyze
public class AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFormController.class);

    private static final String SESSION_TRANSFORMATIONS_KEY = "%s.transformations";

    protected static final String HTTP_POST = "POST";
    protected static final String HTTP_GET = "GET";

    protected final MessageSource  messageSource;
    protected final SessionManager sessionManager;
    private final TransformationManager transformationManager;
    private final PageOrder pageOrdering;
    private ValidationSummary validationSummary;

    public AbstractFormController(final SessionManager sessionManager,
                                  final MessageSource messageSource,
                                  final TransformationManager transformationManager,
                                  final PageOrder pageOrdering) {
        this.sessionManager = sessionManager;
        this.messageSource = messageSource;
        this.transformationManager = transformationManager;
        this.pageOrdering = pageOrdering;
        this.validationSummary = new ValidationSummary();
    }

    protected String getPageName() {
        return null;
    }

    public MessageSource     getMessageSource()             { return messageSource; }
    public Session           getSession(String sessionId)   { return sessionManager.getSession(sessionId); }
    public ValidationSummary getValidationSummary()         { return validationSummary; }

    public String[] getSharedFields() {
        return null;
    }

    public String[] getReadOnlyFields() {
        return new String[]{"dateOfClaim_day", "dateOfClaim_month", "dateOfClaim_year", "careeFirstName", "careeSurname", "language", "isOriginGB", "carerFirstName", "carerSurname", "employerName"};
    }

    public String getForm(HttpServletRequest request, Model model) {
        return getFormInternal(request, model);
    }

    private String getFormInternal(HttpServletRequest request, Model model) {
        LOG.trace("Starting AbstractFormController.getFormInternal");
        try {
            LOG.debug("model = {}", model);

            String path = request.getServletPath();
            LOG.info("path = {}", path);

            final String currentPage = PropertyUtils.getCurrentPage(request);
            final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
            model.addAttribute("previousPage", pageOrdering.getPreviousPage(currentPage, session));   // not sure if we can use this as the request data is not available yet
            model.addAttribute("currentPage", currentPage);

            // if we copy everything then we don't need a static list
            // in which case the fields used are defined by the jsp, which is far less visible
            // it would also remove the distinction between shared & readonly fields and ordinary fields
            // so we probably could verify if a field name was duplicated.
            // So probably best to stick to a list of fields, but make it data driven (in messages.properties)
            String[] fields = FieldCollection.getFields(messageSource, currentPage);
            String[] additionalFields = FieldCollection.getFields(messageSource, currentPage, ".additional.fields.for.model");
            if(fields == null || fields.length == 0) {
                fields = FieldCollection.getFields(messageSource, getPageName());
                additionalFields = FieldCollection.getFields(messageSource, getPageName(), ".additional.fields.for.model");
            }
            copyFromSessionToModel(session, fields, model);
            copyFromSessionToModel(session, additionalFields, model);
            copyFromSessionToModel(session, getSharedFields(), model);
            copyFromSessionToModel(session, getReadOnlyFields(), model);

            return currentPage;        // returns the view name
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AbstractFormController.getFormInternal");
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
    public String postForm(final HttpServletRequest request, final Model model) {

        LOG.trace("Starting AbstractFormController.postForm");
        try {
            LOG.debug("request.getParameterMap() = {}", request.getParameterMap());

            final String pageName = request.getParameter("pageName");
            final String[] fields = FieldCollection.getFields(messageSource, pageName);
            final String currentPage = PropertyUtils.getCurrentPage(request);

            getValidationSummary().reset();

            final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));

            Map<String, String[]> existingFieldValues = CollectionUtils.getAllFieldValues(session);

            validate(fields, request.getParameterMap(), existingFieldValues);

            if (hasErrors()) {
                return processPageErrors(request, fields, model);
            }

            copyFromRequestToSession(session, request, fields);
            copyFromRequestToSession(session, request, getSharedFields());

            finalizePostForm(request);

            // add the fieldCollection id field to the url (if its populated)
            // we need to add the # to the end of the redirect url to stop the browser from inheriting the hash url
            // from the previous page ('cos its a redirect), see https://www.w3.org/TR/cuap#uri
            // we needed to do a redirect so that we show the page we are on, rather than the page we submitted to
            // and we needed to have submitted to the previous page, so we can validate and update the form, which
            // is part of the previous screen and not the one we are going to.  We don't know if the previous page
            // had a hash location, as the hash location is never submitted to the server
            // there are various things we *could* do with javascript, but none of them will work in the no-javascript journey
            String nextPage = "redirect:" + pageOrdering.pageProcessing(currentPage, session) + "#";

            sessionManager.saveSession(session);

            LOG.debug("next page = {}", nextPage);

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
    protected void copyFromSessionToModel(final Session session, final String[] fieldNames, final Model model) {
        LOG.trace("Started AbstractFormController.copyFromSessionToModel");
        try {
            Parameters.validateMandatoryArgs(new Object[]{session, model}, new String[]{"session", "model"});
            if (fieldNames == null) {
                return;
            }

            LOG.debug("fieldNames = {}", Arrays.asList(fieldNames));
            for (String fieldName: fieldNames) {
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
                model.addAttribute(fieldName, fieldValue);
            }
        } finally {
            LOG.trace("Ending AbstractFormController.syncRequestToModel");
        }
    }

    /**
     * Copy the named values from the model to the session
     */
    protected void copyFromRequestToSession(Session session, HttpServletRequest request, String[] fieldNames) {
        LOG.trace("Started AbstractFormController.copyFromRequestToSession");
        try {
            Parameters.validateMandatoryArgs(request, "request");
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
                LOG.info("Adding session fieldName = {}, fieldValue = {}", fieldName, fieldValue);
                session.setAttribute(fieldName, transformationManager.getTransformedValue(fieldName, fieldValue, SESSION_TRANSFORMATIONS_KEY, messageSource));
            }
        } finally {
            LOG.trace("Ending AbstractFormController.copyFromRequestToSession");
        }
    }

    protected boolean hasErrors() {
        return validationSummary.hasFormErrors();
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
            validations.validate(getValidationSummary(), getMessageSource(), transformationManager, fieldValues, existingFieldValues);
            LOG.debug("validation summary after = {}", getValidationSummary());
        } catch (ParseException e) {
            throw new IllegalStateException("Unable to read validation configuration.", e);
        }

        LOG.trace("Ending AbstractFormController.validate");
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
            LOG.info("Ending AbstractFormController.supportsRequest");
        }
    }

    /**
     * Data driven request holder
     * @throws NoSuchRequestHandlingMethodException
     */
    public String handleRequest(final HttpServletRequest request, final HttpServletResponse response, final Model model) throws NoSuchRequestHandlingMethodException {
        LOG.info("Started AbstractFormController.handleRequest");
        Parameters.validateMandatoryArgs(request, "request");
        try {
            String path = request.getServletPath();
            String method = request.getMethod();

            LOG.info("method = {}, path = {}", method, path);
            if (HTTP_GET.equalsIgnoreCase(method)) {
                return getForm(request, model);
            } else if (HTTP_POST.equalsIgnoreCase(method)) {
                if (request.getParameter("changeSubFormRecord") != null || request.getParameter("deleteSubFormRecord") != null) {
                    return changeDeleteForm(request, model, request.getParameter("changeSubFormRecord"), request.getParameter("deleteSubFormRecord"));
                } else {
                    return postForm(request, model);
                }
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
            LOG.info("Ending AbstractFormController.handleRequest");
        }
    }

    private String changeDeleteForm(final HttpServletRequest request, final Model model, final String idToChange, final String idToDelete) {
        LOG.trace("Starting AbstractFormController.changeDeleteForm");
        try {
            LOG.debug("request.getParameterMap() = {}", request.getParameterMap());

            final String pageName = request.getParameter("pageName");
            final String currentPage = PropertyUtils.getCurrentPage(request);
            final String[] fields = FieldCollection.getFields(messageSource, pageName);

            getValidationSummary().reset();

            final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));

            String changeDeletePage = pageOrdering.deleteChangeRecord(idToDelete, idToChange, currentPage, session, getValidationSummary());
            if (hasErrors()) {
                return processPageErrors(request, fields, model);
            }
            sessionManager.saveSession(session);
            return changeDeletePage + "#";
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AbstractFormController.postForm");
        }
    }

    private String processPageErrors(final HttpServletRequest request, final String[] fields, final Model model) {
        LOG.info("there are validation errors, re-showing form");
        String form = getForm(request, model);

        // add the values of the current form to the model as well as the session values
        copyFromRequestToModel(request, fields, model);
        copyFromRequestToModel(request, getSharedFields(), model);
        model.addAttribute("validationErrors", getValidationSummary());

        return form;
    }
}
