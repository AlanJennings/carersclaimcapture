package uk.gov.dwp.carersallowance.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;

import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.utils.KeyValue;
import uk.gov.dwp.carersallowance.utils.MessageSourceTestUtils;
import uk.gov.dwp.carersallowance.utils.Parameters;

/**
 * @author David Hutchinson (drh@elegantsolutions.co.uk) on 20 Jan 2017.
 *
 * Note: subform=<subform name> entries are not part of the actual navigation, but
 * are placehoders for the subforms themselves.  So when navigating if their dependencies
 * are not satisfied they will not appear, and if they are then the page ordering will
 * include the pages of the subform until the dependency is not satisfied.
 */
public class PageOrderTest {
    private static final String MESSAGE_PROPERTIES = "messages.properties";

    private final static String[] FORM_PAGES_FULL_LIST = {
            "/allowance/benefits",
            "/allowance/eligibility",
            "/allowance/approve",
            "/disclaimer/disclaimer",
            "/third-party/third-party",
            "/your-claim-date/claim-date",
            "/about-you/your-details",
            "/about-you/marital-status",
            "/about-you/contact-details",
            "/about-you/nationality-and-residency",
            "/about-you/other-eea-state-or-switzerland",
            "/your-partner/personal-details",
            "/care-you-provide/their-personal-details",
//            "subform=breaks",
            "/care-you-provide/more-about-the-care",
            "/education/your-course-details",
            "/your-income/your-income",
//            "subform=employment",
            "/your-income/self-employment/self-employment-dates",
            "/your-income/self-employment/pensions-and-expenses",
            "/your-income/employment/additional-info",
            "/your-income/statutory-sick-pay",
            "/your-income/smp-spa-sap",
            "/your-income/fostering-allowance",
            "/your-income/direct-payment",
            "/your-income/other-income",
            "/pay-details/how-we-pay-you",
            "/information/additional-info",
            "/preview",
            "/consent-and-declaration/declaration",
            "/thankyou/apply-carers"
    };

    private final static String[] SUBFORM_BREAKS_PAGES_FULL_LIST = {
            "/breaks/breaks-in-care",
            "/breaks/break-in-hospital",
            "/breaks/break-in-respite-care",
            "/breaks/break-somewhere-else"
    };
    private final static String[] SUBFORM_EMPLOYMENT_PAGES_FULL_LIST = {
            "/your-income/employment/been-employed",
            "/your-income/employment/last-wage",
            "/your-income/employment/job-details",
            "/your-income/employment/about-expenses"
    };

    private final static String[] ALL_DEPENDENT_FIELDS = {
            "/your-income/self-employment/self-employment-dates",
            "/your-income/self-employment/pensions-and-expenses",
            "/your-income/statutory-sick-pay",
            "/your-income/smp-spa-sap",
            "/your-income/fostering-allowance",
            "/your-income/direct-payment",
            "/your-income/other-income"
    };

    private final static String[] DEPENDECIES = {
            "beenEmployedSince6MonthsBeforeClaim=yes",
            "beenSelfEmployedSince1WeekBeforeClaim=yes",
            "yourIncome_sickpay=yes",
            "yourIncome_patmatadoppay=yes",
            "yourIncome_fostering=yes",
            "yourIncome_directpay=yes",
            "yourIncome_anyother=yes",

            "moreBreaksInCare=yes",                     // breaks dependency
            "beenEmployedSince6MonthsBeforeClaim=yes"   // employment dependency
    };

    private static final String LAST_PAGE          = FORM_PAGES_FULL_LIST[FORM_PAGES_FULL_LIST.length - 1];

    private final static String PAGE_BEFORE_BREAKS = "/care-you-provide/their-personal-details";
    private final static String PAGE_AFTER_BREAKS  = "/care-you-provide/more-about-the-care";

    private final static String PAGE_BEFORE_EMPLOYMENT = "/your-income/your-income";
    private final static String PAGE_AFTER_EMPLOYMENT  = "/your-income/employment/additional-info";    // self-employment dependencies satisfied

    private MessageSource messageSource;
    private PageOrder     pageOrder;

    @Before
    public void init() throws IOException, ParseException {
        messageSource = MessageSourceTestUtils.loadMessageSource(MESSAGE_PROPERTIES);
        pageOrder = new PageOrder(messageSource, "claim");
    }

    private Session addToSession(Session session, List<String> keyValues) {
        if(session == null) {
            session = new Session();
        }

        if(keyValues != null) {
            for(String keyValue : keyValues) {
                addToSession(session, keyValue);
            }
        }

        return session;
    }

    private String addToSession(Session session, String keyValue) {
        KeyValue keyValueObj = new KeyValue(keyValue, "=");
        return addToSession(session, keyValueObj.getKey(), keyValueObj.getValue());
    }

    private String addToSession(Session session, String key, String value) {
        Parameters.validateMandatoryArgs(session,  "session");
        String oldValue = (String)session.getAttribute(key);
        session.setAttribute(key, value);

        if(oldValue == null || oldValue.length() == 0) {
            return null;
        } else {
            return oldValue;
        } 
    }

    private void assertSameContents(List<String> expected, List<String> actual) {
        Set<String> expectedSet = new HashSet<>(expected);
        Set<String> actualSet = new HashSet<>(actual);

        Set<String> extra = new HashSet<>(actualSet);
        extra.removeAll(expectedSet);

        Set<String> missing = new HashSet<>(expectedSet);
        missing.removeAll(actualSet);

        Set<String> empty = new HashSet<>();
        Assert.assertEquals("unexpected additional values: ", empty, extra);
        Assert.assertEquals("missing values: ", empty, missing);
    }

    /*******************   START OF NEXT-PAGE TESTS *************************/

    @Test
    public void noDependenciesSatisfiedNextTest() throws ParseException {
//        List<String> noSatisfiedDependenciesList = new ArrayList<>(Arrays.asList(FORM_PAGES_FULL_LIST));
//        List<String> allDependantFields = Arrays.asList(ALL_DEPENDENT_FIELDS);
//        noSatisfiedDependenciesList.removeAll(allDependantFields);
//
//        Session session = new Session();
//
//        String currentPage = null;
//        List<String> pages = new ArrayList<>();
//        for(int count = 0; count < 100; count++) {
//            currentPage = pageOrder.getNextPage(currentPage, session);
//            if(currentPage == null) {
//                break;
//            }
//            System.err.println(currentPage);
//            pages.add(currentPage);
//        }
//
//        Assert.assertNotEquals("endless loop detected in pages", 100, pages.size());
//        assertSameContents(noSatisfiedDependenciesList, pages);
//
//        Assert.assertEquals(noSatisfiedDependenciesList, pages);    // check order
    }

    @Test
    public void allDependenciesSatisfiedNextTest() throws ParseException {
//        List<String> allSatisfiedDependenciesList = new ArrayList<>(Arrays.asList(FORM_PAGES_FULL_LIST));
//
//        int breaksIndex = allSatisfiedDependenciesList.indexOf(PAGE_BEFORE_BREAKS);
//        allSatisfiedDependenciesList.addAll(breaksIndex + 1, Arrays.asList(SUBFORM_BREAKS_PAGES_FULL_LIST));
//
//        int employmentIndex = allSatisfiedDependenciesList.indexOf(PAGE_BEFORE_EMPLOYMENT);
//        allSatisfiedDependenciesList.addAll(employmentIndex + 1, Arrays.asList(SUBFORM_EMPLOYMENT_PAGES_FULL_LIST));
//
//        Session session = new Session();
//        addToSession(session, Arrays.asList(DEPENDECIES));
//
//        String currentPage = null;
//        List<String> pages = new ArrayList<>();
//        for(int count = 0; count < 100; count++) {
//            currentPage = pageOrder.getNextPage(currentPage, session);
//            if(currentPage == null) {
//                break;
//            }
//            System.err.println(currentPage);
//            pages.add(currentPage);
//
//            if(currentPage.equals(SUBFORM_BREAKS_PAGES_FULL_LIST[0])) {
//                session.removeAttribute("moreBreaksInCare");
//            }
//            if(currentPage.equals(SUBFORM_EMPLOYMENT_PAGES_FULL_LIST[0])) {
//                session.removeAttribute("beenEmployedSince6MonthsBeforeClaim");
//            }
//        }
//
//        Assert.assertNotEquals("endless loop detected in pages", 100, pages.size());
//        assertSameContents(allSatisfiedDependenciesList, pages);
//
//        Assert.assertEquals(allSatisfiedDependenciesList, pages);    // check order
    }

    @Test
    public void breaksDependencyNotSatisfiedNextTest() throws ParseException {
        Session session = new Session();

        String currentPage = PAGE_BEFORE_BREAKS;
        String nextPage = pageOrder.getNextPage(currentPage, session);

        Assert.assertEquals(PAGE_AFTER_BREAKS, nextPage);
    }

    @Test
    public void breaksDependencySatisfiedNextTest() throws ParseException {
//        Session session = new Session();
//        addToSession(session, "moreBreaksInCare=yes");
//
//        String currentPage = PAGE_BEFORE_BREAKS;
//        String nextPage = pageOrder.getNextPage(currentPage, session);
//
//        Assert.assertEquals(SUBFORM_BREAKS_PAGES_FULL_LIST[0], nextPage);
    }

    @Test
    public void breaksDependencySatisfiedLoopNextTest() throws ParseException {
//
//        Session session = new Session();
//        addToSession(session, "moreBreaksInCare=yes");
//
//        String currentPage = PAGE_BEFORE_BREAKS;
//        String nextPage = currentPage;
//        for(int index = 0; index < SUBFORM_BREAKS_PAGES_FULL_LIST.length; index++) {
//            nextPage = pageOrder.getNextPage(nextPage, session);
//            Assert.assertEquals("index = " + index, SUBFORM_BREAKS_PAGES_FULL_LIST[index], nextPage);
//        }
//
//        nextPage = pageOrder.getNextPage(nextPage, session);
//        Assert.assertEquals(SUBFORM_BREAKS_PAGES_FULL_LIST[0], nextPage);
//
//        session.removeAttribute("moreBreaksInCare");
//
//        String lastBreaksPage = SUBFORM_BREAKS_PAGES_FULL_LIST[SUBFORM_BREAKS_PAGES_FULL_LIST.length -1];
//        nextPage = pageOrder.getNextPage(lastBreaksPage, session);
//
//        Assert.assertEquals(PAGE_AFTER_BREAKS, nextPage);
    }
    @Test
    public void employmentDependencyNotSatisfiedNextTest() throws ParseException {
        Session session = new Session();

        String currentPage = PAGE_BEFORE_EMPLOYMENT;
        String nextPage = pageOrder.getNextPage(currentPage, session);

        Assert.assertEquals(PAGE_AFTER_EMPLOYMENT, nextPage);
    }

    @Test
    public void employmentDependencySatisfiedNextTest() throws ParseException {
        Session session = new Session();
        addToSession(session, "beenEmployedSince6MonthsBeforeClaim=yes");

        String currentPage = PAGE_BEFORE_EMPLOYMENT;
        String nextPage = pageOrder.getNextPage(currentPage, session);

        Assert.assertEquals(SUBFORM_EMPLOYMENT_PAGES_FULL_LIST[0], nextPage);
    }

    @Test
    public void employmentDependencySatisfiedLoopNextTest() throws ParseException {
//
//        Session session = new Session();
//        addToSession(session, "beenEmployedSince6MonthsBeforeClaim=yes");
//        addToSession(session, "moreEmployment=yes");
//
//        String currentPage = PAGE_BEFORE_EMPLOYMENT;
//        String nextPage = currentPage;
//        for(int index = 0; index < SUBFORM_EMPLOYMENT_PAGES_FULL_LIST.length; index++) {
//            nextPage = pageOrder.getNextPage(nextPage, session);
//            Assert.assertEquals("index = " + index, SUBFORM_EMPLOYMENT_PAGES_FULL_LIST[index], nextPage);
//        }
//
//        nextPage = pageOrder.getNextPage(nextPage, session);
//        Assert.assertEquals(SUBFORM_EMPLOYMENT_PAGES_FULL_LIST[0], nextPage);
//
//        session.removeAttribute("beenEmployedSince6MonthsBeforeClaim");
//
//        String lastEmploymentPage = SUBFORM_EMPLOYMENT_PAGES_FULL_LIST[SUBFORM_EMPLOYMENT_PAGES_FULL_LIST.length -1];
//        nextPage = pageOrder.getNextPage(lastEmploymentPage, session);
//
//        Assert.assertEquals(PAGE_AFTER_EMPLOYMENT, nextPage);
    }

    /*******************   END OF NEXT-PAGE TESTS *************************/

    /****************   START OF PREVIOUS-PAGE TESTS *************************/

    @Test
    public void noDependenciesSatisfiedPreviousTest() throws ParseException {
//        List<String> noSatisfiedDependenciesList = new ArrayList<>(Arrays.asList(FORM_PAGES_FULL_LIST));
//        List<String> allDependantFields = Arrays.asList(ALL_DEPENDENT_FIELDS);
//        noSatisfiedDependenciesList.removeAll(allDependantFields);
//
//        Session session = new Session();
//
//        String currentPage = LAST_PAGE;
//        List<String> pages = new ArrayList<>();
//        pages.add(currentPage);
//
//        for(int count = 0; count < 100; count++) {
//            currentPage = pageOrder.getPreviousPage(currentPage, session);
//            if(currentPage == null) {
//                break;
//            }
//            System.err.println(currentPage);
//            pages.add(currentPage);
//        }
//        List<String> pageReversed = new ArrayList<>(pages);
//        Collections.reverse(pageReversed);
//
//        Assert.assertNotEquals("endless loop detected in pages", 100, pages.size());
//        assertSameContents(noSatisfiedDependenciesList, pageReversed);
//
//        Assert.assertEquals(noSatisfiedDependenciesList, pageReversed);    // check order
    }

    @Test
    public void allDependenciesSatisfiedPreviousTest() throws ParseException {
//        List<String> allSatisfiedDependenciesList = new ArrayList<>(Arrays.asList(FORM_PAGES_FULL_LIST));
//
//        // just add the entry (first) page for the subforms, as we don't back in to the last page, but the first (of subforms)
//        int breaksIndex = allSatisfiedDependenciesList.indexOf(PAGE_BEFORE_BREAKS);
//        allSatisfiedDependenciesList.add(breaksIndex + 1, SUBFORM_BREAKS_PAGES_FULL_LIST[0]);
//
//        int employmentIndex = allSatisfiedDependenciesList.indexOf(PAGE_BEFORE_EMPLOYMENT);
//        allSatisfiedDependenciesList.add(employmentIndex + 1, SUBFORM_EMPLOYMENT_PAGES_FULL_LIST[0]);
//
//        // we don't add the sub-forms, as we don't drop into them when moving backwards
//        Session session = new Session();
//        addToSession(session, Arrays.asList(DEPENDECIES));
//
//        String currentPage = LAST_PAGE;
//        List<String> pages = new ArrayList<>();
//        pages.add(currentPage);
//        for(int count = 0; count < 100; count++) {
//            currentPage = pageOrder.getPreviousPage(currentPage, session);
//            if(currentPage == null) {
//                break;
//            }
//            System.err.println(currentPage);
//            pages.add(currentPage);
//        }
//
//        List<String> pageReversed = new ArrayList<>(pages);
//        Collections.reverse(pageReversed);
//
//        Assert.assertNotEquals("endless loop detected in pages", 100, pages.size());
//        assertSameContents(allSatisfiedDependenciesList, pageReversed);
//
//        Assert.assertEquals(allSatisfiedDependenciesList, pageReversed);    // check order
    }

    @Test
    public void breaksDependencyNotSatisfiedPreviousTest() throws ParseException {
        Session session = new Session();

        String currentPage = PAGE_AFTER_BREAKS;
        String previousPage = pageOrder.getPreviousPage(currentPage, session);

        Assert.assertEquals(PAGE_BEFORE_BREAKS, previousPage);
    }

    @Test
    public void breaksDependencyLoopPreviousTest() throws ParseException {
//
//        Session session = new Session();
//
//        // back from the last page right out of breaks and the next field
//        String breaksLastPage = SUBFORM_BREAKS_PAGES_FULL_LIST[SUBFORM_BREAKS_PAGES_FULL_LIST.length - 1];
//        String previousPage = breaksLastPage;
//        for(int index = SUBFORM_BREAKS_PAGES_FULL_LIST.length -1; index >= 0; index--) {
//            Assert.assertEquals("index = " + index, SUBFORM_BREAKS_PAGES_FULL_LIST[index], previousPage);
//            previousPage = pageOrder.getPreviousPage(previousPage, session);
//        }
//
//        Assert.assertEquals(PAGE_BEFORE_BREAKS, previousPage);
    }

    @Test
    public void employmentDependencyNotSatisfiedPreviousTest() throws ParseException {
        Session session = new Session();

        String currentPage = PAGE_AFTER_EMPLOYMENT;
        String previousPage = pageOrder.getPreviousPage(currentPage, session);

        Assert.assertEquals(PAGE_BEFORE_EMPLOYMENT, previousPage);
    }

    @Test
    public void employmentDependencySatisfiedLoopPreviousTest() throws ParseException {
//
//        Session session = new Session();
//        addToSession(session, "beenEmployedSince6MonthsBeforeClaim=yes");
//        addToSession(session, "moreEmployment=yes");
//
//        String lastEmploymentPage = SUBFORM_EMPLOYMENT_PAGES_FULL_LIST[SUBFORM_EMPLOYMENT_PAGES_FULL_LIST.length - 1];
//        String previousPage = lastEmploymentPage;
//        for(int index = SUBFORM_EMPLOYMENT_PAGES_FULL_LIST.length -1; index >= 0; index--) {
//            Assert.assertEquals("index = " + index, SUBFORM_EMPLOYMENT_PAGES_FULL_LIST[index], previousPage);
//            previousPage = pageOrder.getPreviousPage(previousPage, session);
//        }
//
//        Assert.assertEquals(PAGE_BEFORE_EMPLOYMENT, previousPage);
    }

    // TODO probably need to do some more tests with different page lists (various pathological cases)
}
