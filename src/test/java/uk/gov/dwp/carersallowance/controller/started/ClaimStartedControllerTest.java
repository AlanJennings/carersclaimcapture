package uk.gov.dwp.carersallowance.controller.started;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
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

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClaimStartedControllerTest {
    private ClaimStartedController claimStartedController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Session session;

    @Mock
    private Model model;

    @InjectMocks
    private DefaultFormController defaultFormController;

    @Mock
    private SessionManager sessionManager;

    private List<String> attributes;

    @Before
    public void setUp() throws Exception {
        when(sessionManager.getSessionIdFromCookie(request)).thenReturn("12345");
        when(sessionManager.getSession(sessionManager.getSessionIdFromCookie(request))).thenReturn(session);
        claimStartedController = new ClaimStartedController(defaultFormController);
        attributes = new ArrayList<>();
    }

    @Test
    public void testShowForm() throws Exception {
        assertThat(claimStartedController.showForm(request, response, model), is("/allowance/benefits"));
    }

    @Test
    public void testPostForm() throws Exception {
        when(session.getAttributeNames()).thenReturn(attributes);
        when(request.getServletPath()).thenReturn("/allowance/benefits");
        assertThat(claimStartedController.postForm(request, model), is("redirect:/allowance/eligibility#"));
    }
}