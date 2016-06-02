package uk.gov.dwp.carersallowance.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private ValidationSummary validationSummary;
    private List<String>      pageList;

    public AbstractFormController() {
        validationSummary = new ValidationSummary();
        pageList = new ArrayList<>(Arrays.asList(PAGES));
    }

    /************************ START ABSTRACT METHODS **************************/

    public abstract String   getCurrentPage();           // e.g. /allowance/benefits
    public abstract String   getPageTitle();
    public abstract String[] getFields();

    protected abstract void validate(Map<String, String[]> fieldValues, String[] fields);

    /*********************** END ABSTRACT METHODS ******************************/

    public ValidationSummary getValidationSummary() { return validationSummary; }

    public String getPreviousPage() {
        String currentPage = getCurrentPage();
        int index = pageList.indexOf(currentPage);
        if(index == 0) {
            return null;
        }
        String previousPage = pageList.get(index - 1);
        return previousPage;
    }

    public String getNextPage() {
        String currentPage = getCurrentPage();
        int index = pageList.indexOf(currentPage);
        if(index == (pageList.size() -1)) {
            return null;
        }
        String nextPage = pageList.get(index + 1);
        return nextPage;
    }

    protected String showForm(HttpServletRequest request, Model model) {

        LOG.trace("Starting AbstractFormController.showForm");
        LOG.debug("model = {}", model);

        model.addAttribute("previousPage", getPreviousPage());
        model.addAttribute("currentPage", getCurrentPage());
        model.addAttribute("nextPage", getNextPage());
        model.addAttribute("pageTitle", getPageTitle());

        copyFromSessionToModel(request, getFields(), model);

        LOG.trace("Ending AbstractFormController.showForm");
        return getCurrentPage();        // returns the view name
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
                model.addAttribute("errors", getValidationSummary());
                return showForm(request, model);
            }

            return "redirect:" + getNextPage();
        } finally {
            LOG.trace("Ending AbstractFormController.postForm");
        }
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
        LOG.trace("Started AbstractFormController.syncModelToSession");
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
            LOG.trace("Ending AbstractFormController.syncModelToSession");
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

    protected void validateEmailAddress(Map<String, String[]> allfieldValues, String fieldTitle, String fieldName) {
        // TODO
    }

    /**
     * Validate that two fields match (e.g. for email address, passwords etc)
     */
    protected void validateMatchingValues(Map<String, String[]> allfieldValues, String firstTitle, String firstName, String secondTitle, String secondName) {
        // TODO
    }

    protected void validateRegexField(Map<String, String[]> allfieldValues, String fieldTitle, String fieldname, String regex) {
        // TODO
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
}
