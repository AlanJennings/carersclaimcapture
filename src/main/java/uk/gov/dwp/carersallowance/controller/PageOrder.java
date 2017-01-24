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

import uk.gov.dwp.carersallowance.utils.KeyValue;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.validations.Dependencies;

/**
 * @author David Hutchinson (drh@elegantsolutions.co.uk) on 4 Jan 2017.
 */
public class PageOrder {
    private static final Logger LOG = LoggerFactory.getLogger(PageOrder.class);

    private static final String ROOT_FORM_PAGE_KEY_FORMAT   = "form.pages";
    private static final String NESTED_FORM_PAGE_KEY_FORMAT = "subform.%s.pages";
    private static final String PAGE_DEPENDENCY_KEY_FORMAT  = "%s.dependency";
    private static final String SUBFORM_PSEUDO_FIELDNAME    = "subform.%s";
    private static final String NESTED_FORM_PREFIX          = "subform=";
    private static final String NESTED_FORM_FORMAT          = NESTED_FORM_PREFIX + "%s";

    private String                  formName;
    private List<String>            pageList;
    private Dependencies            dependencies;
    private Map<String, PageOrder>  nestedForms;
    private boolean                 isNestedForm;
    private PageOrder               parentForm;

    public PageOrder(MessageSource messageSource, String formName) throws ParseException {
        this(messageSource, formName, false, null);
    }

    public PageOrder(MessageSource messageSource, String formName, boolean isNestedForm, PageOrder parentForm) throws ParseException {
        LOG.trace("Started PageOrder.initPageList");
        Parameters.validateMandatoryArgs(messageSource,  "messageSource");
        try {
            LOG.debug("formName = {}");
            this.formName = formName;
            this.isNestedForm = isNestedForm;
            this.parentForm = parentForm;

            init(messageSource, formName);

        } finally {
            LOG.trace("Started PageOrder.initPageList");
        }
    }

    public String       getFormName()   { return formName; }
    public List<String> getPageList()   { return pageList; }
    public boolean      isNestedForm()  { return isNestedForm; }
    public PageOrder    getParentForm() { return parentForm; }

    private void init(MessageSource messageSource, String formName) throws ParseException {
        Map<String, PageOrder> tempNestedForms = new HashMap<>();
        String formKey;
        if(isNestedForm) {
            formKey = String.format(NESTED_FORM_PAGE_KEY_FORMAT, formName);
        } else {
            formKey = ROOT_FORM_PAGE_KEY_FORMAT;
        }

        List<String> rawPageList = initPageList(messageSource, formKey);
        for(String pageName: rawPageList) {
            if(isNestedFormFieldName(pageName)) {
                String nestedFormName = KeyValue.getValue(pageName, "=");
                PageOrder nestedPageOrder = new PageOrder(messageSource, nestedFormName, true, this);

                tempNestedForms.put(nestedFormName, nestedPageOrder);
            }
        }

        this.pageList = rawPageList;
        this.nestedForms = tempNestedForms;

        List<String> referenceFields = null;    // TODO used to check that the field being dependent on are valid (null == no checking)
        List<String> dependencyFieldList = new ArrayList<>(pageList);
        for(String nestedFormName : tempNestedForms.keySet()) {
            String subformPseudoFieldName = String.format(SUBFORM_PSEUDO_FIELDNAME, nestedFormName);
            dependencyFieldList.add(subformPseudoFieldName);
        }
        dependencies = new Dependencies(messageSource, PAGE_DEPENDENCY_KEY_FORMAT, dependencyFieldList, referenceFields);
    }

    private List<String> initPageList(MessageSource messageSource, String pagesKeyName) {
        LOG.trace("Started PageOrder.initPageList");
        LOG.debug("formName = {}, pagesKeyName = {}", formName, pagesKeyName);
        Parameters.validateMandatoryArgs(messageSource,  "messageSource");
        if(pagesKeyName == null) {
            return null;
        }
        String pageList = messageSource.getMessage(pagesKeyName, null, null, Locale.getDefault());
        if(pageList == null) {
            return null;
        }

        String[] pagesArray = pageList.split(",");

        List<String> pages = new ArrayList<>();
        for(String pageName: pagesArray) {
            pageName = pageName.trim();
            if(pageName.equals("") == false) {
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
    public String getNextPage(String currentPage, Map<String, String[]> session) {
        String nextPage;
        if(StringUtils.isBlank(currentPage)) {
            nextPage = pageList.get(0);

        } else {
            // check if we are on a nested form
            PageOrder currentPageOrder = getCurrentPageOrder(currentPage);
            if(currentPageOrder != this) {
                return currentPageOrder.getNextPage(currentPage, session);
            }

            int index = pageList.indexOf(currentPage);
            if(index == -1 || index == (pageList.size() -1)) {
                if(currentPageOrder.isNestedForm() == false) {
                    return null;  // not found or last
                }

                // nestedForms loop back to their start page.
                // the start page in a nested form has a different dependency that the nested form itself
                // so by using the first page dependency it can loop back to any page in the nested form.
                // Therefore also need to explicitly check the subform dependency (and therefore exit) criteria
                String subformName = currentPageOrder.getFormName();
                String psuedoFieldName = String.format(SUBFORM_PSEUDO_FIELDNAME, subformName);
                PageOrder parentForm = currentPageOrder.getParentForm();
                if(parentForm.areDependenciesFulfilled(session, psuedoFieldName) == false) {
                    // return to the point we dropped out of the parent
                    String nestedFormName = String.format(NESTED_FORM_FORMAT, subformName);
                    return parentForm.getNextPage(nestedFormName, session);
                } else {
                    nextPage = pageList.get(0);
                }
            } else {
                nextPage = pageList.get(index + 1);
            }
        }

        String dependencyNextPage = nextPage;
        if(isNestedFormFieldName(nextPage)) {
            dependencyNextPage = convertNestedDeclarationToPseudoFieldName(nextPage);
        }
        if(areDependenciesFulfilled(session, dependencyNextPage) == false) {
            return getNextPage(nextPage, session);
        }

        if(isNestedFormFieldName(nextPage)) {   // e.g. subform=breaks
            String nestedFormName = nextPage.substring(NESTED_FORM_PREFIX.length()).trim();
            if(nestedForms.containsKey(nestedFormName) == false) {
                throw new IllegalArgumentException("Unknown nested form: " + nestedFormName);
            }
            PageOrder nestedForm = nestedForms.get(nestedFormName);
            nextPage = nestedForm.getNextPage(null, session);
        }

        return nextPage;
    }

    private boolean areDependenciesFulfilled(Map<String, String[]> session, String psuedoFieldName) {
        return dependencies.areDependenciesFulfilled(psuedoFieldName, session);
    }

    /**
     * Return the pseudo field name for a nested form, or the original unchanged
     * name if it is not a nested form declaration
     *
     * @param nestedDeclaration of the form: subform=<subform name>
     */
    private String convertNestedDeclarationToPseudoFieldName(String nestedDeclaration) {
        if(nestedDeclaration == null) {
            return null;
        }
        if(nestedDeclaration.startsWith(NESTED_FORM_PREFIX)) {
            KeyValue keyValue = new KeyValue(nestedDeclaration, "=");
            String nestedFormName = keyValue.getValue();
            return String.format(SUBFORM_PSEUDO_FIELDNAME, nestedFormName);
        }

        return nestedDeclaration;  // return it unchanged
    }

    private boolean isNestedFormFieldName(String nextPage) {
        return nextPage != null && nextPage.startsWith(NESTED_FORM_PREFIX);
    }

   /**
     * If current page is blank, then return the first page
     */
    public String getPreviousPage(String currentPage, Map<String, String[]> session) {
        String previousPage;
        if(StringUtils.isBlank(currentPage)) {
            previousPage = pageList.get(0);

        } else {
            // get the correct PageOrder object
            PageOrder currentPageOrder = getCurrentPageOrder(currentPage);
            if(currentPageOrder != this) {
                return currentPageOrder.getPreviousPage(currentPage, session);
            }

            int index = pageList.indexOf(currentPage);
            if(index == -1 || index == 0) { // if not found or the first page
                if(currentPageOrder.isNestedForm() == false) {
                    // top level form, so there is no previous
                    return null;
                }

                // ok, so this is a nested form.  Previous page for a nested form
                // goes to the page of the parent just prior to the nested form entry
                // unless dependencies don't allow in which case the one before that
                // and so on
                PageOrder parentForm = currentPageOrder.getParentForm();
                String subFormName = currentPageOrder.getFormName();
                String nestedFormName = String.format(NESTED_FORM_FORMAT, subFormName); // e.g. subform=breaks
                return parentForm.getPreviousPage(nestedFormName, session);
            } else {
                previousPage = pageList.get(index - 1);
            }
        }

        // if the previous page has dependencies that are not satisfied iterate backwards
        String dependencyPreviousPage = previousPage;
        if(isNestedFormFieldName(previousPage)) {
            dependencyPreviousPage = convertNestedDeclarationToPseudoFieldName(previousPage);
        }
        if(areDependenciesFulfilled(session, dependencyPreviousPage) == false) {
            return getPreviousPage(previousPage, session);
        }

        if(isNestedFormFieldName(previousPage)) {   // e.g. subform=breaks
            String nestedFormName = previousPage.substring(NESTED_FORM_PREFIX.length()).trim();
            if(nestedForms.containsKey(nestedFormName) == false) {
                throw new IllegalArgumentException("Unknown nested form: " + nestedFormName);
            }
            PageOrder nestedForm = nestedForms.get(nestedFormName);
            previousPage = nestedForm.getNextPage(null, session);   // get the first page of the nested form
        }

        return previousPage;
    }

    private PageOrder getCurrentPageOrder(String currentPage) {
        if(pageList.contains(currentPage)) {
            return this;
        }

        for(PageOrder nestedForm : nestedForms.values()) {
            if(nestedForm.getCurrentPageOrder(currentPage) != null) {
                return nestedForm;
            }
        }

        return null;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

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
}



