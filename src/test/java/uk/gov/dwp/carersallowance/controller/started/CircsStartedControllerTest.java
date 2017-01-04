package uk.gov.dwp.carersallowance.controller.started;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.Model;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultChangeOfCircsController;

import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = C3Application.class)
//@ContextConfiguration
//@WebAppConfiguration
@RunWith(MockitoJUnitRunner.class)
//@TestPropertySource(properties = { "circs.replica.enabled=false", "circs.replica.datafile = CircsReplicaDefault.xml"})
public class CircsStartedControllerTest {
    private CircsStartedController circsStartedController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Session session;

    @Mock
    private Model model;

    @InjectMocks
    private DefaultChangeOfCircsController defaultChangeOfCircsController;

    @InjectMocks
    private DefaultFormController defaultFormController;

    @Mock
    private SessionManager sessionManager;

    private List<String> attributes;

    @Before
    public void setUp() throws Exception {
        when(sessionManager.getSessionIdFromCookie(request)).thenReturn("12345");
        when(sessionManager.getSession(sessionManager.getSessionIdFromCookie(request))).thenReturn(session);
        circsStartedController = new CircsStartedController( defaultChangeOfCircsController);
        attributes = new ArrayList<>();
    }

    @Test
    public void testShowCircsForm() throws Exception {
        assertThat(circsStartedController.showCircsForm(request, response, model), is("redirect:/circumstances/report-changes/change-selection"));
    }

    @Test
    public void testPostCircsForm() throws Exception {
        when(session.getAttributeNames()).thenReturn(attributes);
        when(request.getServletPath()).thenReturn("/circumstances/report-changes/selection");
        assertThat(circsStartedController.postCircsForm(request, model), is("redirect:/circumstances/report-changes/change-selection#"));
    }

    @Test
    public void testShowCircsGBNIRForm() throws Exception {
        circsStartedController = new CircsStartedController( defaultChangeOfCircsController);
        assertThat(circsStartedController.showCircsForm(request, response, model), is("redirect:/circumstances/report-changes/change-selection"));
    }
}