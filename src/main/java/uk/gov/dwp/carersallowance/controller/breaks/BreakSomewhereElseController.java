package uk.gov.dwp.carersallowance.controller.breaks;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import uk.gov.dwp.carersallowance.transformations.TransformationManager;

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
    public BreakSomewhereElseController(final SessionManager sessionManager, final MessageSource messageSource, final TransformationManager transformationManager) {
        super(sessionManager, messageSource, transformationManager);
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
    public String postForm(HttpServletRequest request, Model model) {
        LOG.trace("Starting BreakInCareDetailController.postForm");
        try {
            return super.postForm(request, model);
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
    protected void validate(String[] fields, Map<String, String[]> fieldValues, Map<String, String[]> allFieldValues) {
        LOG.trace("Starting BenefitsController.validate");

        getLegacyValidation().validateMandatoryDateField(fieldValues, "careeSomewhereElseStartDate");
        getLegacyValidation().validateMandatoryField(fieldValues, "careeSomewhereElseEndedTime");
        if(getLegacyValidation().fieldValue_Equals(fieldValues, "careeSomewhereElseEndedTime", "yes")) {
            getLegacyValidation().validateMandatoryDateField(fieldValues, "careeSomewhereElseEndDate");
        }

        getLegacyValidation().validateMandatoryField(fieldValues, "carerSomewhereElseWhereYou");
        if(getLegacyValidation().fieldValue_Equals(fieldValues, "carerSomewhereElseWhereYou", "elsewhere")) {
            getLegacyValidation().validateMandatoryField(fieldValues, "carerSomewhereElseWhereYouOtherText");
        }

        getLegacyValidation().validateMandatoryField(fieldValues, "carerSomewhereElseWhereCaree");
        if(getLegacyValidation().fieldValue_Equals(fieldValues, "carerSomewhereElseWhereCaree", "elsewhere")) {
            getLegacyValidation().validateMandatoryField(fieldValues, "carerSomewhereElseWhereCareeOtherText");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
