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

import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.validations.FormValidationError;
import uk.gov.dwp.carersallowance.validations.ValidationError;

public abstract class AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFormController.class);

    private ValidationSummary validationSummary;

    public AbstractFormController() {
        validationSummary = new ValidationSummary();
    }

    /************************ START ABSTRACT METHODS **************************/

    /**
     * Validate form contents before progressing
     */
    protected abstract void validate(Map<String, Object> fieldValues, String[] fields);

    /**
     * Used for various URLs in Google Analytics in particular
     */
    public abstract String getCurrentPage();           // e.g. /allowance/benefits

    /**
     * The names of the input fields
     */
    public abstract String[] getFields();

    public ValidationSummary getValidationSummary() { return validationSummary; }

    /*********************** END ABSTRACT METHODS ******************************/

    protected String showForm(HttpServletRequest request, Model model) {

        LOG.trace("Starting AbstractFormController.showForm");
        LOG.debug("model = {}", model);

        model.addAttribute("currentPage", getCurrentPage());
        syncSessionToModel(request, getFields(), model);

        LOG.trace("Ending AbstractFormController.showForm");
        return getCurrentPage();        // returns the view name
    }

    protected void syncSessionToModel(HttpServletRequest request, String[] fieldNames, Model model) {
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

    protected void syncModelToSession(Model model, String[] fieldNames, HttpServletRequest request) {
        LOG.trace("Started AbstractFormController.syncModelToSession");
        try {
            Parameters.validateMandatoryArgs(new Object[]{request, model}, new String[]{"request", "model"});
            if(fieldNames == null) {
                return;
            }

            LOG.debug("fieldNames = {}", Arrays.asList(fieldNames));
            HttpSession session = request.getSession();
            Map<String, Object> map = model.asMap();
            for(String fieldName: fieldNames) {
                Object fieldValue = map.get(fieldName);
                LOG.debug("fieldName = {}, fieldValue = {}", fieldName, fieldValue);
                session.setAttribute(fieldName,  fieldValue);
            }
        } finally {
            LOG.trace("Ending AbstractFormController.syncModelToSession");
        }
    }

    protected void validateMandatoryField(Map<String, Object> fieldValues, String fieldName, String fieldTitle) {
        LOG.trace("Started AbstractFormController.validateMandatoryField");
        Object fieldValue = fieldValues.get(fieldName);
        LOG.debug("fieldName = {}, fieldValue={}", fieldName, fieldValue);
        if(StringUtils.isEmpty(fieldValue)) {
            LOG.debug("missing mandatory field: {}", fieldName);
            addFormError(fieldName, fieldTitle + " - " + "You must complete this section");
        }
        LOG.trace("Ending AbstractFormController.validateMandatoryField");
    }

    protected void addFormError(String id, String errorMessage) {
        validationSummary.addFormError(id, errorMessage);
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

        protected void addFormError(String id, String errorMessage) {
            formErrors.add(new FormValidationError(id, errorMessage));
        }

        public void reset() {
            formErrors.clear();
        }

        public boolean hasFormErrors() {
            return !formErrors.isEmpty();
        }

        private boolean containsError(List<ValidationError> errors, String id) {
            if(errors == null || StringUtils.isEmpty(id)) {
                return false;
            }

            for(ValidationError error: errors) {
                if(id.equals(error.getId())) {
                    return true;
                }
            }

            return false;
        }

        public boolean hasError(String id) {
            return containsError(formErrors, id);
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
