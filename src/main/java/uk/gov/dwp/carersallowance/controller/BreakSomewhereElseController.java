package uk.gov.dwp.carersallowance.controller;

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

import uk.gov.dwp.carersallowance.session.SessionManager;

/**
 * A new empty care break has an index of -1;
 * Otherwise the care break index is whatever is appropriate
 * @author drh
 */
@Controller
public class BreakSomewhereElseController extends AbstractFormController {
    public static final Logger LOG = LoggerFactory.getLogger(BreakSomewhereElseController.class);

    private static final String PAGE_NAME     = "page.break-somewhere-else";
    private static final String CURRENT_PAGE  = "/breaks/break-somewhere-else";    // this has an argument
    private static final String NEXT_PAGE     = "/breaks/breaks-in-care/update";
    private static final String PARENT_PAGE   = "/breaks/breaks-in-care";

    public static final String[] SHARED_FIELDS = {"break_id", "breakInCareType"};

    @Autowired
    public BreakSomewhereElseController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    @Override
    public String getCurrentPage(HttpServletRequest request) {
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
    protected String getPageName() {
        return PAGE_NAME;
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

        validateMandatoryDateField(fieldValues, "careeSomewhereElseStartDate");
        validateMandatoryField(fieldValues, "careeSomewhereElseEndedTime");
        if(fieldValue_Equals(fieldValues, "careeSomewhereElseEndedTime", "yes")) {
            validateMandatoryDateField(fieldValues, "careeSomewhereElseEndDate");
        }

        validateMandatoryField(fieldValues, "carerSomewhereElseWhereYou");
        if(fieldValue_Equals(fieldValues, "carerSomewhereElseWhereYou", "elsewhere")) {
            validateMandatoryField(fieldValues, "carerSomewhereElseWhereYouOtherText");
        }

        validateMandatoryField(fieldValues, "carerSomewhereElseWhereCaree");
        if(fieldValue_Equals(fieldValues, "carerSomewhereElseWhereCaree", "elsewhere")) {
            validateMandatoryField(fieldValues, "carerSomewhereElseWhereCareeOtherText");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
