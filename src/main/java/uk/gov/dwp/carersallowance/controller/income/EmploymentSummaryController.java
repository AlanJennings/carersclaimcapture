package uk.gov.dwp.carersallowance.controller.income;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;

import uk.gov.dwp.carersallowance.controller.PageOrder;
import uk.gov.dwp.carersallowance.session.FieldCollection;
import uk.gov.dwp.carersallowance.session.FieldCollection.FieldCollectionComparator;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.session.UnknownRecordException;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.C3Constants;

@Controller
public class EmploymentSummaryController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EmploymentSummaryController.class);

    private static final String PENSION_EXPENSES_PAGE_NAME = "/your-income/employment/about-expenses";
    private static final String YOUR_INCOME = "/your-income/your-income";
    private static final String LAST_WAGE_PAGE_NAME = "/your-income/employment/last-wage";
    private static final String JOB_DETAILS_PAGE_NAME = "/your-income/employment/job-details";

    private static final String BEEN_EMPLOYED_PAGE    = "/your-income/employment/been-employed";
    private static final String EDITING_PAGE          = JOB_DETAILS_PAGE_NAME;

    private static final String[] READONLY_FIELDS     = { "employerName" };
    private static final String[] SORTING_FIELDS      = {"jobStartDate_year", "jobStartDate_month", "jobStartDate_day"};

    public static final String   FIELD_COLLECTION_NAME = "employment";
    public static final String   ID_FIELD              = "employment_id";

    @Autowired
    public EmploymentSummaryController(final SessionManager sessionManager,
                                       final MessageSource messageSource,
                                       final TransformationManager transformationManager,
                                       final PageOrder pageOrder) {
        super(sessionManager, messageSource, transformationManager, pageOrder);
    }

    /**
     * If moreEmployment is yes, then go to the employment detail page (i.e. start a new employment item)
     * otherwise, sort the current records and display the next page according to the choices entered
     * in YourIncome (currently self employment, statutory pay or onto bank details)
     */
    @Override
    public String getNextPage(final String currentPage, final Session session) {
        LOG.trace("Started EmploymentSummaryController.getNextPage");
        try {
            final String moreEmployment = (String)session.getAttribute("moreEmployment");
            LOG.debug("moreEmployment = {}", moreEmployment);
            if (C3Constants.TRUE.equals(moreEmployment)) {
                // reset the moreEmployment question
                session.removeAttribute("moreEmployment");
                sessionManager.saveSession(session);
                LOG.debug("redirecting to employment detail page: {}.", JOB_DETAILS_PAGE_NAME);
                return JOB_DETAILS_PAGE_NAME;
            }

            // sort the employment here, so they are sorted when we revisit this page, but
            // not while we are working on it, as that might be confusing
//            final List<Map<String, String>> employments = getFieldCollections(session, FIELD_COLLECTION_NAME);
//            if (employments != null) {
//                final FieldCollectionComparator comparator = new FieldCollectionComparator(SORTING_FIELDS);
//                Collections.sort(employments, comparator);
//            }

            return super.getNextPage(currentPage, session);
        } finally {
            LOG.trace("Ending EmploymentSummaryController.getNextPage");
        }
    }

    @Override
    public String[] getReadOnlyFields() {
        return ArrayUtils.addAll(super.getReadOnlyFields(), READONLY_FIELDS);
    }

    /**
     * if there are no employment items already skip straight to entering a new one
     * otherwise show the summary form as normal.
     */
    @RequestMapping(value=BEEN_EMPLOYED_PAGE, method = RequestMethod.GET)
    public String getForm(HttpServletRequest request, Model model) {
        LOG.trace("Started EmploymentSummaryController.getForm");
        try {
            final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
//            final List<Map<String, String>> employments = getFieldCollections(session, FIELD_COLLECTION_NAME);
//            if (employments == null || employments.isEmpty()) {
//                return "redirect:" + YOUR_INCOME;
//            }
//            model.addAttribute(FIELD_COLLECTION_NAME, employments);
            return super.getForm(request, model);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending EmploymentSummaryController.getForm\n");
        }
    }

    /**
     * handle the action choices (edit entry, delete entry or create new entry)
     */
    @RequestMapping(value=BEEN_EMPLOYED_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request,
                           @ModelAttribute("changeSubFormElement") String idToChange,
                           @ModelAttribute("deleteSubFormElement") String idToDelete,
                           Model model) {
        LOG.trace("Started EmploymentSummaryController.postForm");
        try {
            final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
            if (StringUtils.isEmpty(idToChange) == false) {
                try {
                    final String editPage = ""; //editFieldCollectionRecord(session, idToChange, FIELD_COLLECTION_NAME, ID_FIELD, EDITING_PAGE);
                    session.setAttribute("moreEmployment", C3Constants.YES);
                    sessionManager.saveSession(session);
                    return editPage;
                } catch (UnknownRecordException e) {
                    getValidationSummary().addFormError(idToChange, "break from care", "Unable to edit item");
                }
            }

            if (StringUtils.isEmpty(idToDelete) == false) {
                try {
                    final String currentPage = ""; //deleteFieldCollectionRecord(session, idToDelete, request, FIELD_COLLECTION_NAME, ID_FIELD);
                    sessionManager.saveSession(session);
//                    final List<Map<String, String>> employments = getFieldCollections(sessionManager.getSession(sessionManager.getSessionIdFromCookie(request)), FIELD_COLLECTION_NAME);
//                    if (employments == null || employments.isEmpty()) {
//                        return "redirect:" + YOUR_INCOME;
//                    }
                    return currentPage;
                } catch (UnknownRecordException e) {
                    getValidationSummary().addFormError(idToDelete, "break from care", "Unable to delete item");
                }
            }

            // handling of the "moreEmployment" question is handled in getNextPage()
            return super.postForm(request, model);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending EmploymentSummaryController.postForm\n");
        }
    }

    @RequestMapping(value=PENSION_EXPENSES_PAGE_NAME, method = RequestMethod.POST)
    public String postLastPage(HttpServletRequest request, Model model) {
        final String nextPage = super.postForm(request, model);
        if (hasErrors()) {
            return nextPage;
        }
        final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
        final String[] fieldCollectionFields = FieldCollection.aggregateFieldLists(getFields(BEEN_EMPLOYED_PAGE), getFields(JOB_DETAILS_PAGE_NAME), getFields(LAST_WAGE_PAGE_NAME), getFields(PENSION_EXPENSES_PAGE_NAME), new String[]{ ID_FIELD });
        //populateFieldCollectionEntry(session, FIELD_COLLECTION_NAME, fieldCollectionFields, ID_FIELD);
        session.removeAttribute("moreEmployment");
        sessionManager.saveSession(session);
        return nextPage;
    }

    @RequestMapping(value=PENSION_EXPENSES_PAGE_NAME, method = RequestMethod.GET)
    public String showLastPage(HttpServletRequest request, Model model) {
        return super.getForm(request, model);
    }

    @RequestMapping(value=YOUR_INCOME, method = RequestMethod.POST)
    public String postYourIncomePage(HttpServletRequest request, Model model) {
        final String nextPage = super.postForm(request, model);
        if (hasErrors()) {
            return nextPage;
        }
        final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
//        final List<Map<String, String>> employments = getFieldCollections(session, FIELD_COLLECTION_NAME);
//        if (CollectionUtils.isEmpty(employments) && C3Constants.YES.equals(session.getAttribute("beenEmployedSince6MonthsBeforeClaim"))) {
//            session.setAttribute("moreEmployment", C3Constants.YES);
//            sessionManager.saveSession(session);
//            return "redirect:" + JOB_DETAILS_PAGE_NAME;
//        }
        return nextPage;
    }

    @RequestMapping(value=YOUR_INCOME, method = RequestMethod.GET)
    public String showIncomePage(HttpServletRequest request, Model model) {
        return super.getForm(request, model);
    }
}
