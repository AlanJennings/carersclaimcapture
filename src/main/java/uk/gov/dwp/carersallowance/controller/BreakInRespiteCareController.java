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
 */
@Controller
public class BreakInRespiteCareController extends AbstractFormController {
    public static final Logger LOG = LoggerFactory.getLogger(BreakInRespiteCareController.class);

    private static final String CURRENT_PAGE  = "/breaks/break-in-respite-care";    // this has an argument
    private static final String NEXT_PAGE     = "/breaks/breaks-in-care/update";
    private static final String PARENT_PAGE   = "/breaks/breaks-in-care";
    private static final String PAGE_TITLE    = "Break - About the care you provide";

    public static final String[] FIELDS = {"respiteBreakWhoInHospital",
                                           "respiteBreakCarerHospitalStartDate_day",
                                           "respiteBreakCarerHospitalStartDate_month",
                                           "respiteBreakCarerHospitalStartDate_year",
                                           "respiteBreakCarerHospitalEndDate_day",
                                           "respiteBreakCarerHospitalEndDate_month",
                                           "respiteBreakCarerHospitalEndDate_year",
                                           "respiteBreakCarerInHospitalCareeLocation",
                                           "respiteBreakCarerInHospitalCareeLocationText",
                                           "respiteBreakCareeHospitalStartDate_day",
                                           "respiteBreakCareeHospitalStartDate_month",
                                           "respiteBreakCareeHospitalStartDate_year",
                                           "respiteBreakCareeHospitalStayEnded",
                                           "respiteBreakCareeHospitalEndDate_day",
                                           "respiteBreakCareeHospitalEndDate_month",
                                           "respiteBreakCareeHospitalEndDate_year",
                                           "respiteBreakCareeHospitalCarerStillCaring",
                                           "respiteBreakWeeksNotCaring" };

    @Autowired
    public BreakInRespiteCareController(SessionManager sessionManager) {
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

//        TODO
//        validateMandatoryDateField(fieldValues, "When did the break start?", "respiteBreakBreakStartDate", new String[]{"respiteBreakBreakStartDate_day", "respiteBreakBreakStartDate_month", "respiteBreakBreakStartDate_year"});
//        validateMandatoryField(fieldValues, "respiteBreakBreakWhereCaree", "Where was the person you care for during the break?");
//        validateMandatoryField(fieldValues, "respiteBreakBreakWhereYou", "Where were you during the break?");
//        validateMandatoryField(fieldValues, "respiteBreakBreakHasBreakEnded", "Has this break ended?");
//
//        if(fieldValue_Equals(fieldValues, "respiteBreakBreakHasBreakEnded", "yes")) {
//            validateMandatoryDateField(fieldValues, "When did the break end?", "respiteBreakBreakHasBreakEndedDate", new String[]{"respiteBreakBreakHasBreakEndedDate_day", "respiteBreakBreakHasBreakEndedDate_month", "respiteBreakBreakHasBreakEndedDate_year"});
//        }
//
//        if(fieldValue_Equals(fieldValues, "respiteBreakBreakWhereCaree", "somewhere else")) {
//            validateMandatoryField(fieldValues, "respiteBreakBreakWhereCareeOtherText", "Where was the person you care for during the break?");
//        }
//
//        if(fieldValue_Equals(fieldValues, "respiteBreakBreakWhereYou", "somewhere else")) {
//            validateMandatoryField(fieldValues, "respiteBreakBreakWhereYouOtherText", "Where were you during the break?");
//        }
//
//        validateMandatoryField(fieldValues, "respiteBreakBreakMedicalCareDuringBreak", "Did you or the person you care for get any medical treatment or professional care during this time?");

        LOG.trace("Ending BenefitsController.validate");
    }
}
