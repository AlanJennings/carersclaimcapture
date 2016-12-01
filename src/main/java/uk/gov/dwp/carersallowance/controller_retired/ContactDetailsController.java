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
public class ContactDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(ContactDetailsController.class);

    private static final String PAGE_NAME     = "page.contactDetails";
    private static final String CURRENT_PAGE  = "/about-you/contact-details";

    public static final String CARER_ADDRESS_LINE_ONE           = "carerAddressLineOne";
    public static final String CARER_ADDRESS_LINE_TWO           = "carerAddressLineTwo";
    public static final String CARER_ADDRESS_LINE_THREE         = "carerAddressLineThree";
    public static final String CARER_POSTCODE                   = "carerPostcode";
    public static final String CARER_HOW_WE_CONTACT_YOU         = "carerHowWeContactYou";
    public static final String CARER_HONTACT_YOU_BY_TELEPHONE   = "carerContactYouByTextPhone";
    public static final String CARER_WANTS_EMAIL_CONTACT        = "carerWantsEmailContact";
    public static final String CARER_MAIL                       = "carerMail";
    public static final String CARER_MAIL_CONFIRMATION          = "carerMailConfirmation";

// FIELDS = carerAddressLineOne, carerAddressLineTwo, carerAddressLineThree, carerPostcode, carerHowWeContactYou, carerContactYouByTextPhone, carerWantsEmailContact, carerMail, carerMailConfirmation

    @Autowired
    public ContactDetailsController(SessionManager sessionManager, MessageSource messageSource) {
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
    protected void validate(String[] fields, Map<String, String[]> fieldValues, Map<String, String[]> allFieldValues) {
        LOG.trace("Starting BenefitsController.validate");

        validateAddressFields(fieldValues, "Address", "carerAddress", new String[]{CARER_ADDRESS_LINE_ONE, CARER_ADDRESS_LINE_TWO, CARER_ADDRESS_LINE_THREE});
        validateMandatoryField(fieldValues,CARER_WANTS_EMAIL_CONTACT);

        if(fieldValue_Equals(fieldValues, CARER_WANTS_EMAIL_CONTACT, "yes")) {
            validateMandatoryField(fieldValues,CARER_MAIL);
            validateRegexField(fieldValues,"Your email address", CARER_MAIL, ValidationPatterns.EMAIL_REGEX);

            validateMatchingValues(fieldValues,"Your email address", CARER_MAIL, "Confirm your email address", CARER_MAIL_CONFIRMATION, true);
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
