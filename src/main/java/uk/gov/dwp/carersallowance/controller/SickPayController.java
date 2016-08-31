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
public class SickPayController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(SickPayController.class);

    private static final String CURRENT_PAGE  = "/your-income/statutory-sick-pay";
    private static final String PAGE_TITLE    = "Your income Statutory Sick Pay";

    private static final String[] FIELDS = {"sickPayStillBeingPaidThisPay",
                                            "sickPayWhenDidYouLastGetPaid_day",
                                            "sickPayWhenDidYouLastGetPaid_month",
                                            "sickPayWhenDidYouLastGetPaid_year",
                                            "sickPayWhoPaidYouThisPay",
                                            "sickPayAmountOfThisPay",
                                            "sickPayHowOftenPaidThisPay",
                                            "sickPayHowOftenPaidThisPayOther"};

    @Autowired
    public SickPayController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return super.getPreviousPage(request, YourIncomeController.getIncomePageList(request.getSession()));
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        return super.getNextPage(request, YourIncomeController.getIncomePageList(request.getSession()));
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

        validateMandatoryField(fieldValues, "sickPayStillBeingPaidThisPay", "Are you still being paid Statutory Sick Pay?");
        if(fieldValue_Equals(fieldValues, "sickPayStillBeingPaidThisPay", "no")) {
            validateMandatoryDateField(fieldValues, "sickPayWhenDidYouLastGetPaid", "When were you last paid?");
        }

        validateMandatoryField(fieldValues, "sickPayWhoPaidYouThisPay", "Who paid you Statutory Sick Pay?");
        validateMandatoryField(fieldValues, "sickPayAmountOfThisPay", "Amount paid");

        validateMandatoryField(fieldValues, "sickPayHowOftenPaidThisPay", "How often are you paid?");
        if(fieldValue_Equals(fieldValues, "sickPayHowOftenPaidThisPay", "Other")) {
            validateMandatoryField(fieldValues, "sickPayHowOftenPaidThisPayOther", "How often are you paid?");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
