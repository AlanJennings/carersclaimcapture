package uk.gov.dwp.carersallowance.controller.breaks;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.FieldCollection;
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
    private static final String PARENT_PAGE   = "/breaks/breaks-in-care";
    private static final String PAGE_TITLE    = "Break - About the care you provide";

    private static final String   FIELD_COLLECTION_NAME = "breaks";
    private static final String   ID_FIELD = "break_id";
    private static final String[] FIELDS = {ID_FIELD,
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
        return PARENT_PAGE;
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
        LOG.trace("Started BreakInCareDetailController.showForm");
        try {
            String destination = super.showForm(request, model);

            // if the ID field is populated then we are editing an existing break
            // and we should load the data, but only if we have not failed validation
            // otherwise it is a new break and everything is already up to date.
            String editIdValue = request.getParameter(ID_FIELD);
            LOG.debug("editIdValue = {}", editIdValue);
            if(StringUtils.isEmpty(editIdValue) == false && getValidationSummary().hasFormErrors() == false) {
                LOG.debug("populating edit break data");
                List<Map<String, String>> breaks = getFieldCollections(request.getSession(), FIELD_COLLECTION_NAME, true);
                Map<String, String> careBreak = FieldCollection.getFieldCollection(breaks, ID_FIELD, editIdValue);
                LOG.debug("careBreak = {}", careBreak);
                model.addAllAttributes(careBreak);
            }
            return destination;
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending BreakInCareDetailController.showForm\n");
        }
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, HttpSession session, Model model, RedirectAttributes redirectAttrs) {
        LOG.trace("Starting BreakInCareDetailController.postForm");
        try {
            return super.postForm(request, session, model, redirectAttrs);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending BreakInCareDetailController.postForm\n");
        }
    }

    @Override
    protected void finalizePostForm(HttpServletRequest request) {
        // build the field collection and add it to the list of breaks
        LOG.trace("Starting BreakInCareDetailController.finalizePostForm");
        try {
            HttpSession session = request.getSession();

            List<Map<String, String>> breaks = getFieldCollections(session, FIELD_COLLECTION_NAME, true);
            LOG.debug("breaks before = {}", breaks);

            Map<String, String> careBreak = FieldCollection.getFieldValues(request.getParameterMap(), FIELDS);
            LOG.debug("careBreak = {}", careBreak);

            String breakId = careBreak.get(ID_FIELD);
            if(StringUtils.isEmpty(breakId)) {
                LOG.debug("Creating new break");
                String newBreakId = getNextIdValue(breaks, ID_FIELD);

                careBreak.put(ID_FIELD, newBreakId);
                breaks.add(careBreak);
            } else {
                boolean existingBreakFound = false;
                if(StringUtils.isEmpty(breakId) == false) {
                    for(Map<String, String> existingBreak: breaks) {
                        String existingBreakId = existingBreak.get(ID_FIELD);
                        if(breakId.equals(existingBreakId)) {
                            existingBreak.clear();
                            existingBreak.putAll(careBreak);
                            existingBreakFound = true;
                            break;
                        }
                    }
                }

                if(existingBreakFound == false) {
                    LOG.error("Unknown break ID: " + breakId);
                    throw new IllegalArgumentException("Unknown break ID: " + breakId);
                }
            }
            LOG.debug("breaks after = {}", breaks);
            LOG.debug("getFieldCollections('breaks') = {}", getFieldCollections(session, FIELD_COLLECTION_NAME, false));

            removeFromSession(session, FIELDS);
        } finally {
            LOG.trace("Ending BreakInCareDetailController.finalizePostForm");
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
