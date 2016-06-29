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

/**
 * A new empty care break has an index of -1;
 * Otherwise the care break index is whatever is appropriate
 * @author drh
 *
 */
@Controller
public class BreakInCareDetailController extends AbstractFormController {
    public static final Logger LOG = LoggerFactory.getLogger(BreakInCareDetailController.class);

    private static final String CURRENT_PAGE  = "/breaks/break";    // this has an argument
    private static final String NEXT_PAGE     = "/breaks/breaks-in-care/update";
    private static final String PARENT_PAGE   = "/breaks/breaks-in-care";
    private static final String PAGE_TITLE    = "Break - About the care you provide";

    public static final String   ID_FIELD = "break_id";
    public static final String[] FIELDS = {ID_FIELD,
                                            "startDate_day",
                                            "startDate_month",
                                            "startDate_year",
                                            "startTime",
                                            "whereCaree",
                                            "whereCareeOtherText",
                                            "whereYou",
                                            "whereYouOtherText",
                                            "hasBreakEnded",
                                            "hasBreakEndedDate_day",
                                            "hasBreakEndedDate_month",
                                            "hasBreakEndedDate_year",
                                            "endTime",
                                            "medicalCareDuringBreak"};


    @Autowired
    public BreakInCareDetailController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        return NEXT_PAGE;
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return PARENT_PAGE;
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
        LOG.trace("Starting BreakInCareDetailController.postForm");
        try {
            return super.postForm(request, session, model);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending BreakInCareDetailController.postForm\n");
        }
    }

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting BenefitsController.validate");

        validateMandatoryDateField(fieldValues, "When did the break start?", "startDate", new String[]{"startDate_day", "startDate_month", "startDate_year"});
        validateMandatoryFields(fieldValues, "Where was the person you care for during the break?", "whereCaree");
        validateMandatoryFields(fieldValues, "Where were you during the break?", "whereYou");
        validateMandatoryFields(fieldValues, "Has this break ended?", "hasBreakEnded");

        if(fieldValue_Equals(fieldValues, "hasBreakEnded", "yes")) {
            validateMandatoryDateField(fieldValues, "When did the break end?", "hasBreakEndedDate", new String[]{"hasBreakEndedDate_day", "hasBreakEndedDate_month", "hasBreakEndedDate_year"});
        }

        if(fieldValue_Equals(fieldValues, "whereCaree", "Somewhere_else")) {
            validateMandatoryFields(fieldValues, "Where was the person you care for during the break?", "whereCareeOtherText");
        }

        if(fieldValue_Equals(fieldValues, "whereYou", "Somewhere_else")) {
            validateMandatoryFields(fieldValues, "Where were you during the break?", "whereYouOtherText");
        }

        validateMandatoryFields(fieldValues, "Did you or the person you care for get any medical treatment or professional care during this time?", "medicalCareDuringBreak");

        LOG.trace("Ending BenefitsController.validate");
    }
}
