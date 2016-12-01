package uk.gov.dwp.carersallowance.controller_retired;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

//@Controller
public class PaymentsFromAbroadController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentsFromAbroadController.class);

    private static final String PAGE_NAME     = "page.other-eea-state-or-switzerland";
    private static final String CURRENT_PAGE  = "/about-you/other-eea-state-or-switzerland";

// FIELDS = eeaGuardQuestion, benefitsFromEEADetails, benefitsFromEEADetails_field, workingForEEADetails, workingForEEADetails_field
    @Autowired
    public PaymentsFromAbroadController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

    @Override
    public String getCurrentPage(HttpServletRequest request) {
        return CURRENT_PAGE;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String showForm(HttpServletRequest request, Model model) {
        return super.showForm(request, model);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, HttpSession session, Model model) {
        return super.postForm(request, session, model);
    }

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(String[] fields, Map<String, String[]> fieldValues, Map<String, String[]> allFieldValues) {
        LOG.trace("Starting BenefitsController.validate");

        validateMandatoryField(fieldValues, "eeaGuardQuestion");

        if(fieldValue_Equals(fieldValues, "eeaGuardQuestion", "yes")) {
            validateMandatoryField(fieldValues, "benefitsFromEEADetails");
            validateMandatoryField(fieldValues, "workingForEEADetails");
        }

        if(fieldValue_Equals(fieldValues, "benefitsFromEEADetails", "yes")) {
            validateMandatoryField(fieldValues, "benefitsFromEEADetails_field");
        }

        if(fieldValue_Equals(fieldValues, "workingForEEADetails", "yes")) {
            validateMandatoryField(fieldValues, "workingForEEADetails_field");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
