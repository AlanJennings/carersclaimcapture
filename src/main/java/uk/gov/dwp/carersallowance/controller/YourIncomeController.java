package uk.gov.dwp.carersallowance.controller;

import java.util.ArrayList;
import java.util.List;
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
import uk.gov.dwp.carersallowance.utils.Parameters;

@Controller
public class YourIncomeController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(YourIncomeController.class);

    private static final String CURRENT_PAGE  = "/your-income/your-income";
    private static final String PAGE_TITLE    = "Your income";

    private static final String YOUR_INCOME_DIRECTPAY                        = "yourIncome_directpay";
    private static final String YOUR_INCOME_FOSTERING                        = "yourIncome_fostering";
    private static final String YOUR_INCOME_PATMATADOPPAY                    = "yourIncome_patmatadoppay";
    private static final String YOUR_INCOME_SICKPAY                          = "yourIncome_sickpay";
    private static final String BEEN_SELF_EMPLOYED_SINCE1_WEEK_BEFORE_CLAIM  = "beenSelfEmployedSince1WeekBeforeClaim";
    private static final String HAD_OTHER_INCOME_SINCE_CLAIM_DATE            = "hadOtherIncomeSinceClaimDate";
    private static final String BEEN_EMPLOYED_SINCE6_MONTHS_BEFORE_CLAIM     = "beenEmployedSince6MonthsBeforeClaim";
    private static final String YOUR_INCOME_ANYOTHER                         = "yourIncome_anyother";

    private static final String[] FIELDS = {BEEN_EMPLOYED_SINCE6_MONTHS_BEFORE_CLAIM,
                                            BEEN_SELF_EMPLOYED_SINCE1_WEEK_BEFORE_CLAIM,
                                            HAD_OTHER_INCOME_SINCE_CLAIM_DATE,
                                            YOUR_INCOME_SICKPAY,
                                            YOUR_INCOME_PATMATADOPPAY,
                                            YOUR_INCOME_FOSTERING,
                                            YOUR_INCOME_DIRECTPAY,
                                            YOUR_INCOME_ANYOTHER};

    @Autowired
    public YourIncomeController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public static List<String> getIncomePageList(HttpSession session) {
        Parameters.validateMandatoryArgs(session, "session");
        List<String> pages = new ArrayList<>();
        pages.add(CURRENT_PAGE);

        if(getYesNoBooleanFieldValue(session, BEEN_EMPLOYED_SINCE6_MONTHS_BEFORE_CLAIM) == Boolean.TRUE) {
            pages.add("/your-income/employment/been-employed");
        }

        if(getYesNoBooleanFieldValue(session, BEEN_SELF_EMPLOYED_SINCE1_WEEK_BEFORE_CLAIM) == Boolean.TRUE) {
            pages.add("/your-income/self-employment/self-employment-dates");
            pages.add("/your-income/self-employment/pensions-and-expenses");
        }

        if(getYesNoBooleanFieldValue(session, YOUR_INCOME_SICKPAY) == Boolean.TRUE) {
            pages.add("/your-income/statutory-sick-pay");
        }

        if(getYesNoBooleanFieldValue(session, YOUR_INCOME_PATMATADOPPAY) == Boolean.TRUE) {
            pages.add("/your-income/smp-spa-sap");
        }

        if(getYesNoBooleanFieldValue(session, YOUR_INCOME_FOSTERING) == Boolean.TRUE) {
            pages.add("/your-income/fostering-allowance");
        }

        if(getYesNoBooleanFieldValue(session, YOUR_INCOME_DIRECTPAY) == Boolean.TRUE) {
            pages.add("/your-income/direct-payment");
        }

        if(getYesNoBooleanFieldValue(session, YOUR_INCOME_ANYOTHER) == Boolean.TRUE) {
            pages.add("/your-income/other-income");
        }

        pages.add("/pay-details/how-we-pay-you");

        LOG.debug("employment pages = {}", pages);
        return pages;
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
// TODO the dates are from earlier in the claim
        validateMandatoryField(fieldValues, BEEN_EMPLOYED_SINCE6_MONTHS_BEFORE_CLAIM, "Have you been in employment since 1 December 2015?");
        validateMandatoryField(fieldValues, BEEN_SELF_EMPLOYED_SINCE1_WEEK_BEFORE_CLAIM, "Have you been in self-employment since 25 May 2016?");
        validateMandatoryField(fieldValues, HAD_OTHER_INCOME_SINCE_CLAIM_DATE, "Have you had other income since 17 June 2016?");
        if(fieldValue_Equals(fieldValues, HAD_OTHER_INCOME_SINCE_CLAIM_DATE, "yes")) {
            validateMandatoryFieldGroupAnyField(fieldValues,
                                                "otherIncomeGroup",
                                                "Have you had other income since 17 June 2016?",
                                                YOUR_INCOME_SICKPAY,
                                                YOUR_INCOME_PATMATADOPPAY,
                                                YOUR_INCOME_FOSTERING,
                                                YOUR_INCOME_DIRECTPAY,
                                                YOUR_INCOME_ANYOTHER);
        }
        LOG.trace("Ending BenefitsController.validate");
    }
}
