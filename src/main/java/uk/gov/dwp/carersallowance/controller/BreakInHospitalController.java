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

    private static final String CURRENT_PAGE  = "/breaks/break";    // this has an argument
    private static final String NEXT_PAGE     = "/breaks/breaks-in-care/update";
    private static final String PARENT_PAGE   = "/breaks/breaks-in-care";
    private static final String PAGE_TITLE    = "Break - About the care you provide";

    public static final String[] FIELDS = {"whoInHospital",
                                           "carerHospitalStartDate_day",
                                           "carerHospitalStartDate_month",
                                           "carerHospitalStartDate_year",
                                           "carerHospitalEndDate_day",
                                           "carerHospitalEndDate_month",
                                           "carerHospitalEndDate_year",
                                           "carerInHospitalCareeLocation",
                                           "carerInHospitalCareeLocationText",
                                           "careeHospitalStartDate_day",
                                           "careeHospitalStartDate_month",
                                           "careeHospitalStartDate_year",
                                           "careeHospitalStayEnded",
                                           "careeHospitalEndDate_day",
                                           "careeHospitalEndDate_month",
                                           "careeHospitalEndDate_year",
                                           "careeHospitalCarerStillCaring",
                                           "weeksNotCaring" };

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

        validateMandatoryField(fieldValues, "whoInHospital", "Who was in hospital?");

        if(fieldValue_Equals(fieldValues, "whoInHospital", "Carer")) {
            validateMandatoryDateField(fieldValues, "When were you admitted?", "carerHospitalStartDate", new String[]{"carerHospitalStartDate_day", "carerHospitalStartDate_month", "carerHospitalStartDate_year"});
            validateMandatoryField(fieldValues, "carerHospitalStayEnded", "Has the hospital stay ended?");

            if(fieldValue_Equals(fieldValues, "carerHospitalStayEnded", "yes")) {
                validateMandatoryDateField(fieldValues, "When did your hospital stay end?", "carerHospitalEndDate", new String[]{"carerHospitalEndDate_day", "carerHospitalEndDate_month", "carerHospitalEndDate_year"});
                validateMandatoryField(fieldValues, "carerInHospitalCareeLocation", "Where was John Smith during this break?");

                if(fieldValue_Equals(fieldValues, "carerInHospitalCareeLocation", "Somewhere else")) {
                    validateMandatoryField(fieldValues, "carerInHospitalCareeLocationText", "Where was John Smith during this break?");
                }
            }
        }

        if(fieldValue_Equals(fieldValues, "whoInHospital", "Caree")) {
            validateMandatoryDateField(fieldValues, "When were they admitted?", "careeHospitalStartDate", new String[]{"careeHospitalStartDate_day", "careeHospitalStartDate_month", "careeHospitalStartDate_year"});
            validateMandatoryField(fieldValues, "careeHospitalStayEnded", "Has the hospital stay ended?");

            if(fieldValue_Equals(fieldValues, "careeHospitalStayEnded", "yes")) {
                validateMandatoryDateField(fieldValues, "When did their hospital stay end?", "careeHospitalEndDate", new String[]{"careeHospitalEndDate_day", "careeHospitalEndDate_month", "careeHospitalEndDate_year"});
                validateMandatoryField(fieldValues, "careeHospitalCarerStillCaring", "During this time in hospital were you still providing care for John Smith for 35 hours a week?");
            }
        }

        validateMandatoryField(fieldValues, "weeksNotCaring", "Have there been any other weeks you've not provided care for John Smith for 35 hours or more each week");

        LOG.trace("Ending BenefitsController.validate");
    }
}
