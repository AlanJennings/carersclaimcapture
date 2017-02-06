package uk.gov.dwp.carersallowance.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.session.UnknownRecordException;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.subform.SubFormProcessing;
import uk.gov.dwp.carersallowance.utils.CollectionUtils;
import uk.gov.dwp.carersallowance.utils.KeyValue;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.validations.Dependencies;
import uk.gov.dwp.carersallowance.validations.Dependency;
import uk.gov.dwp.carersallowance.validations.ValidationSummary;

/**
 * @author David Hutchinson (drh@elegantsolutions.co.uk) on 4 Jan 2017.
 */
public class PageOrder {
    private static final Logger LOG = LoggerFactory.getLogger(PageOrder.class);

    private static final String ROOT_FORM_PAGE_KEY_FORMAT               = "form.%s.pages";
    private static final String NESTED_FORM_PAGE_KEY_FORMAT             = "subform.%s.pages";
    private static final String PAGE_DEPENDENCY_KEY_FORMAT              = "%s.dependency";
    private static final String SUBFORM_PSEUDO_FIELDNAME                = "subform.%s";
    private static final String NESTED_FORM_PREFIX                      = "subform=";
    private static final String NEXT_PARENT_PAGE_DEPENDENCY_KEY_FORMAT  = "%s.move.to.next.parent.page.dependency";
    private static final String NESTED_FORM_FORMAT                      = NESTED_FORM_PREFIX + "%s";
    private static final String FIRST_PAGE_DEPENDENCY                   = "subform.%s.first.page.dependency";
    private static final String FIRST_PAGE                              = "subform.%s.first.page";
    private static final String PREVIOUS_PAGE_EXCEPTIONS                = "%s.previous.page.exceptions";

    private String                  formName;
    private List<String>            pageList;
    private Dependencies            dependencies;
    private Map<String, PageOrder>  nestedForms;
    private boolean                 isNestedForm;
    private PageOrder               parentForm;
    private SubFormProcessing       subFormProcessing;
    private MessageSource           messageSource;

    public PageOrder(final MessageSource messageSource, final String formName) throws ParseException {
        this(messageSource, formName, false, null);
    }

    public PageOrder(final MessageSource messageSource, final String formName, final boolean isNestedForm, final PageOrder parentForm) throws ParseException {
        LOG.trace("Started PageOrder.initPageList");
        Parameters.validateMandatoryArgs(messageSource,  "messageSource");
        try {
            LOG.debug("formName = {}");
            this.formName = formName;
            this.isNestedForm = isNestedForm;
            this.parentForm = parentForm;
            this.messageSource = messageSource;

            init(messageSource, formName);

        } finally {
            LOG.trace("Started PageOrder.initPageList");
        }
    }

    public String       getFormName()   { return formName; }
    public List<String> getPageList()   { return pageList; }
    public boolean      isNestedForm()  { return isNestedForm; }
    public PageOrder    getParentForm() { return parentForm; }

    private void init(final MessageSource messageSource, final String formName) throws ParseException {
        final Map<String, PageOrder> tempNestedForms = new HashMap<>();
        String formKey;
        if (isNestedForm) {
            formKey = String.format(NESTED_FORM_PAGE_KEY_FORMAT, formName);
        } else {
            formKey = String.format(ROOT_FORM_PAGE_KEY_FORMAT, formName);
        }
        subFormProcessing = new SubFormProcessing(formName, messageSource);

        final List<String> rawPageList = initPageList(messageSource, formKey);
        for (final String pageName: rawPageList) {
            if (isNestedFormFieldName(pageName)) {
                final String nestedFormName = KeyValue.getValue(pageName, "=");
                PageOrder nestedPageOrder = new PageOrder(messageSource, nestedFormName, true, this);

                tempNestedForms.put(nestedFormName, nestedPageOrder);
            }
        }

        this.pageList = rawPageList;
        this.nestedForms = tempNestedForms;

        List<String> referenceFields = null;    // TODO used to check that the field being dependent on are valid (null == no checking)
        List<String> dependencyFieldList = new ArrayList<>(pageList);
        for (final String nestedFormName : tempNestedForms.keySet()) {
            final String subFormPseudoFieldName = String.format(SUBFORM_PSEUDO_FIELDNAME, nestedFormName);
            dependencyFieldList.add(subFormPseudoFieldName);
        }
        dependencies = new Dependencies(messageSource, PAGE_DEPENDENCY_KEY_FORMAT, dependencyFieldList, referenceFields);
    }

    private List<String> initPageList(final MessageSource messageSource, final String pagesKeyName) {
        LOG.trace("Started PageOrder.initPageList");
        LOG.debug("formName = {}, pagesKeyName = {}", formName, pagesKeyName);
        Parameters.validateMandatoryArgs(messageSource,  "messageSource");
        if (pagesKeyName == null) {
            return null;
        }
        final String pageList = getMessage(pagesKeyName);
        if (pageList == null) {
            return null;
        }

        final String[] pagesArray = pageList.split(",");

        final List<String> pages = new ArrayList<>();
        for (String pageName: pagesArray) {
            pageName = pageName.trim();
            if (pageName.equals("") == false) {
                pages.add(pageName);
            }
        }
        LOG.debug("page = {}", pages);
        LOG.trace("Ending PageOrder.initPageList");
        return pages;
    }

    /**
     * If current page is blank, then return the first page
     */
    public String getNextPage(final String currentPage, final Session session, final Boolean previousPage) {
        String nextPage;
        if (StringUtils.isBlank(currentPage)) {
            String firstPageDependency = String.format(FIRST_PAGE_DEPENDENCY, formName);
            if (previousPage == false && Dependency.pageDependencyFulfilled(session.getData(), getMessage(firstPageDependency), Boolean.FALSE)) {
                return getMessage(String.format(FIRST_PAGE, formName));
            } else {
                nextPage = pageList.get(0);
            }
        } else {
            // check if we are on a nested form
            final PageOrder currentPageOrder = getCurrentPageOrder(currentPage);
            if (currentPageOrder != this) {
                return currentPageOrder.getNextPage(currentPage, session, previousPage);
            }

            int index = pageList.indexOf(currentPage);
            if (index == -1 || index == (pageList.size() -1)) {
                if (currentPageOrder.isNestedForm() == false) {
                    return null;  // not found or last
                }

                // nestedForms loop back to their start page.
                // the start page in a nested form has a different dependency that the nested form itself
                // so by using the first page dependency it can loop back to any page in the nested form.
                // Therefore also need to explicitly check the subform dependency (and therefore exit) criteria
                final String subFormName = currentPageOrder.getFormName();
                final String pseudoFieldName = String.format(SUBFORM_PSEUDO_FIELDNAME, subFormName);
                final String nextParentPageDependency = String.format(NEXT_PARENT_PAGE_DEPENDENCY_KEY_FORMAT, pseudoFieldName);
                final PageOrder parentForm = currentPageOrder.getParentForm();
                if (Dependency.pageDependencyFulfilled(session.getData(), getMessage(nextParentPageDependency), Boolean.TRUE)) {
                    // return to the point we dropped out of the parent
                    final String nestedFormName = String.format(NESTED_FORM_FORMAT, subFormName);
                    return parentForm.getNextPage(nestedFormName, session, previousPage);
                } else {
                    nextPage = pageList.get(0);
                }
            } else {
                nextPage = pageList.get(index + 1);
            }
        }

        String dependencyNextPage = nextPage;
        if (isNestedFormFieldName(nextPage)) {
            dependencyNextPage = convertNestedDeclarationToPseudoFieldName(nextPage);
        }
        if (areDependenciesFulfilled(CollectionUtils.getAllFieldValues(session), dependencyNextPage) == false) {
            return getNextPage(nextPage, session, previousPage);
        }

        nextPage = getPage(nextPage, session, previousPage);

        return nextPage;
    }

    private boolean areDependenciesFulfilled(final Map<String, String[]> session, final String pseudoFieldName) {
        return dependencies.areDependenciesFulfilled(pseudoFieldName, session);
    }

    /**
     * Return the pseudo field name for a nested form, or the original unchanged
     * name if it is not a nested form declaration
     *
     * @param nestedDeclaration of the form: subform=<subform name>
     */
    private String convertNestedDeclarationToPseudoFieldName(final String nestedDeclaration) {
        if (nestedDeclaration == null) {
            return null;
        }
        if (nestedDeclaration.startsWith(NESTED_FORM_PREFIX)) {
            final KeyValue keyValue = new KeyValue(nestedDeclaration, "=");
            final String nestedFormName = keyValue.getValue();
            return String.format(SUBFORM_PSEUDO_FIELDNAME, nestedFormName);
        }
        return nestedDeclaration;  // return it unchanged
    }

    private boolean isNestedFormFieldName(final String nextPage) {
        return nextPage != null && nextPage.startsWith(NESTED_FORM_PREFIX);
    }

   /**
     * If current page is blank, then return the first page
     */
    public String getPreviousPage(final String currentPage, final Session session) {
        String previousPage;
        if (StringUtils.isBlank(currentPage)) {
            previousPage = pageList.get(0);

        } else {
            // get the correct PageOrder object
            final PageOrder currentPageOrder = getCurrentPageOrder(currentPage);
            if (currentPageOrder != this) {
                return currentPageOrder.getPreviousPage(currentPage, session);
            }

            int index = pageList.indexOf(currentPage);
            if (index == -1 || index == 0) { // if not found or the first page
                if (currentPageOrder.isNestedForm() == false) {
                    // top level form, so there is no previous
                    return null;
                }

                // ok, so this is a nested form.  Previous page for a nested form
                // goes to the page of the parent just prior to the nested form entry
                // unless dependencies don't allow in which case the one before that
                // and so on
                final PageOrder parentForm = currentPageOrder.getParentForm();
                final String subFormName = currentPageOrder.getFormName();
                final String nestedFormName = String.format(NESTED_FORM_FORMAT, subFormName); // e.g. subform=breaks
                return parentForm.getPreviousPage(nestedFormName, session);
            } else {
                previousPage = pageList.get(index - 1);
            }
        }

        // if the previous page has dependencies that are not satisfied iterate backwards
        String dependencyPreviousPage = previousPage;
        if (isNestedFormFieldName(previousPage)) {
            dependencyPreviousPage = convertNestedDeclarationToPseudoFieldName(previousPage);
        }
        final String previousPageExceptions = getMessage(String.format(PREVIOUS_PAGE_EXCEPTIONS, formName));
        if ((previousPageExceptions != null && !previousPageExceptions.contains(dependencyPreviousPage)) && areDependenciesFulfilled(CollectionUtils.getAllFieldValues(session), dependencyPreviousPage) == false) {
            return getPreviousPage(previousPage, session);
        }

        previousPage = getPage(previousPage, session, true);

        return previousPageOnEmpty(currentPage, previousPage, session);
    }

    private String getPage(final String page, final Session session, final Boolean previousPage) {
        if (isNestedFormFieldName(page)) {   // e.g. subform=breaks
            final String nestedFormName = page.substring(NESTED_FORM_PREFIX.length()).trim();
            if (nestedForms.containsKey(nestedFormName) == false) {
                throw new IllegalArgumentException("Unknown nested form: " + nestedFormName);
            }
            final PageOrder nestedForm = nestedForms.get(nestedFormName);
            return nestedForm.getNextPage(null, session, previousPage);
        }
        return page;
    }

    private PageOrder getCurrentPageOrder(String currentPage) {
        if (pageList.contains(currentPage)) {
            return this;
        }

        for (final PageOrder nestedForm : nestedForms.values()) {
            if (nestedForm.getCurrentPageOrder(currentPage) != null) {
                return nestedForm;
            }
        }

        return null;
    }

    private String getMessage(final String code) {
        return messageSource.getMessage(code, null, null, Locale.getDefault());
    }

    public String toString() {
        final StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("formName = ").append(formName);
        buffer.append(", pageList = ").append(pageList);
        buffer.append(", dependencies = ").append(dependencies);
        buffer.append(", nestedForms = ").append(nestedForms);
        buffer.append(", isNestedForm = ").append(isNestedForm);
        buffer.append(", parentForm = ").append(System.identityHashCode(parentForm));

        buffer.append("]");

        return buffer.toString();
    }

    public Boolean isLastPage(final String currentPage) {
        PageOrder pageOrder = getCurrentPageOrder(currentPage);
        List<String> pageList = pageOrder.getPageList();
        return pageList.indexOf(currentPage) == pageList.size()-1;
    }

    public Boolean isFirstPage(final String currentPage) {
        PageOrder pageOrder = getCurrentPageOrder(currentPage);
        List<String> pageList = pageOrder.getPageList();
        return pageList.indexOf(currentPage) == 0;
    }

    public SubFormProcessing getSubFormProcessorForPage(final String currentPage) {
        PageOrder pageOrder = getCurrentPageOrder(currentPage);
        return pageOrder.subFormProcessing;
    }

    public List<String> getCurrentPageList(final String currentPage) {
        PageOrder pageOrder = getCurrentPageOrder(currentPage);
        return pageOrder.getPageList();
    }

    public String getNextPageInList(final String currentPage, final Session session) {
        PageOrder pageOrder = getCurrentPageOrder(currentPage);
        int pageIndex = pageOrder.getPageList().indexOf(currentPage);
        if (pageIndex == -1 || pageIndex+1 > pageOrder.getPageList().size()-1) {
            return null;
        }
        String page = pageOrder.getPageList().get(pageIndex+1);
        if (isNestedFormFieldName(page)) {   // e.g. subform=breaks
            final String nestedFormName = page.substring(NESTED_FORM_PREFIX.length()).trim();
            final PageOrder nestedForm = nestedForms.get(nestedFormName);
            if (nestedForm.areDependenciesFulfilled(CollectionUtils.getAllFieldValues(session), nestedForm.getPageList().get(0))) {
                return nestedForm.getPageList().get(0);
            }
        }
        return null;
    }

    public String pageProcessing(final String currentPage, final Session session) {
        String nextPage = null;
        if (isLastPage(currentPage)) {
            nextPage = getSubFormProcessorForPage(currentPage).processLastPageInCollection(session, messageSource, getCurrentPageList(currentPage));
        }
        if (nextPage == null) {
            nextPage = nextPageProcessing(getNextPageInList(currentPage, session), session);
            if (nextPage == null) {
                nextPage = getNextPage(currentPage, session, false);
            }
            return nextPage;
        }
        return nextPage;
    }

    private String nextPageProcessing(final String nextPage, final Session session) {
        if (nextPage == null) {
            return nextPage;
        }
        if (isFirstPage(nextPage)) {
            return getSubFormProcessorForPage(nextPage).processFirstPageInCollection(nextPage, session);
        }
        return nextPage;
    }

    public String previousPageOnEmpty(final String currentPage, final String previousPage, final Session session) {
        return getSubFormProcessorForPage(currentPage).previousOnEmpty(currentPage, previousPage, session);
    }

    public String changeRecord(final String idToChange, final String currentPage, final Session session, final ValidationSummary validationSummary) {
        try {
            return getSubFormProcessorForPage(currentPage).processEditRowInCollection(idToChange, session);
        } catch (UnknownRecordException ure) {
            validationSummary.addFormError(idToChange, getMessage(currentPage + ".pageTitle"),  getMessage("edit.record"));
        }
        return null;
    }

    public String deleteRecord(final String idToDelete, final String currentPage, final Session session, final ValidationSummary validationSummary) {
        try {
            return getSubFormProcessorForPage(currentPage).processDeleteRowInCollection(idToDelete, currentPage, session);
        } catch (UnknownRecordException ure) {
            validationSummary.addFormError(idToDelete, getMessage(currentPage + ".pageTitle"),  getMessage("delete.record"));
        }
        return null;
    }

    public String deleteChangeRecord(final String idToDelete, final String idToChange, final String currentPage, final Session session, final ValidationSummary validationSummary) {
        String changeDeletePage = deleteRecord(idToDelete, currentPage, session, validationSummary);
        if (changeDeletePage == null) {
            changeDeletePage = changeRecord(idToChange, currentPage, session, validationSummary);
        }
        return changeDeletePage;
    }
}



