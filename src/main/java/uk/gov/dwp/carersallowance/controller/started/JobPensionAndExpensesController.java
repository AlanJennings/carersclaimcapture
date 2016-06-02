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
public class JobPaymentScheduleController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(JobPaymentScheduleController.class);

    private static final String CURRENT_PAGE  = "/employment/last-wage/"; // parameter to indicate which job index
    private static final String PAGE_TITLE    = "Your pay - Your income";

    private static final String[] FIELDS = {"paymentFrequency",
                                            "whenGetPaid",
                                            "lastPaidDate_day",
                                            "lastPaidDate_month",
                                            "lastPaidDate_year",
                                            "grossPay",
                                            "payInclusions",
                                            "sameAmountEachTime"};

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage() {
        // TODO

        return super.getNextPage();
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
        // TODO the dates are from earlier in the claim

        validateMandatoryFields(fieldValues, "How often are you paid?", "paymentFrequency");
        validateMandatoryFields(fieldValues, "When do you get paid?", "whenGetPaid");
        validateMandatoryDateField(fieldValues, "When were you last paid?", "lastPaidDate", new String[]{"lastPaidDate_day", "lastPaidDate_month", "lastPaidDate_year"});
        validateMandatoryFields(fieldValues, "What were you paid in your last wage?", "grossPay");
        validateMandatoryFields(fieldValues, "Do you get the same amount each time?", "sameAmountEachTime");

        LOG.trace("Ending BenefitsController.validate");
    }
}
