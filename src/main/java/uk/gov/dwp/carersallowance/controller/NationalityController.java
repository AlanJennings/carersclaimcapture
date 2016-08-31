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

        validateMandatoryField(fieldValues, "nationality", "What is your nationality?");
        validateMandatoryField(fieldValues, "alwaysLivedInUK", "Have you always lived in England, Scotland or Wales?");
        validateMandatoryField(fieldValues, "trip52Weeks","Have you been away from England, Scotland or Wales for more than 52 weeks in the 3 years before your claim date?");

        if(fieldValue_Equals(fieldValues, "nationality", "Another nationality")) {
            validateMandatoryField(fieldValues, "actualnationality", "Your nationality");
        }

        if(fieldValue_Equals(fieldValues, "alwaysLivedInUK", "no")) {
            validateMandatoryField(fieldValues, "liveInUKNow", "Do you live in England, Scotland or Wales now?");

            if(fieldValue_Equals(fieldValues, "liveInUKNow", "yes")) {
                validateMandatoryField(fieldValues, "arrivedInUK", "When did you arrive in England, Scotland or Wales?");

                if(fieldValue_Equals(fieldValues, "arrivedInUK", "less")) {
                    validateMandatoryDateField(fieldValues, "arrivedInUKDate", "Date arrived");
                    validateMandatoryField(fieldValues, "arrivedInUKFrom", "Which country did you live in?");
                }
            }
        }

        if(fieldValue_Equals(fieldValues, "trip52Weeks", "yes")) {
            validateMandatoryField(fieldValues, "tripDetails", "Tell us about where you've been.");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
