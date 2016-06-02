package uk.gov.dwp.carersallowance.controller.started;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;

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
    public String postForm(HttpServletRequest request, HttpSession session, Model model, RedirectAttributes redirectAttrs) {
        return super.postForm(request, session, model, redirectAttrs);
    }

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting BenefitsController.validate");

        validateMandatoryFields(fieldValues, "Have you or any of your close family worked abroad or been paid benefits from outside the United Kingdom since your claim date?", "eeaGuardQuestion");

        if(fieldValue_Equals(fieldValues, "eeaGuardQuestion", "yes")) {
            validateMandatoryFields(fieldValues, "Have you or your close family claimed or been paid any benefits or pensions from any of these countries since your claim date?", "benefitsFromEEADetails");
            validateMandatoryFields(fieldValues, "Have you or your close family worked or paid national insurance in any of these countries since your claim date?", "workingForEEADetails");
        }

        if(fieldValue_Equals(fieldValues, "benefitsFromEEADetails", "yes")) {
            validateMandatoryFields(fieldValues, "Details of the pension or benefit", "benefitsFromEEADetails_field");
        }

        if(fieldValue_Equals(fieldValues, "workingForEEADetails", "yes")) {
            validateMandatoryFields(fieldValues, "Details of the overseas work or national insurance paid abroad", "workingForEEADetails_field");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
