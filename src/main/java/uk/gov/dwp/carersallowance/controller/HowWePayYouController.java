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
public class HowWePayYouController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(HowWePayYouController.class);

    private static final String PAGE_NAME     = "page.how-we-pay-you";
    private static final String CURRENT_PAGE  = "/pay-details/how-we-pay-you";

    @Autowired
    public HowWePayYouController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    @Override
    public String getCurrentPage(HttpServletRequest request) {
        return CURRENT_PAGE;
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return super.getPreviousPage(request, YourIncomeController.getIncomePageList(request.getSession()));
    }

    @Override
    protected String getPageName() {
        return PAGE_NAME;
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

        validateMandatoryField(fieldValues, "likeToPay");
        if(fieldValue_Equals(fieldValues, "likeToPay", "yes")) {
            validateMandatoryField(fieldValues, "accountHolderName");
            validateMandatoryField(fieldValues, "bankFullName");
            validateMandatoryFieldGroupAllFields(fieldValues, "sortcode", "Sort code", "sortcode_1", "sortcode_2", "sortcode_3");
            validateMandatoryField(fieldValues, "accountNumber");
        }

        validateMandatoryField(fieldValues, "paymentFrequency");

        LOG.trace("Ending BenefitsController.validate");
    }
}
