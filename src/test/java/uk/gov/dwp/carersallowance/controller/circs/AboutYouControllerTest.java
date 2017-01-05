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
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AboutYouControllerTest {


    private static final String SESSION_ATTRIBUTE = "changeTypeAnswer";
    private static final String STOPPED_PROVIDING_CARE = "stoppedProvidingCare";
    private static final String INCOME_CHANGED = "incomeChanged";
    private static final String PATIENT_AWAY = "patientAway";
    private static final String CARER_AWAY = "carerAway";
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

    @InjectMocks
    private AboutYouController controller;

    @Mock
    private SessionManager sessionManager;
    @Mock
    private MessageSource messageSource;

    private Session session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @Before
    public void setup() {
        session = new Session();
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
    public void givenPostCallWithPatientAwayResponseThenShouldReturnCorrectPage() {
        session.setAttribute(SESSION_ATTRIBUTE, PATIENT_AWAY);
        Assert.assertThat(controller.postForm(request, model), is(equalTo(BREAKS_IN_CARE_PAGE)));
    }

    @Test
    public void givenPostCallWithCarerAwayResponseThenShouldReturnCorrectPage() {
        session.setAttribute(SESSION_ATTRIBUTE, CARER_AWAY);
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
