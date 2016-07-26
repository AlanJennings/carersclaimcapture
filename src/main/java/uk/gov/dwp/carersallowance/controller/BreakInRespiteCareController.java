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

    public static final String[] SHARED_FIELDS = {"break_id", "breakInCareType"};

    public static final String[] FIELDS = {"respiteBreakWhoInRespite",
                                           "respiteBreakCarerRespiteStartDate_day",
                                           "respiteBreakCarerRespiteStartDate_month",
                                           "respiteBreakCarerRespiteStartDate_year",
                                           "respiteBreakCarerRespiteStayEnded",
                                           "respiteBreakCarerRespiteEndDate_day",
                                           "respiteBreakCarerRespiteEndDate_month",
                                           "respiteBreakCarerRespiteEndDate_year",
                                           "respiteBreakCarerRespiteStayMedicalCare",
                                           "respiteBreakCareeRespiteStartDate_day",
                                           "respiteBreakCareeRespiteStartDate_month",
                                           "respiteBreakCareeRespiteStartDate_year",
                                           "respiteBreakCareeRespiteStayEnded",
                                           "respiteBreakCareeRespiteEndDate_day",
                                           "respiteBreakCareeRespiteEndDate_month",
                                           "respiteBreakCareeRespiteEndDate_year",
                                           "respiteBreakCareeRespiteStayMedicalCare",
                                           "respiteBreakCareeRespiteCarerStillCaring" };

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
    public String[] getSharedFields() {
        return SHARED_FIELDS;
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

        validateMandatoryField(fieldValues, "respiteBreakWhoInRespite", "Who was in respite care or a care home");
        if(fieldValue_Equals(fieldValues, "respiteBreakWhoInRespite", "Carer")) {
            validateMandatoryDateField(fieldValues, "respiteBreakCarerRespiteStartDate", "When were you admitted?");
            validateMandatoryField(fieldValues, "respiteBreakCarerRespiteStayEnded", "Has the respite or care home stay ended");

            if(fieldValue_Equals(fieldValues, "respiteBreakCarerRespiteStayEnded", "yes")) {
                validateMandatoryDateField(fieldValues, "respiteBreakCarerRespiteEndDate", "When did your respite or care home stay end?");
                validateMandatoryField(fieldValues, "respiteBreakCarerRespiteStayMedicalCare", "Did you or the person being looked after receive care from a medical professional during this time?");
            }
        }

        if(fieldValue_Equals(fieldValues, "respiteBreakWhoInRespite", "Caree")) {
            validateMandatoryDateField(fieldValues, "respiteBreakCareeRespiteStartDate", "When were they admitted?");
            validateMandatoryField(fieldValues, "respiteBreakCareeRespiteStayEnded", "Has the respite or care home stay ended");

            if(fieldValue_Equals(fieldValues, "respiteBreakCareeRespiteStayEnded", "yes")) {
                validateMandatoryDateField(fieldValues, "respiteBreakCareeRespiteEndDate", "When did their respite or care home stay end?");
                validateMandatoryField(fieldValues, "respiteBreakCareeRespiteStayMedicalCare", "Did you or the person being looked after receive care from a medical professional during this time?");
                validateMandatoryField(fieldValues, "respiteBreakCareeRespiteCarerStillCaring", "During this time in respite or a care home, were you still providing care for John Smith for 35 hours a week?");
            }
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
