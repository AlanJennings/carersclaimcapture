package uk.gov.dwp.carersallowance.controller_retired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

public class EmploymentPensionAndExpensesController extends AbstractFormController {
    private static final String CURRENT_PAGE  = "/your-income/employment/about-expenses"; // parameter to indicate which job index
    private static final String PREVIOUS_PAGE = "/your-income/employment/last-wage";
    private static final String NEXT_PAGE     = "/your-income/employment/been-employed/update";

    private static final String[] READONLY_FIELDS = {"employerName"};

    @Autowired
    public EmploymentPensionAndExpensesController(SessionManager sessionManager, MessageSource messageSource) {
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

//    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
//        LOG.trace("Starting BenefitsController.validate");
//        // TODO the dates are from earlier in the claim
//
//        validateMandatoryField(fieldValues, "payPensionScheme");
//        validateMandatoryField(fieldValues, "payForThings");
//        validateMandatoryField(fieldValues, "haveExpensesForJob");
//        if(fieldValue_Equals(fieldValues, "payPensionScheme", "yes")) {
//            validateMandatoryField(fieldValues, "payPensionSchemeText");
//        }
//        if(fieldValue_Equals(fieldValues, "payForThings", "yes")) {
//            validateMandatoryField(fieldValues, "payForThingsText");
//        }
//        if(fieldValue_Equals(fieldValues, "haveExpensesForJob", "yes")) {
//            validateMandatoryField(fieldValues, "haveExpensesForJobText");
//        }
//
//        LOG.trace("Ending BenefitsController.validate");
//    }
}
