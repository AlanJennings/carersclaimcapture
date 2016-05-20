package uk.gov.dwp.carersallowance.controller.thirdparty;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;

//TODO
@Controller
public class ThirdPartyController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(ThirdPartyController.class);

    private static final String PREVIOUS_PAGE = "/disclaimer/disclaimer";
    private static final String CURRENT_PAGE  = "/third-party/third-party";
    private static final String NEXT_PAGE     = "/your-claim-date/claim-date";
    private static final String PAGE_TITLE    = "Third Party - Are you applying for Carer's Allowance for yourself?";

    private static final String[] FIELDS = {"thirdParty", "nameAndOrganisation", };

    @Override
    public String getPreviousPage() {
        return PREVIOUS_PAGE;
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage() {
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
    public String postForm(HttpServletRequest request, HttpSession session, Model model, RedirectAttributes redirectAttrs) {
        return super.postForm(request, session, model, redirectAttrs);
    }

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting BenefitsController.validate");

        validateMandatoryField(fieldValues, "thirdParty", "Are you the carer?");
        if(fieldValue_Equals(fieldValues, "thirdParty", "noCarer")) {
            validateMandatoryField(fieldValues, "nameAndOrganisation", "Your name and organisation");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
