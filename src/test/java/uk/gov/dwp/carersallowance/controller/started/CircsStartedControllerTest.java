package uk.gov.dwp.carersallowance.controller.started;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultChangeOfCircsController;

import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by peterwhitehead on 23/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
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

    @Mock
    private SessionManager sessionManager;

    private List<String> attributes;

    @Before
    public void setUp() throws Exception {
        circsStartedController = new CircsStartedController("GB", defaultChangeOfCircsController);
        attributes = new ArrayList<>();
    }

    @Test
    public void testShowCircsForm() throws Exception {
        assertThat(circsStartedController.showCircsForm(request, response, model), is("redirect:/circumstances/report-changes/change-selection"));
    }

    @Test
    public void testPostCircsForm() throws Exception {
        when(sessionManager.getSessionIdFromCookie(request)).thenReturn("12345");
        when(sessionManager.getSession(sessionManager.getSessionIdFromCookie(request))).thenReturn(session);
        when(session.getAttributeNames()).thenReturn(attributes);
        when(request.getServletPath()).thenReturn("/circumstances/report-changes/selection");
        assertThat(circsStartedController.postCircsForm(request, model), is("redirect:/circumstances/report-changes/change-selection#"));
    }

    @Test
    public void testShowCircsGBNIRForm() throws Exception {
        circsStartedController = new CircsStartedController("GB-NIR", defaultChangeOfCircsController);
        assertThat(circsStartedController.showCircsForm(request, response, model), is("/circumstances/report-changes/selection"));
    }
}