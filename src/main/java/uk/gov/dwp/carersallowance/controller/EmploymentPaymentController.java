package uk.gov.dwp.carersallowance.controller;

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

import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class EmploymentPaymentController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EmploymentPaymentController.class);

    private static final String PAGE_NAME     = "page.last-wage";
    private static final String CURRENT_PAGE  = "/your-income/employment/last-wage"; // parameter to indicate which job index
    private static final String PREVIOUS_PAGE = "/your-income/employment/job-details";
    private static final String NEXT_PAGE     = "/your-income/employment/about-expenses";

    private static final String[] READONLY_FIELDS = {"employerName"};

    @Autowired
    public EmploymentPaymentController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return PREVIOUS_PAGE;
    }

    @Override
    public String getCurrentPage(HttpServletRequest request) {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        return NEXT_PAGE;
    }

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

    @Override
    public String[] getReadOnlyFields() {
        return READONLY_FIELDS;
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

        validateMandatoryField(fieldValues, "employmentPaymentFrequency");
        validateMandatoryField(fieldValues, "employmentWhenGetPaid");
        validateMandatoryDateField(fieldValues, "employmentemploymentLastPaidDate");
        validateMandatoryField(fieldValues, "employmentGrossPay");
        validateMandatoryField(fieldValues, "employmentSameAmountEachTime");


        LOG.trace("Ending BenefitsController.validate");
    }
}
