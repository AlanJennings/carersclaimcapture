package uk.gov.dwp.carersallowance.controller.started;

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
public class OtherStatutoryPaymentsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(OtherStatutoryPaymentsController.class);

    private static final String CURRENT_PAGE  = "/your-income/smp-spa-sap";
    private static final String PAGE_TITLE    = "Your income Statutory Pay";

    private static final String[] FIELDS = {"paymentTypesForThisPay",
                                            "stillBeingPaidThisPay",
                                            "whenDidYouLastGetPaid.day",
                                            "whenDidYouLastGetPaid.month",
                                            "whenDidYouLastGetPaid.year",
                                            "whoPaidYouThisPay",
                                            "amountOfThisPay",
                                            "howOftenPaidThisPay",
                                            "howOftenPaidThisPayOther"};

    @Autowired
    public OtherStatutoryPaymentsController(SessionManager sessionManager) {
        super(sessionManager);
    }

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

        validateMandatoryField(fieldValues, "paymentTypesForThisPay", "Which are you paid?");
        validateMandatoryField(fieldValues, "stillBeingPaidThisPay", "Are you still being paid this?");
        if(fieldValue_Equals(fieldValues, "beenInEducationSinceClaimDate", "yes")) {
            validateMandatoryDateField(fieldValues, "When were you last paid?", "whenDidYouLastGetPaid", new String[]{"whenDidYouLastGetPaid_day", "whenDidYouLastGetPaid_month", "whenDidYouLastGetPaid_year"});
        }

        validateMandatoryField(fieldValues, "whoPaidYouThisPay", "Who paid you this?");
        validateMandatoryField(fieldValues, "amountOfThisPay", "Amount paid");
        validateMandatoryField(fieldValues, "howOftenPaidThisPay", "How often are you paid?");
        if(fieldValue_Equals(fieldValues, "howOftenPaidThisPay", "Other")) {
            validateMandatoryField(fieldValues, "howOftenPaidThisPayOther", "How often are you paid?");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
