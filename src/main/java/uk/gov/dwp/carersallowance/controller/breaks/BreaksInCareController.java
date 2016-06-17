package uk.gov.dwp.carersallowance.controller.breaks;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.FieldCollection.FieldCollectionComparator;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.utils.Parameters;

@Controller
public class BreaksInCareController extends AbstractFormController {

    private static final Logger LOG = LoggerFactory.getLogger(BreaksInCareController.class);

    private static final String BREAKS_IN_CARE_DETAIL = "/breaks/break";
    private static final String FIELD_COLLECTION_NAME = "breaks";
    private static final String CURRENT_PAGE          = "/breaks/breaks-in-care";
    private static final String EDITING_PAGE          = BREAKS_IN_CARE_DETAIL;
    private static final String PAGE_TITLE            = "Breaks from care - About the person you care for";

    private static final String[] FIELDS              = {"moreBreaksInCare"};
    private static final String[] SORTING_FIELDS      = {"startDate_year", "startDate_month", "startDate_day"};
    private static final String   ID_FIELD            = "break_id";

    @Autowired
    public BreaksInCareController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        LOG.trace("Started BreaksInCareController.getNextPage");
        try {
            Boolean moreBreaksInCare = getYesNoBooleanFieldValue(request, "moreBreaksInCare");
            LOG.debug("moreBreaksInCare = {}", moreBreaksInCare);
            if(Boolean.TRUE.equals(moreBreaksInCare)) {
                // reset the moreBreaksInCare question
                request.getSession().removeAttribute("moreBreaksInCare");
                LOG.debug("redirecting to breaks in care detail page: {}.", BREAKS_IN_CARE_DETAIL);
                return BREAKS_IN_CARE_DETAIL;
            }

            // sort the breaks here, so they are sorted when we revisit this page, but
            // not while we are working on it, as that might be confusing
            List<Map<String, String>> breaks = getFieldCollections(request.getSession(), FIELD_COLLECTION_NAME);
            if(breaks != null) {
                FieldCollectionComparator comparator = new FieldCollectionComparator(SORTING_FIELDS);
                Collections.sort(breaks, comparator);
            }

            return super.getNextPage(request);
        } finally {
            LOG.trace("Ending BreaksInCareController.getNextPage");
        }
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
        LOG.trace("Started BreaksInCareController.showForm");
        try {
            return super.showForm(request, model);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending BreaksInCareController.showForm\n");
        }
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request,
                           @ModelAttribute("changeBreak") String idToChange,
                           @ModelAttribute("deleteBreak") String idToDelete,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttrs) {
        LOG.trace("Started BreaksInCareController.postForm");
        try {
            if(StringUtils.isEmpty(idToChange) == false) {
                return editCareBreak(idToChange, request, model, redirectAttrs);
            }

            if(StringUtils.isEmpty(idToDelete) == false) {
                return deleteCareBreak(idToDelete, request, model);
            }

            // handling of the "moreBreaksInCare" question is handled in getNextPage()
            return super.postForm(request, session, model, redirectAttrs);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending BreaksInCareController.postForm\n");
        }
    }

    public String editCareBreak(String idToChange, HttpServletRequest request, Model model, RedirectAttributes redirectAttrs) {
        Parameters.validateMandatoryArgs(new Object[]{idToChange, request, model}, new String[]{"idToDelete", "request", "model"});

        getValidationSummary().reset();

        redirectAttrs.addAttribute(ID_FIELD, idToChange);
        return "redirect:" + EDITING_PAGE;
    }

    /**
     * Note: this does not validate the form
     */
    public String deleteCareBreak(String idToDelete, HttpServletRequest request, Model model) {
        LOG.trace("Starting BenefitsController.deleteCareBreak");
        try {
            Parameters.validateMandatoryArgs(new Object[]{idToDelete, request, model}, new String[]{"idToDelete", "request", "model"});

            Integer foundIndex = null;
            List<Map<String, String>> breaks = getFieldCollections(request.getSession(), FIELD_COLLECTION_NAME);
            for(int index = 0; index < breaks.size(); index++) {
                Map<String, String> map = breaks.get(index);
                if(idToDelete.equals(map.get(ID_FIELD))) {
                    foundIndex = Integer.valueOf(index);
                    break;
                }
            }

            getValidationSummary().reset();
            model.addAttribute("validationErrors", getValidationSummary());

            if(foundIndex != null) {
                breaks.remove(foundIndex.intValue());
            } else {
                addFormError(idToDelete, "break from care", "Unable to delete item");
            }

            return showForm(request, model);
        } finally {
            LOG.trace("Ending BenefitsController.deleteCareBreak");
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

        validateMandatoryFields(fieldValues, "Have you had any more breaks from caring for this person since 1 March 2016?", "moreBreaksInCare");

        LOG.trace("Ending BenefitsController.validate");
    }
}
