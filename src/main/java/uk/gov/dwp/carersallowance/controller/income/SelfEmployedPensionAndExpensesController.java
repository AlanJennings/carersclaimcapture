package uk.gov.dwp.carersallowance.controller.income;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;

import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;

@Controller
public class SelfEmployedPensionAndExpensesController extends AbstractFormController {
    private static final String CURRENT_PAGE  = "/your-income/self-employment/pensions-and-expenses";

    @Autowired
    public SelfEmployedPensionAndExpensesController(final SessionManager sessionManager, final MessageSource messageSource, final TransformationManager transformationManager) {
        super(sessionManager, messageSource, transformationManager);
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return super.getPreviousPage(request, YourIncomeController.getIncomePageList(sessionManager.getSession(sessionManager.getSessionIdFromCookie(request))));
    }

    @Override
    public String getCurrentPage(HttpServletRequest request) {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        return super.getNextPage(request, YourIncomeController.getIncomePageList(sessionManager.getSession(sessionManager.getSessionIdFromCookie(request))));
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(HttpServletRequest request, Model model) {
        return super.getForm(request, model);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, Model model) {
        return super.postForm(request, model);
    }

//    /**
//     * Might use BindingResult, and spring Validator, not sure yet
//     * don't want to perform type conversion prior to controller as we have no control
//     * over the (rather poor) reporting behaviour
//     * @return
//     */
//    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
//        LOG.trace("Starting BenefitsController.validate");
//
//        validateMandatoryField(fieldValues, "selfEmployedPayPensionScheme");
//        if(fieldValue_Equals(fieldValues, "selfEmployedPayPensionScheme", C3Constants.YES)) {
//            validateMandatoryField(fieldValues, "selfEmployedPayPensionSchemeText");
//        }
//
//        validateMandatoryField(fieldValues, "selfEmployedHaveExpensesForJob");
//        if(fieldValue_Equals(fieldValues, "selfEmployedHaveExpensesForJob", C3Constants.YES)) {
//            validateMandatoryField(fieldValues, "selfEmployedHaveExpensesForJobText");
//        }
//
//        LOG.trace("Ending BenefitsController.validate");
//    }
}
