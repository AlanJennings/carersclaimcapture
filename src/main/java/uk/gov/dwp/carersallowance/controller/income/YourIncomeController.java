package uk.gov.dwp.carersallowance.controller.income;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.utils.Parameters;

@Controller
public class YourIncomeController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(YourIncomeController.class);

    private static final String CURRENT_PAGE  = "/your-income/your-income";

    private static final String YOUR_INCOME_DIRECTPAY                        = "yourIncome_directpay";
    private static final String YOUR_INCOME_FOSTERING                        = "yourIncome_fostering";
    private static final String YOUR_INCOME_PATMATADOPPAY                    = "yourIncome_patmatadoppay";
    private static final String YOUR_INCOME_SICKPAY                          = "yourIncome_sickpay";
    private static final String BEEN_SELF_EMPLOYED_SINCE1_WEEK_BEFORE_CLAIM  = "beenSelfEmployedSince1WeekBeforeClaim";
    private static final String BEEN_EMPLOYED_SINCE6_MONTHS_BEFORE_CLAIM     = "beenEmployedSince6MonthsBeforeClaim";
    private static final String YOUR_INCOME_ANYOTHER                         = "yourIncome_anyother";

    @Autowired
    public YourIncomeController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
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
    public String getNextPage(HttpServletRequest request) {
        return super.getNextPage(request, YourIncomeController.getIncomePageList(request.getSession()));
    }
}
