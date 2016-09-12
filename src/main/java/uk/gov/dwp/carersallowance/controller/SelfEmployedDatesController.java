package uk.gov.dwp.carersallowance.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class SelfEmployedDatesController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(SelfEmployedDatesController.class);

    private static final String PAGE_NAME     = "page.self-employment-dates";
    private static final String CURRENT_PAGE  = "/your-income/self-employment/self-employment-dates";

    @Autowired
    public SelfEmployedDatesController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return super.getPreviousPage(request, YourIncomeController.getIncomePageList(request.getSession()));
    }

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        return super.getNextPage(request, YourIncomeController.getIncomePageList(request.getSession()));
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

        validateMandatoryField(fieldValues, "stillSelfEmployed");
        if(fieldValue_Equals(fieldValues, "stillSelfEmployed", "no")) {
            validateMandatoryDateField(fieldValues, "finishThisWork");
        }

        validateMandatoryField(fieldValues, "moreThanYearAgo");
        if(fieldValue_Equals(fieldValues, "moreThanYearAgo", "yes")) {
            validateMandatoryField(fieldValues, "haveAccounts");
            if(fieldValue_Equals(fieldValues, "haveAccounts", "no")) {
                validateMandatoryField(fieldValues, "knowTradingYear");
                if(fieldValue_Equals(fieldValues, "knowTradingYear", "yes")) {
                    validateMandatoryDateField(fieldValues, "tradingYearStart");
                }
            }
        } else if(fieldValue_Equals(fieldValues, "moreThanYearAgo", "no")) {
            validateMandatoryDateField(fieldValues, "startThisWork");

            validateMandatoryField(fieldValues, "paidMoney");
            if(fieldValue_Equals(fieldValues, "paidMoney", "yes")) {
                validateMandatoryDateField(fieldValues, "paidMoneyDate");
            }
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
