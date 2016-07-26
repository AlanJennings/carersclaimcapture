package uk.gov.dwp.carersallowance.controller;

import java.util.Arrays;
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

import uk.gov.dwp.carersallowance.session.FieldCollection;
import uk.gov.dwp.carersallowance.session.FieldCollection.FieldCollectionComparator;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.session.UnknownRecordException;
import uk.gov.dwp.carersallowance.utils.Parameters;

@Controller
public class BreaksInCareSummaryController extends AbstractFormController {

    private static final String BREAK_IN_CARE_TYPE = "breakInCareType";

    private static final Logger LOG = LoggerFactory.getLogger(BreaksInCareSummaryController.class);

    private static final String CURRENT_PAGE          = "/breaks/breaks-in-care";
    private static final String PAGE_TITLE            = "Breaks from care - About the person you care for";

    private static final String BREAKS_IN_HOSPITAL    = "/breaks/break-in-hospital";
    private static final String BREAKS_IN_RESPITE     = "/breaks/break-in-respite-care";
    private static final String BREAKS_IN_OTHER       = "/breaks/break-somewhere-else";

    private static final String SAVE_EDITED_PAGE      = CURRENT_PAGE + "/update";


    private static final String[] READONLY_FIELDS       = {"carerFirstName", "carerSurname", "careeFirstName", "careeSurname"};
    private static final String[] FIELDS                = {"moreBreaksInCare"};
    private static final String[] SORTING_FIELDS        = {"startDate_year", "startDate_month", "startDate_day"};

    private static final String   FIELD_COLLECTION_NAME = "breaks";
    private static final String   ID_FIELD              = "break_id";

    @Autowired
    public BreaksInCareSummaryController(SessionManager sessionManager) {
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
                String breakType = getRequestValue("moreBreaksInCareResidence", request);
                LOG.debug("breakType = {}", breakType);

                switch(breakType) {
                    case "hospital":
                        LOG.debug("redirecting to breaks in care detail page: {}.", BREAKS_IN_HOSPITAL);
                        return BREAKS_IN_HOSPITAL;
                    case "respite":
                        LOG.debug("redirecting to breaks in care detail page: {}.", BREAKS_IN_RESPITE);
                        return BREAKS_IN_RESPITE;
                    case "somewhere else":
                        LOG.debug("redirecting to breaks in care detail page: {}.", BREAKS_IN_OTHER);
                        return BREAKS_IN_OTHER;
                    default:
                        throw new IllegalArgumentException("Unknown break type: " + breakType + ", expecting one of: 'hospital', 'respite' or 'somewhere else'");
                }
            }

            // sort the breaks here, so they are sorted when we revisit this page, but
            // not while we are working on it, as that might be confusing
            List<Map<String, String>> breaks = getFieldCollections(request.getSession(), FIELD_COLLECTION_NAME);
            if(breaks != null) {
                FieldCollectionComparator comparator = new FieldCollectionComparator(SORTING_FIELDS);
                Collections.sort(breaks, comparator);
            }

            return super.getNextPage(request);  // this will be education
        } finally {
            LOG.trace("Ending BreaksInCareController.getNextPage");
        }
    }

    @Override
    public String[] getReadOnlyFields() {
        return READONLY_FIELDS;
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

    /**
     * Save the individual break fields into a break entry in the fieldCollection list
     * either a new one (if break_id is not populated), or an existing one (if it is)
     * and then cleanup the session by removing the individual fields values.
     *
     * Then redirect to the showForm page (i.e. post, redirect, get)
     *
     * Note: validation has already occurred, but can be repeated if required by iterating
     * over the individual page handlers (the request will need populating)
     */
    @RequestMapping(value=SAVE_EDITED_PAGE, method = RequestMethod.GET)
    public String saveFieldCollection(HttpServletRequest request, HttpSession session, Model model) {
        LOG.trace("Started BreaksInCareController.saveFieldCollection");
        try {
            // put the type in the session (so we can get it back)
            String[] fieldCollectionFields = getFieldCollectionFields();
            LOG.debug("fieldCollectionFields = {}", Arrays.asList(fieldCollectionFields));
            populateFieldCollectionEntry(session, FIELD_COLLECTION_NAME, fieldCollectionFields, ID_FIELD);

            return "redirect:" + getCurrentPage();
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending BreaksInCareController.saveFieldCollection\n");
        }
    }

    private String[] getFieldCollectionFields() {
        String[] fieldCollectionFields = FieldCollection.aggregateFieldLists(new String[]{ID_FIELD, BREAK_IN_CARE_TYPE},
                                                                             BreakInHospitalController.FIELDS,
                                                                             BreakInRespiteCareController.FIELDS,
                                                                             BreakSomewhereElseController.FIELDS);
        return fieldCollectionFields;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request,
                           @ModelAttribute("changeBreak") String idToChange,
                           @ModelAttribute("deleteBreak") String idToDelete,
                           HttpSession session,
                           Model model) {
        LOG.trace("Started EmploymentSummaryController.postForm");
        try {
            if(StringUtils.isEmpty(idToChange) == false) {
                try {
                    return editFieldCollectionRecord(request, idToChange, FIELD_COLLECTION_NAME, ID_FIELD);
                } catch(UnknownRecordException e) {
                    addFormError(idToChange, "break from care", "Unable to edit item");
                }
            }

            if(StringUtils.isEmpty(idToDelete) == false) {
                try {
                    return deleteFieldCollectionRecord(idToDelete, request, FIELD_COLLECTION_NAME, ID_FIELD);
                } catch(UnknownRecordException e) {
                    addFormError(idToDelete, "break from care", "Unable to delete item");
                }
            }

            // handling of the "moreEmployment" question is handled in getNextPage()
            return super.postForm(request, session, model);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending EmploymentSummaryController.postForm\n");
        }
    }

    protected String editFieldCollectionRecord(HttpServletRequest request, String idToChange, String fieldCollectionName, String idField) {
        Parameters.validateMandatoryArgs(new Object[]{idToChange, request}, new String[]{"idToChange", "request"});

        getValidationSummary().reset();

        // copy the record values into the edit fields in the session
        List<Map<String, String>> records = getFieldCollections(request.getSession(), fieldCollectionName, true);
        Map<String, String> record = FieldCollection.getFieldCollection(records, idField, idToChange);
        if(record == null) {
            throw new UnknownRecordException("Unknown record id: " + idToChange);
        } else {
            LOG.debug("found record, for id: {}", idToChange);
//            String[] fields = record.keySet().toArray(new String[]{});  // TODO instead of BreakInCareDetailController.FIELDS (?)
            copyMapToSession(record, getFieldCollectionFields(), request.getSession());
        }

        String breakType = record.get(BREAK_IN_CARE_TYPE);
        LOG.debug("breakType = {}", breakType);

        switch(breakType) {
            case "hospital":
                LOG.debug("redirecting to breaks in care detail page: {}.", BREAKS_IN_HOSPITAL);
                return "redirect:" + BREAKS_IN_HOSPITAL;
            case "respite":
                LOG.debug("redirecting to breaks in care detail page: {}.", BREAKS_IN_RESPITE);
                return "redirect:" + BREAKS_IN_RESPITE;
            case "elsewhere":
                LOG.debug("redirecting to breaks in care detail page: {}.", BREAKS_IN_OTHER);
                return "redirect:" + BREAKS_IN_OTHER;
            default:
                throw new IllegalArgumentException("Unknown break type: " + breakType + ", expecting one of: 'hospital', 'respite' or 'elsewhere'");
        }
    }


//    public String editCareBreak(String idToChange, HttpServletRequest request, Model model) {
//        Parameters.validateMandatoryArgs(new Object[]{idToChange, request, model}, new String[]{"idToDelete", "request", "model"});
//
//        getValidationSummary().reset();
//
//        // copy the record values into the edit fields in the session
//        List<Map<String, String>> records = getFieldCollections(request.getSession(), FIELD_COLLECTION_NAME, true);
//        Map<String, String> record = FieldCollection.getFieldCollection(records, ID_FIELD, idToChange);
//        if(record == null) {
//            throw new UnknownRecordException("Unknown record id: " + idToChange);
//        } else {
//            String[] fields = record.keySet().toArray(new String[]{});  // TODO instead of BreakInCareDetailController.FIELDS (?)
//            copyMapToSession(record, fields, request.getSession());
//        }
//
//        return "redirect:" + EDITING_PAGE;
//    }
//
    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting BenefitsController.validate");

        validateMandatoryField(fieldValues, "moreBreaksInCare", "Have you had any more breaks from caring for this person since 1 March 2016?");

        LOG.trace("Ending BenefitsController.validate");
    }
}
