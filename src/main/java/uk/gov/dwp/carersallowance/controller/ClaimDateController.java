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

//TODO
@Controller
public class ClaimDateController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimDateController.class);

    private static final String PAGE_NAME     = "";
    private static final String CURRENT_PAGE  = "/your-claim-date/claim-date";

// FIELDS = dateOfClaim_year, dateOfClaim_month, dateOfClaim_day, beforeClaimCaring, beforeClaimCaringDate_year, beforeClaimCaringDate_month, beforeClaimCaringDate_day

    @Autowired
    public ClaimDateController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    @Override
    public String getCurrentPage(HttpServletRequest request) {
        return CURRENT_PAGE;
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

    @Override
    public void finalizePostForm(HttpServletRequest request) {
        saveFormattedDate(request.getSession(), "dateOfClaim", "dd MMMMMMMMMM yyyy", "dateOfClaim_year", "dateOfClaim_month", "dateOfClaim_day");
    }

    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting YourDetailsController.validate");

        validateMandatoryDateField(fieldValues, "dateOfClaim");
        validateMandatoryField(fieldValues, "beforeClaimCaring");
        if(fieldValue_Equals(fieldValues, "beforeClaimCaring", "yes")) {
            validateMandatoryDateField(fieldValues, "beforeClaimCaringDate");
        }

        LOG.trace("Ending YourDetailsController.validate");
    }
}
