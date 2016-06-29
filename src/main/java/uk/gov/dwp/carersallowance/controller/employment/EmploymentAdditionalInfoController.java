package uk.gov.dwp.carersallowance.controller.employment;

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
public class EmploymentAdditionalInfoController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EmploymentAdditionalInfoController.class);

    private static final String CURRENT_PAGE  = "/your-income/employment/additional-info";
    private static final String PREVIOUS_PAGE = "?";
    private static final String NEXT_PAGE     = "/pay-details/how-we-pay-you";
    private static final String PAGE_TITLE    = "Additional Information  Your income";

    public static final String[] FIELDS = {"empAdditionalInfo",
                                           "empAdditionalInfoText"};

    @Autowired
    public EmploymentAdditionalInfoController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return PREVIOUS_PAGE;
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        return NEXT_PAGE;
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

        validateMandatoryField(fieldValues, "empAdditionalInfo", "Do you want to add anything about your work?");
        if(fieldValue_Equals(fieldValues, "empAdditionalInfo", "yes")) {
            validateMandatoryField(fieldValues, "empAdditionalInfoText", "Do you want to add anything about your work?");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
