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
public class SelfEmployedDatesController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(SelfEmployedDatesController.class);

    private static final String CURRENT_PAGE  = "/your-income/self-employment/self-employment-dates";
    private static final String PAGE_TITLE    = "Your job Self-employment";

    private static final String[] FIELDS = {"stillSelfEmployed",
                                            "finishThisWork_day",
                                            "finishThisWork_month",
                                            "finishThisWork_year",
                                            "moreThanYearAgo",
                                            "haveAccounts",
                                            "knowTradingYear",
                                            "tradingYearStart_day",
                                            "tradingYearStart_month",
                                            "tradingYearStart_year",
                                            "startThisWork_day",
                                            "startThisWork_month",
                                            "startThisWork_year",
                                            "paidMoney",
                                            "paidMoneyDate_day",
                                            "paidMoneyDate_month",
                                            "paidMoneyDate_year"};

    @Autowired
    public SelfEmployedDatesController(SessionManager sessionManager) {
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

        validateMandatoryField(fieldValues, "stillSelfEmployed", "Are you still doing this work?");
        if(fieldValue_Equals(fieldValues, "stillSelfEmployed", "no")) {
            validateMandatoryDateField(fieldValues, "When did you start the course?", "finishThisWork", new String[]{"finishThisWork_day", "finishThisWork_month", "finishThisWork_year"});
        }

        validateMandatoryField(fieldValues, "moreThanYearAgo", "Did you start this work more than a year ago?");
        if(fieldValue_Equals(fieldValues, "moreThanYearAgo", "yes")) {
            validateMandatoryField(fieldValues, "haveAccounts", "Do you have accounts?");
            if(fieldValue_Equals(fieldValues, "haveAccounts", "no")) {
                validateMandatoryField(fieldValues, "knowTradingYear", "Do you know your trading year?");
                if(fieldValue_Equals(fieldValues, "knowTradingYear", "yes")) {
                    validateMandatoryDateField(fieldValues, "Trading year start date", "tradingYearStart", new String[]{"tradingYearStart_day", "tradingYearStart_month", "tradingYearStart_year"});
                }
            }
        } else if(fieldValue_Equals(fieldValues, "moreThanYearAgo", "no")) {
            validateMandatoryDateField(fieldValues, "When did you finish this work?", "startThisWork", new String[]{"startThisWork_day", "startThisWork_month", "startThisWork_year"});

            validateMandatoryField(fieldValues, "paidMoney", "Has your self-employed business been paid any money yet?");
            if(fieldValue_Equals(fieldValues, "paidMoney", "yes")) {
                validateMandatoryDateField(fieldValues, "Date money first received by the business", "paidMoneyDate", new String[]{"paidMoneyDate_day", "paidMoneyDate_month", "paidMoneyDate_year"});
            }
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
