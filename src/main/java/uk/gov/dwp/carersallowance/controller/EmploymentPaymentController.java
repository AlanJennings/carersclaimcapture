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
public class EmploymentPaymentController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EmploymentPaymentController.class);

    private static final String CURRENT_PAGE  = "/your-income/employment/last-wage"; // parameter to indicate which job index
    private static final String PAGE_TITLE    = "Your pay - Your income";
    private static final String PREVIOUS_PAGE = "/your-income/employment/job-details";
    private static final String NEXT_PAGE     = "/your-income/employment/about-expenses";

    private static final String[] READONLY_FIELDS = {"employerName"};

    public static final String[] FIELDS = {"employmentPaymentFrequency",
                                           "employmentPaymentFrequencyOtherText",
                                           "employmentwhenGetPaid",
                                           "employmentLastPaidDate_day",
                                           "employmentLastPaidDate_month",
                                           "employmentLastPaidDate_year",
                                           "employmentGrossPay",
                                           "employmentPayInclusions",
                                           "employmentSameAmountEachTime"};

    @Autowired
    public EmploymentPaymentController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return PREVIOUS_PAGE;
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        return NEXT_PAGE;
    }

    @Override
    public String[] getFields() {
        return FIELDS;
    }

    @Override
    public String[] getReadOnlyFields() {
        return READONLY_FIELDS;
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
        // TODO the dates are from earlier in the claim

        validateMandatoryField(fieldValues, "employmentPaymentFrequency", "How often are you paid?");
        validateMandatoryField(fieldValues, "employmentWhenGetPaid", "When do you get paid?");
        validateMandatoryDateField(fieldValues, "employmentemploymentLastPaidDate", "When were you last paid?");
        validateMandatoryField(fieldValues, "employmentGrossPay", "What were you paid in your last wage?");
        validateMandatoryField(fieldValues, "employmentSameAmountEachTime", "Do you get the same amount each time?");


        LOG.trace("Ending BenefitsController.validate");
    }
}
