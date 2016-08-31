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
public class PartnerDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(PartnerDetailsController.class);

    private static final String CURRENT_PAGE  = "/your-partner/personal-details";
    private static final String PAGE_TITLE    = "Partner details - About your partner";

    private static final String[] FIELDS = {"hadPartnerSinceClaimDate",
                                            "partnerTitle",
                                            "partnerFirstName",
                                            "partnerMiddleName",
                                            "partnerSurname",
                                            "partnerOtherNames",
                                            "partnerNationalInsuranceNumber",
                                            "partnerDateOfBirth_day",
                                            "partnerDateOfBirth_month",
                                            "partnerDateOfBirth_year",
                                            "partnerNationality",
                                            "partnerSeparated",
                                            "isPartnerPersonYouCareFor",
                                            };

    @Autowired
    public PartnerDetailsController(SessionManager sessionManager) {
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
        LOG.trace("Starting PartnerDetailsController.validate");

        validateMandatoryField(fieldValues, "hadPartnerSinceClaimDate", "Have you lived with a partner at any time since your claim date?");
        if(fieldValue_Equals(fieldValues, "hadPartnerSinceClaimDate", "yes")) {
            validateMandatoryField(fieldValues, "partnerTitle", "Title");
            validateMandatoryField(fieldValues, "partnerFirstName", "First name");
            validateMandatoryField(fieldValues, "partnerSurname", "Last name");
            validateMandatoryField(fieldValues, "partnerNatioNalInsuranceNumber", "National Insurance number");
            validateRegexField(fieldValues,"National Insurance number", "partnernatioNalInsuranceNumber", AbstractFormController.ValidationPatterns.NINO_REGEX);
            validateMandatoryDateField(fieldValues, "partnerDateOfBirth", "Date of Birth");
            validateMandatoryField(fieldValues, "partnerNationality", "Nationality");
            validateMandatoryField(fieldValues, "partnerSeparated", "Have you separated since your claim date?");
            validateMandatoryField(fieldValues, "isPartnerPersonYouCareFor", "Is this the person you care for?");
        }

        LOG.trace("Ending PartnerDetailsController.validate");
    }
}
