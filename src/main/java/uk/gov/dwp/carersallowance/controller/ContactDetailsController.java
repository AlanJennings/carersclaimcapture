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

    private static final String[] FIELDS = {"address_lineOne",
                                            "address_lineTwo",
                                            "address_lineThree",
                                            "postcode",
                                            "howWeContactYou",
                                            "contactYouByTelephone",
                                            "wantsEmailContact",
                                            "mail",
                                            "mailConfirmation"};

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

        validateAddressFields(fieldValues, "Address", "address", new String[]{"address_lineOne", "address_lineTwo", "address_lineThree"});
        validateMandatoryField(fieldValues,"wantsEmailContact", "Do you want an email to confirm your application has been received?");

        if(fieldValue_Equals(fieldValues, "wantsEmailContact", "yes")) {
            validateMandatoryField(fieldValues,"mail", "Your email address");
            validateRegexField(fieldValues,"Your email address", "mail", AbstractFormController.ValidationPatterns.EMAIL_REGEX);

            validateMatchingValues(fieldValues,"Your email address", "mail", "Confirm your email address", "mailConfirmation", true);
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
