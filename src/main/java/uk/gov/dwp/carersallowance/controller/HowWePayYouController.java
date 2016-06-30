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
public class HowWePayYouController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(HowWePayYouController.class);

    private static final String CURRENT_PAGE  = "/pay-details/how-we-pay-you";
    private static final String PAGE_TITLE    = "How would you like to get paid? - Your bank details";

    private static final String[] FIELDS = {"likeToPay",
                                            "accountHolderName",
                                            "bankFullName",
                                            "sortcode_1",
                                            "sortcode_2",
                                            "sortcode_3",
                                            "accountNumber",
                                            "rollOrReferenceNumber",
                                            "paymentFrequency"};

    @Autowired
    public HowWePayYouController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return super.getPreviousPage(request, YourIncomeController.getIncomePageList(request.getSession()));
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

        validateMandatoryField(fieldValues, "likeToPay", "Do you have a bank account?");
        if(fieldValue_Equals(fieldValues, "likeToPay", "yes")) {
            validateMandatoryField(fieldValues, "accountHolderName", "Account holder name");
            validateMandatoryField(fieldValues, "bankFullName", "Name of bank or building society");
            validateMandatoryFieldGroup(fieldValues, "sortcode", "Sort code", true, "sortcode_1", "sortcode_2", "sortcode_3");
            validateMandatoryField(fieldValues, "accountNumber", "Account number");
        }

        validateMandatoryField(fieldValues, "paymentFrequency", "How often do you want to be paid Carer's Allowance?");

        LOG.trace("Ending BenefitsController.validate");
    }
}
