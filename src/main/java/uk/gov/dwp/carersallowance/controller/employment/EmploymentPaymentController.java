package uk.gov.dwp.carersallowance.controller.employment;

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

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class EmploymentPaymentController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EmploymentPaymentController.class);

    private static final String CURRENT_PAGE  = "/your-income/employment/last-wage"; // parameter to indicate which job index
    private static final String PAGE_TITLE    = "Your pay - Your income";
    private static final String PREVIOUS_PAGE = "/your-income/employment/job-details";
    private static final String NEXT_PAGE     = "/your-income/employment/about-expenses";

    private static final String   FIELD_COLLECTION_NAME = EmploymentHistoryController.FIELD_COLLECTION_NAME;
    public static final String   ID_FIELD              = EmploymentHistoryController.ID_FIELD;
    public static final String[] FIELDS = {ID_FIELD,
                                           "employerName",
                                           "paymentFrequency",
                                           "whenGetPaid",
                                           "lastPaidDate_day",
                                           "lastPaidDate_month",
                                           "lastPaidDate_year",
                                           "grossPay",
                                           "payInclusions",
                                           "sameAmountEachTime"};

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
    public String getPageTitle() {
        return PAGE_TITLE;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String showForm(HttpServletRequest request, Model model) {
        return showFormEditFieldCollection(request, model, FIELD_COLLECTION_NAME, ID_FIELD);    // not sure if we need this or not
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

        validateMandatoryField(fieldValues, "paymentFrequency", "How often are you paid?");
        validateMandatoryField(fieldValues, "whenGetPaid", "When do you get paid?");
        validateMandatoryDateField(fieldValues, "When were you last paid?", "lastPaidDate", new String[]{"lastPaidDate_day", "lastPaidDate_month", "lastPaidDate_year"});
        validateMandatoryField(fieldValues, "grossPay", "What were you paid in your last wage?");
        validateMandatoryField(fieldValues, "sameAmountEachTime", "Do you get the same amount each time?");

        LOG.trace("Ending BenefitsController.validate");
    }
}
