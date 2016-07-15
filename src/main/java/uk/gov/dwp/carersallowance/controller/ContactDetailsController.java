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
public class ContactDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(ContactDetailsController.class);

    private static final String CURRENT_PAGE  = "/about-you/contact-details";
    private static final String PAGE_TITLE    = "Your Contact Details - About you - the carer";

    public static final String CARER_ADDRESS_LINE_ONE           = "carerAddressLineOne";
    public static final String CARER_ADDRESS_LINE_TWO           = "carerAddressLineTwo";
    public static final String CARER_ADDRESS_LINE_THREE         = "carerAddressLineThree";
    public static final String CARER_POSTCODE                   = "carerPostcode";
    public static final String CARER_HOW_WE_CONTACT_YOU         = "carerHowWeContactYou";
    public static final String CARER_HONTACT_YOU_BY_TELEPHONE   = "carerContactYouByTextPhone";
    public static final String CARER_WANTS_EMAIL_CONTACT        = "carerWantsEmailContact";
    public static final String CARER_MAIL                       = "carerMail";
    public static final String CARER_MAIL_CONFIRMATION          = "carerMailConfirmation";

    private static final String[] FIELDS = {CARER_ADDRESS_LINE_ONE,
                                            CARER_ADDRESS_LINE_TWO,
                                            CARER_ADDRESS_LINE_THREE,
                                            CARER_POSTCODE,
                                            CARER_HOW_WE_CONTACT_YOU,
                                            CARER_HONTACT_YOU_BY_TELEPHONE,
                                            CARER_WANTS_EMAIL_CONTACT,
                                            CARER_MAIL,
                                            CARER_MAIL_CONFIRMATION};

    @Autowired
    public ContactDetailsController(SessionManager sessionManager) {
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

        validateAddressFields(fieldValues, "Address", "carerAddress", new String[]{CARER_ADDRESS_LINE_ONE, CARER_ADDRESS_LINE_TWO, CARER_ADDRESS_LINE_THREE});
        validateMandatoryField(fieldValues,CARER_WANTS_EMAIL_CONTACT, "Do you want an email to confirm your application has been received?");

        if(fieldValue_Equals(fieldValues, CARER_WANTS_EMAIL_CONTACT, "yes")) {
            validateMandatoryField(fieldValues,CARER_MAIL, "Your email address");
            validateRegexField(fieldValues,"Your email address", CARER_MAIL, AbstractFormController.ValidationPatterns.EMAIL_REGEX);

            validateMatchingValues(fieldValues,"Your email address", CARER_MAIL, "Confirm your email address", CARER_MAIL_CONFIRMATION, true);
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
