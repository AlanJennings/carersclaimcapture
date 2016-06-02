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
public class SickPayController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(SickPayController.class);

    private static final String CURRENT_PAGE  = "/about-you/marital-status";
    private static final String PAGE_TITLE    = "Your income Statutory Sick Pay";

    private static final String[] FIELDS = {"stillBeingPaidThisPay",
                                            "whenDidYouLastGetPaid.day",
                                            "whenDidYouLastGetPaid.month",
                                            "whenDidYouLastGetPaid.year",
                                            "whoPaidYouThisPay",
                                            "amountOfThisPay",
                                            "howOftenPaidThisPay",
                                            "howOftenPaidThisPayOther"};

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

        validateMandatoryFields(fieldValues, "Are you still being paid Statutory Sick Pay?", "stillBeingPaidThisPay");
        if(fieldValue_Equals(fieldValues, "stillBeingPaidThisPay", "no")) {
            validateMandatoryDateField(fieldValues, "When were you last paid?", "whenDidYouLastGetPaid", new String[]{"whenDidYouLastGetPaid_day", "whenDidYouLastGetPaid_month", "whenDidYouLastGetPaid_year"});
        }

        validateMandatoryFields(fieldValues, "Who paid you Statutory Sick Pay?", "whoPaidYouThisPay");
        validateMandatoryFields(fieldValues, "Amount paid", "amountOfThisPay");

        validateMandatoryFields(fieldValues, "How often are you paid?", "howOftenPaidThisPay");
        if(fieldValue_Equals(fieldValues, "howOftenPaidThisPay", "Other")) {
            validateMandatoryFields(fieldValues, "How often are you paid?", "howOftenPaidThisPayOther");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
