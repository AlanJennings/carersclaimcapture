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
public class SelfEmployedDatesController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(SelfEmployedDatesController.class);

    private static final String CURRENT_PAGE  = "/your-income/self-employment/self-employment-dates";
    private static final String PAGE_TITLE    = "Your job Self-employment";

    private static final String[] FIELDS = {"stillSelfEmployed",
                                            "finishThisWork.day",
                                            "finishThisWork.month",
                                            "finishThisWork.year",
                                            "moreThanYearAgo",
                                            "haveAccounts",
                                            "knowTradingYear",
                                            "tradingYearStart.day",
                                            "tradingYearStart.month",
                                            "tradingYearStart.year",
                                            "startThisWork.day",
                                            "startThisWork.month",
                                            "startThisWork.year",
                                            "paidMoney",
                                            "paidMoneyDate.day",
                                            "paidMoneyDate.month",
                                            "paidMoneyDate.year"};

    @Autowired
    public SelfEmployedDatesController(SessionManager sessionManager) {
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

        validateMandatoryFields(fieldValues, "Are you still doing this work?", "stillSelfEmployed");
        if(fieldValue_Equals(fieldValues, "stillSelfEmployed", "no")) {
            validateMandatoryDateField(fieldValues, "When did you start the course?", "finishThisWork", new String[]{"finishThisWork_day", "finishThisWork_month", "finishThisWork_year"});
        }

        validateMandatoryFields(fieldValues, "Did you start this work more than a year ago?", "moreThanYearAgo");
        if(fieldValue_Equals(fieldValues, "moreThanYearAgo", "yes")) {
            validateMandatoryFields(fieldValues, "Do you have accounts?", "haveAccounts");
            if(fieldValue_Equals(fieldValues, "haveAccounts", "no")) {
                validateMandatoryFields(fieldValues, "Do you know your trading year?", "knowTradingYear");
                if(fieldValue_Equals(fieldValues, "knowTradingYear", "yes")) {
                    validateMandatoryDateField(fieldValues, "Trading year start date", "tradingYearStart", new String[]{"tradingYearStart_day", "tradingYearStart_month", "tradingYearStart_year"});
                }
            }
        } else if(fieldValue_Equals(fieldValues, "moreThanYearAgo", "no")) {
            validateMandatoryDateField(fieldValues, "When did you finish this work?", "startThisWork", new String[]{"startThisWork_day", "startThisWork_month", "startThisWork_year"});
        }

        validateMandatoryFields(fieldValues, "Has your self-employed business been paid any money yet?", "paidMoney");
        if(fieldValue_Equals(fieldValues, "paidMoney", "yes")) {
            validateMandatoryDateField(fieldValues, "Date money first received by the business", "paidMoneyDate", new String[]{"paidMoneyDate_day", "paidMoneyDate_month", "paidMoneyDate_year"});
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
