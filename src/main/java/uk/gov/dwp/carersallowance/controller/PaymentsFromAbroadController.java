package uk.gov.dwp.carersallowance.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class PaymentsFromAbroadController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentsFromAbroadController.class);

    private static final String CURRENT_PAGE  = "/about-you/other-eea-state-or-switzerland";
    private static final String PAGE_TITLE    = "Payments from abroad - Nationality and where you've lived";

    private static final String[] FIELDS = {"eeaGuardQuestion",
                                            "benefitsFromEEADetails",
                                            "benefitsFromEEADetails_field",
                                            "workingForEEADetails",
                                            "workingForEEADetails_field"};

    @Autowired
    public PaymentsFromAbroadController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String[] getFields() {
        return FIELDS;
    }

    @Override
    public String getPageTitle() {
        return PAGE_TITLE;
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
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting BenefitsController.validate");

        validateMandatoryField(fieldValues, "eeaGuardQuestion", "Have you or any of your close family worked abroad or been paid benefits from outside the United Kingdom since your claim date?");

        if(fieldValue_Equals(fieldValues, "eeaGuardQuestion", "yes")) {
            validateMandatoryField(fieldValues, "benefitsFromEEADetails", "Have you or your close family claimed or been paid any benefits or pensions from any of these countries since your claim date?");
            validateMandatoryField(fieldValues, "workingForEEADetails", "Have you or your close family worked or paid national insurance in any of these countries since your claim date?");
        }

        if(fieldValue_Equals(fieldValues, "benefitsFromEEADetails", "yes")) {
            validateMandatoryField(fieldValues, "benefitsFromEEADetails_field", "Details of the pension or benefit");
        }

        if(fieldValue_Equals(fieldValues, "workingForEEADetails", "yes")) {
            validateMandatoryField(fieldValues, "workingForEEADetails_field", "Details of the overseas work or national insurance paid abroad");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
