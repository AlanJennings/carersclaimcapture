package uk.gov.dwp.carersallowance.controller_retired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

public class ClaimDateController extends AbstractFormController {

    @Autowired
    public ClaimDateController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    public String showForm(HttpServletRequest request, Model model) {
        return super.showForm(request, model);
    }

    public String postForm(HttpServletRequest request, HttpSession session, Model model) {
        return super.postForm(request, session, model);
    }

//    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
//        LOG.trace("Starting YourDetailsController.validate");
//
//        validateMandatoryDateField(fieldValues, "dateOfClaim");
//        validateMandatoryField(fieldValues, "beforeClaimCaring");
//        if(fieldValue_Equals(fieldValues, "beforeClaimCaring", "yes")) {
//            validateMandatoryDateField(fieldValues, "beforeClaimCaringDate");
//        }
//
//        LOG.trace("Ending YourDetailsController.validate");
//    }
}
