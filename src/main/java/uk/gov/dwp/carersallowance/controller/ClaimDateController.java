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

//TODO
@Controller
public class ClaimDateController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimDateController.class);

    private static final String CURRENT_PAGE  = "/your-claim-date/claim-date";
    private static final String PAGE_TITLE    = "Claim date - When do you want Carer's Allowance to start?";

    private static final String[] FIELDS = {"dateOfClaim_year",
                                            "dateOfClaim_month",
                                            "dateOfClaim_day",
                                            "beforeClaimCaring",
                                            "beforeClaimCaringDate_year",
                                            "beforeClaimCaringDate_month",
                                            "beforeClaimCaringDate_day"};

    @Autowired
    public ClaimDateController(SessionManager sessionManager) {
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

    @Override
    public void finalizePostForm(HttpServletRequest request) {
        saveFormattedDate(request.getSession(), "dateOfClaim", "dd MMMMMMMMMM yyyy", "dateOfClaim_year", "dateOfClaim_month", "dateOfClaim_day");
    }

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting BenefitsController.validate");

        validateMandatoryDateField(fieldValues, "dateOfClaim", "Claim date");
        validateMandatoryField(fieldValues, "beforeClaimCaring", "Were you caring for the person for more than 35 hours a week before this date?");
        if(fieldValue_Equals(fieldValues, "beforeClaimCaring", "yes")) {
            validateMandatoryDateField(fieldValues, "beforeClaimCaringDate", "When did you begin caring?");
        }
        LOG.trace("Ending BenefitsController.validate");
    }
}
