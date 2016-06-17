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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.gov.dwp.carersallowance.session.SessionManager;

//TODO
@Controller
public class NationalityController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(NationalityController.class);

    private static final String CURRENT_PAGE  = "/about-you/nationality-and-residency";
    private static final String PAGE_TITLE    = "About your nationality and where you live - Nationality and where you've lived";

    private static final String[] FIELDS = {"nationality",
                                            "actualnationality",
                                            "alwaysLivedInUK",
                                            "liveInUKNow",
                                            "arrivedInUK",
                                            "arrivedInUKDate_day",
                                            "arrivedInUKDate_month",
                                            "arrivedInUKDate_year",
                                            "arrivedInUKFrom",
                                            "trip52Weeks",
                                            "tripDetails"};

    @Autowired
    public NationalityController(SessionManager sessionManager) {
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

        validateMandatoryFields(fieldValues, "What is your nationality?", "nationality");
        validateMandatoryFields(fieldValues, "Have you always lived in England, Scotland or Wales?", "alwaysLivedInUK");
        validateMandatoryFields(fieldValues, "Have you been away from England, Scotland or Wales for more than 52 weeks in the 3 years before your claim date?","trip52Weeks");

        if(fieldValue_Equals(fieldValues, "nationality", "Another_nationality")) {
            validateMandatoryFields(fieldValues, "Your nationality", "actualnationality");
        }

        if(fieldValue_Equals(fieldValues, "alwaysLivedInUK", "no")) {
            validateMandatoryFields(fieldValues, "Do you live in England, Scotland or Wales now?", "liveInUKNow");

            if(fieldValue_Equals(fieldValues, "liveInUKNow", "yes")) {
                validateMandatoryFields(fieldValues, "When did you arrive in England, Scotland or Wales?", "arrivedInUK");

                if(fieldValue_Equals(fieldValues, "arrivedInUK", "less")) {
                    validateMandatoryDateField(fieldValues, "Date arrived", "arrivedInUKDate", new String[]{"arrivedInUKDate_day", "arrivedInUKDate_month", "arrivedInUKDate_year"});
                    validateMandatoryFields(fieldValues, "Which country did you live in?", "arrivedInUKFrom");
                }
            }
        }

        if(fieldValue_Equals(fieldValues, "trip52Weeks", "yes")) {
            validateMandatoryFields(fieldValues, "Tell us about where you've been.", "tripDetails");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
