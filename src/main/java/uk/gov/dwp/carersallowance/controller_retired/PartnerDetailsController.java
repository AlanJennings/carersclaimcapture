package uk.gov.dwp.carersallowance.controller_retired;

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

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.validations.ValidationPatterns;

//@Controller
public class PartnerDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(PartnerDetailsController.class);

    private static final String PAGE_NAME     = "page.personal-details";
    private static final String CURRENT_PAGE  = "/your-partner/personal-details";

    @Autowired
    public PartnerDetailsController(SessionManager sessionManager, MessageSource messageSource) {
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

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting PartnerDetailsController.validate");

        validateMandatoryField(fieldValues, "hadPartnerSinceClaimDate");
        if(fieldValue_Equals(fieldValues, "hadPartnerSinceClaimDate", "yes")) {
            validateMandatoryField(fieldValues, "partnerTitle");
            validateMandatoryField(fieldValues, "partnerFirstName");
            validateMandatoryField(fieldValues, "partnerSurname");
            validateMandatoryField(fieldValues, "partnerNatioNalInsuranceNumber");
            validateRegexField(fieldValues,"National Insurance number", "partnernatioNalInsuranceNumber", ValidationPatterns.NINO_REGEX);
            validateMandatoryDateField(fieldValues, "partnerDateOfBirth");
            validateMandatoryField(fieldValues, "partnerNationality");
            validateMandatoryField(fieldValues, "partnerSeparated");
            validateMandatoryField(fieldValues, "isPartnerPersonYouCareFor");
        }

        LOG.trace("Ending PartnerDetailsController.validate");
    }
}
