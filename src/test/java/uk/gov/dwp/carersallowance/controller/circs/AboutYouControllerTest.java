package uk.gov.dwp.carersallowance.controller.circs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import uk.gov.dwp.carersallowance.controller.PageOrder;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.MessageSourceTestUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AboutYouControllerTest {


    private static final String SESSION_ATTRIBUTE = "changeTypeAnswer";
    private static final String STOPPED_PROVIDING_CARE = "stoppedProvidingCare";
    private static final String INCOME_CHANGED = "incomeChanged";
    private static final String BREAK_FROM_CARING = "breakFromCaring";
    private static final String CHANGE_OF_ADDRESS = "changeOfAddress";
    private static final String CHANGE_PAYMENT_DETAILS = "changePaymentDetails";
    private static final String SOMETHING_ELSE = "somethingElse";

    private static final String ABOUT_YOU_PAGE = "/circumstances/identification/about-you";
    private static final String STOPPED_CARING_PAGE     = "redirect:/circumstances/report-changes/stopped-caring#";
    private static final String EMPLOYMENT_CHANGE_PAGE  = "redirect:/circumstances/report-changes/employment-change#";
    private static final String BREAKS_IN_CARE_PAGE     = "redirect:/circumstances/report-changes/breaks-in-care#";
    private static final String ADDRESS_CHANGE_PAGE     = "redirect:/circumstances/report-changes/address-change#";
    private static final String PAYMENT_CHANGE_PAGE     = "redirect:/circumstances/report-changes/payment-change#";
    private static final String OTHER_CHANGE_PAGE       = "redirect:/circumstances/report-changes/other-change#";

    private AboutYouController controller;

    @Mock
    private SessionManager sessionManager;

    private MessageSource messageSource;

    @Mock
    private TransformationManager transformationManager;

    private PageOrder pageOrder;

    private Session session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @Before
    public void setup() throws Exception {
        session = new Session();
        messageSource = MessageSourceTestUtils.loadMessageSource("messages.properties");
        pageOrder = new PageOrder(messageSource, "circs");

        controller = new AboutYouController(sessionManager, messageSource, transformationManager, pageOrder);
        when(sessionManager.getSession(null)).thenReturn(session);
        when(request.getServletPath()).thenReturn(ABOUT_YOU_PAGE);
    }

    @Test
    public void givenGetCallThenShouldReturnCurrentPage() {
        session.setAttribute(SESSION_ATTRIBUTE, STOPPED_PROVIDING_CARE);
        Assert.assertThat(controller.getForm(request, model), is(equalTo(ABOUT_YOU_PAGE)));
    }

    @Test
    public void givenPostCallWithIncomeChangedResponseThenShouldReturnCorrectPage() {
        session.setAttribute(SESSION_ATTRIBUTE, INCOME_CHANGED);
        Assert.assertThat(controller.postForm(request, model), is(equalTo(EMPLOYMENT_CHANGE_PAGE)));
    }

    @Test
    public void givenPostCallWithStoppedProvidingCareResponseThenShouldReturnCorrectPage() {
        session.setAttribute(SESSION_ATTRIBUTE, STOPPED_PROVIDING_CARE);
        Assert.assertThat(controller.postForm(request, model), is(equalTo(STOPPED_CARING_PAGE)));
    }

    @Test
    public void givenPostCallWithBreakFromCaringResponseThenShouldReturnCorrectPage() {
        session.setAttribute(SESSION_ATTRIBUTE, BREAK_FROM_CARING);
        Assert.assertThat(controller.postForm(request, model), is(equalTo(BREAKS_IN_CARE_PAGE)));
    }

    @Test
    public void givenPostCallWithChangeOfAddressResponseThenShouldReturnCorrectPage() {
        session.setAttribute(SESSION_ATTRIBUTE, CHANGE_OF_ADDRESS);
        Assert.assertThat(controller.postForm(request, model), is(equalTo(ADDRESS_CHANGE_PAGE)));
    }

    @Test
    public void givenPostCallWithChangePaymentResponseThenShouldReturnCorrectPage() {
        session.setAttribute(SESSION_ATTRIBUTE, CHANGE_PAYMENT_DETAILS);
        Assert.assertThat(controller.postForm(request, model), is(equalTo(PAYMENT_CHANGE_PAGE)));
    }

    @Test
    public void givenPostCallWithSomethingElseResponseThenShouldReturnCorrectPage() {
        session.setAttribute(SESSION_ATTRIBUTE, SOMETHING_ELSE);
        Assert.assertThat(controller.postForm(request, model), is(equalTo(OTHER_CHANGE_PAGE)));
    }
}
