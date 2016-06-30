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
import uk.gov.dwp.carersallowance.controller.YourIncomeController;
import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class EmploymentPensionAndExpensesController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EmploymentPensionAndExpensesController.class);

    private static final String PAGE_TITLE            = "Pension and expenses - Your income";
    private static final String CURRENT_PAGE          = "/your-income/employment/about-expenses"; // parameter to indicate which job index
    private static final String PREVIOUS_PAGE         = "/your-income/employment/last-wage";
    private static final String NEXT_PAGE             = "/your-income/employment/been-employed/update";

    private static final String   FIELD_COLLECTION_NAME = EmploymentHistoryController.FIELD_COLLECTION_NAME;
    public static final String   ID_FIELD              = EmploymentHistoryController.ID_FIELD;
    public static final String[] FIELDS = {ID_FIELD,
                                           "employerName",
                                           "payPensionScheme",
                                           "payPensionSchemeText",
                                           "payForThings",
                                           "payForThingsText",
                                           "haveExpensesForJob",
                                           "haveExpensesForJobText"};

    @Autowired
    public EmploymentPensionAndExpensesController(SessionManager sessionManager) {
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

        validateMandatoryField(fieldValues, "payPensionScheme", "Do you pay into a pension?");
        validateMandatoryField(fieldValues, "payForThings", "Do you pay for things you need to do your job?");
        validateMandatoryField(fieldValues, "haveExpensesForJob", "Do you have any care costs because of this work?");
        if(fieldValue_Equals(fieldValues, "payPensionScheme", "yes")) {
            validateMandatoryField(fieldValues, "payPensionSchemeText", "Give details of each pension you pay into, including how much and how often you pay.");
        }
        if(fieldValue_Equals(fieldValues, "payForThings", "yes")) {
            validateMandatoryField(fieldValues, "payForThingsText", "Give details of what you need to buy, why you need it and how much it costs.");
        }
        if(fieldValue_Equals(fieldValues, "haveExpensesForJob", "yes")) {
            validateMandatoryField(fieldValues, "haveExpensesForJobText", "Give details of who you pay and what it costs.");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
