package uk.gov.dwp.carersallowance.controller.started;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import uk.gov.dwp.carersallowance.session.CookieManager;
import uk.gov.dwp.carersallowance.session.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Enumeration;

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
    private CookieManager cookieManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private SessionManager sessionManager;

    @Mock
    private MessageSource messageSource;

    @Mock
    private Enumeration<String> attributes;

    @Before
    public void setUp() throws Exception {
        claimStartedController = new ClaimStartedController(sessionManager, messageSource, cookieManager);
    }

    @Test
    public void testShowForm() throws Exception {
        assertThat(claimStartedController.showForm(request, response, model), is("/allowance/benefits"));
    }

    @Test
    public void testPostForm() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttributeNames()).thenReturn(attributes);
        when(request.getServletPath()).thenReturn("/allowance/benefits");
        assertThat(claimStartedController.postForm(request, session, model), is("redirect:/allowance/eligibility#"));
    }
}