package uk.gov.dwp.carersallowance.validations;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import uk.gov.dwp.carersallowance.jsp.Functions;

public class ValidationSummary {
    private static final Logger LOG = LoggerFactory.getLogger(ValidationSummary.class);

    private List<ValidationError> formErrors;

    public ValidationSummary() {
        formErrors = new ArrayList<>();
    }

    public List<ValidationError> getFormErrors()  { return formErrors; }

    public void addFormError(String id, String displayName, String errorMessage) {
        FormValidationError error = new FormValidationError(id, displayName, errorMessage);
        LOG.debug("Adding form error {}", error);
        formErrors.add(error);
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
            if(Functions.encrypt(id).equals(error.getId())) {
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