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
public class BreakInHospitalController extends AbstractFormController {
    public static final Logger LOG = LoggerFactory.getLogger(BreakInHospitalController.class);

    private static final String CURRENT_PAGE  = "/breaks/break-in-hospital";    // this has an argument
    private static final String NEXT_PAGE     = "/breaks/breaks-in-care/update";
    private static final String PARENT_PAGE   = "/breaks/breaks-in-care";
    private static final String PAGE_TITLE    = "Break - About the care you provide";

    public static final String[] FIELDS = {"hospitalBreakWhoInHospital",
                                           "hospitalBreakCarerHospitalStartDate_day",
                                           "hospitalBreakCarerHospitalStartDate_month",
                                           "hospitalBreakCarerHospitalStartDate_year",
                                           "hospitalBreakCarerHospitalStayEnded",
                                           "hospitalBreakCarerHospitalEndDate_day",
                                           "hospitalBreakCarerHospitalEndDate_month",
                                           "hospitalBreakCarerHospitalEndDate_year",
                                           "hospitalBreakCarerInHospitalCareeLocation",
                                           "hospitalBreakCarerInHospitalCareeLocationText",
                                           "hospitalBreakCareeHospitalStartDate_day",
                                           "hospitalBreakCareeHospitalStartDate_month",
                                           "hospitalBreakCareeHospitalStartDate_year",
                                           "hospitalBreakCareeHospitalStayEnded",
                                           "hospitalBreakCareeHospitalEndDate_day",
                                           "hospitalBreakCareeHospitalEndDate_month",
                                           "hospitalBreakCareeHospitalEndDate_year",
                                           "hospitalBreakCareeHospitalCarerStillCaring",
                                           "hospitalBreakWeeksNotCaring" };

    @Autowired
    public BreakInHospitalController(SessionManager sessionManager) {
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

        validateMandatoryField(fieldValues, "hospitalBreakWhoInHospital", "Who was in hospital?");

        if(fieldValue_Equals(fieldValues, "hospitalBreakWhoInHospital", "Carer")) {
            validateMandatoryDateField(fieldValues, "When were you admitted?", "hospitalBreakCarerHospitalStartDate", new String[]{"hospitalBreakCarerHospitalStartDate_day", "hospitalBreakCarerHospitalStartDate_month", "hospitalBreakCarerHospitalStartDate_year"});
            validateMandatoryField(fieldValues, "hospitalBreakCarerHospitalStayEnded", "Has the hospital stay ended?");

            if(fieldValue_Equals(fieldValues, "hospitalBreakCarerHospitalStayEnded", "yes")) {
                validateMandatoryDateField(fieldValues, "When did your hospital stay end?", "hospitalBreakCarerHospitalEndDate", new String[]{"hospitalBreakCarerHospitalEndDate_day", "hospitalBreakCarerHospitalEndDate_month", "hospitalBreakCarerHospitalEndDate_year"});
                validateMandatoryField(fieldValues, "hospitalBreakCarerInHospitalCareeLocation", "Where was John Smith during this break?");

                if(fieldValue_Equals(fieldValues, "hospitalBreakCarerInHospitalCareeLocation", "Somewhere else")) {
                    validateMandatoryField(fieldValues, "hospitalBreakCarerInHospitalCareeLocationText", "Where was John Smith during this break?");
                }
            }
        }

        if(fieldValue_Equals(fieldValues, "hospitalBreakWhoInHospital", "Caree")) {
            validateMandatoryDateField(fieldValues, "When were they admitted?", "hospitalBreakCareeHospitalStartDate", new String[]{"hospitalBreakCareeHospitalStartDate_day", "hospitalBreakCareeHospitalStartDate_month", "hospitalBreakCareeHospitalStartDate_year"});
            validateMandatoryField(fieldValues, "hospitalBreakCareeHospitalStayEnded", "Has the hospital stay ended?");

            if(fieldValue_Equals(fieldValues, "hospitalBreakCareeHospitalStayEnded", "yes")) {
                validateMandatoryDateField(fieldValues, "When did their hospital stay end?", "hospitalBreakCareeHospitalEndDate", new String[]{"hospitalBreakCareeHospitalEndDate_day", "hospitalBreakCareeHospitalEndDate_month", "hospitalBreakCareeHospitalEndDate_year"});
                validateMandatoryField(fieldValues, "hospitalBreakCareeHospitalCarerStillCaring", "During this time in hospital were you still providing care for John Smith for 35 hours a week?");
            }
        }

        validateMandatoryField(fieldValues, "hospitalBreakWeeksNotCaring", "Have there been any other weeks you've not provided care for John Smith for 35 hours or more each week");

        LOG.trace("Ending BenefitsController.validate");
    }
}
