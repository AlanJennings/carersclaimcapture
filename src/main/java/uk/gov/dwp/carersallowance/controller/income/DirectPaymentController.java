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

@Controller
public class DirectPaymentController extends AbstractFormController {
    private static final String CURRENT_PAGE  = "/your-income/direct-payment";


    @Autowired
    public DirectPaymentController(final SessionManager sessionManager, final MessageSource messageSource) {
        super(sessionManager, messageSource);
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
    public String showForm(HttpServletRequest request, Model model) {
        return super.showForm(request, model);
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
//        validateMandatoryField(fieldValues, "directPaymentStillBeingPaidThisPay");
//        if(fieldValue_Equals(fieldValues, "directPaymentStillBeingPaidThisPay", "no")) {
//            validateMandatoryDateField(fieldValues, "directPaymentWhenDidYouLastGetPaid");
//        }
//
//        validateMandatoryField(fieldValues, "directPaymentWhoPaidYouThisPay");
//        validateMandatoryField(fieldValues, "directPaymentAmountOfThisPay");
//
//        validateMandatoryField(fieldValues, "directPaymentHowOftenPaidThisPay");
//        if(fieldValue_Equals(fieldValues, "directPaymentHowOftenPaidThisPay", "Other")) {
//            validateMandatoryField(fieldValues, "directPaymentHowOftenPaidThisPayOther");
//        }
//
//        LOG.trace("Ending BenefitsController.validate");
//    }
}
