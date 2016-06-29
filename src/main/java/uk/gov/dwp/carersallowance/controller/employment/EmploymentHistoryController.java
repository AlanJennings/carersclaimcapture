package uk.gov.dwp.carersallowance.controller.employment;

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

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.FieldCollection;
import uk.gov.dwp.carersallowance.session.FieldCollection.FieldCollectionComparator;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.session.UnknownRecordException;

@Controller
public class EmploymentHistoryController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EmploymentHistoryController.class);

    private static final String EMPLOYMENT_DETAIL     = "/your-income/employment/job-details";
    private static final String CURRENT_PAGE          = "/your-income/employment/been-employed";
    private static final String SAVE_EDITED_PAGE      = CURRENT_PAGE + "/update";
    private static final String EDITING_PAGE          = EMPLOYMENT_DETAIL;
    private static final String PAGE_TITLE            = "Your employment history - Your income";

    private static final String[] FIELDS                = {"moreEmployment"};
    private static final String[] SORTING_FIELDS        = {"jobStartDate_year", "jobStartDate_month", "jobStartDate_day"};

    public static final String   FIELD_COLLECTION_NAME = "employment";
    public static final String   ID_FIELD              = "employment_id";

    @Autowired
    public EmploymentHistoryController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        LOG.trace("Started EmploymentHistoryController.getNextPage");
        try {
            Boolean moreEmployment = getYesNoBooleanFieldValue(request, "moreEmployment");
            LOG.debug("moreEmployment = {}", moreEmployment);
            if(Boolean.TRUE.equals(moreEmployment)) {
                // reset the moreEmployment question
                request.getSession().removeAttribute("moreEmployment");
                LOG.debug("redirecting to employment detail page: {}.", EMPLOYMENT_DETAIL);
                return EMPLOYMENT_DETAIL;
            }

            // sort the employment here, so they are sorted when we revisit this page, but
            // not while we are working on it, as that might be confusing
            List<Map<String, String>> employments = getFieldCollections(request.getSession(), FIELD_COLLECTION_NAME);
            if(employments != null) {
                FieldCollectionComparator comparator = new FieldCollectionComparator(SORTING_FIELDS);
                Collections.sort(employments, comparator);
            }

            return super.getNextPage(request);
        } finally {
            LOG.trace("Ending EmploymentHistoryController.getNextPage");
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
        LOG.trace("Started EmploymentHistoryController.showForm");
        try {
            List<Map<String, String>> employments = getFieldCollections(request.getSession(), FIELD_COLLECTION_NAME);
            if(employments == null || employments.size() == 0) {
                return "redirect:" + EMPLOYMENT_DETAIL;
            }
            return super.showForm(request, model);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending EmploymentHistoryController.showForm\n");
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
    public String saveFieldCollection(HttpSession session) {
        LOG.trace("Started EmploymentHistoryController.saveFieldCollection");
        try {
            String[] fieldCollectionFields = FieldCollection.aggregateFieldLists(EmploymentDetailsController.FIELDS,
                                                                                 EmploymentPaymentController.FIELDS,
                                                                                 EmploymentPensionAndExpensesController.FIELDS);
            populateFieldCollectionEntry(session, FIELD_COLLECTION_NAME, fieldCollectionFields, ID_FIELD);

            return "redirect:" + getCurrentPage();
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending EmploymentHistoryController.saveFieldCollection\n");
        }
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request,
                           @ModelAttribute("changeEmployment") String idToChange,
                           @ModelAttribute("deleteEmployment") String idToDelete,
                           HttpSession session,
                           Model model) {

        return postFormFieldCollection(request, idToChange, idToDelete, session, model);
    }

    protected String postFormFieldCollection(HttpServletRequest request, String idToChange, String idToDelete, HttpSession session, Model model) {
        LOG.trace("Started EmploymentHistoryController.postForm");
        try {
            if(StringUtils.isEmpty(idToChange) == false) {
                try {
                    return editFieldCollectionRecord(request, idToChange, FIELD_COLLECTION_NAME, ID_FIELD, EDITING_PAGE);
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
            LOG.trace("Ending EmploymentHistoryController.postForm\n");
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
        // TODO date comes from earlier in the claim
        validateMandatoryField(fieldValues, "moreEmployment", "Have you had any other jobs since 1 December 2015?");

        LOG.trace("Ending BenefitsController.validate");
    }
}
