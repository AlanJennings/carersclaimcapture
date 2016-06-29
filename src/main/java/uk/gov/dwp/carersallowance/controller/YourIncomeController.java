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
public class YourIncomeController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(YourIncomeController.class);

    private static final String CURRENT_PAGE  = "/your-income/your-income";
    private static final String PAGE_TITLE    = "Your income";

    private static final String[] FIELDS = {"beenEmployedSince6MonthsBeforeClaim",
                                            "beenSelfEmployedSince1WeekBeforeClaim",
                                            "hadOtherIncomeSinceClaimDate",
                                            "yourIncome_sickpay",
                                            "yourIncome_patmatadoppay",
                                            "yourIncome_fostering",
                                            "yourIncome_directpay",
                                            "yourIncome_anyother"};


    @Autowired
    public YourIncomeController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        // TODO this depends on the answers to beenEmployedSince6MonthsBeforeClaim
        //    & beenSelfEmployedSince1WeekBeforeClaim
        //    & yourIncome

        return super.getNextPage(request);
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
// TODO the dates are from earlier in the claim
        validateMandatoryField(fieldValues, "beenEmployedSince6MonthsBeforeClaim", "Have you been in employment since 1 December 2015?");
        validateMandatoryField(fieldValues, "beenSelfEmployedSince1WeekBeforeClaim", "Have you been in self-employment since 25 May 2016?");
        validateMandatoryField(fieldValues, "hadOtherIncomeSinceClaimDate", "Have you had other income since 17 June 2016?");

        LOG.trace("Ending BenefitsController.validate");
    }
}
