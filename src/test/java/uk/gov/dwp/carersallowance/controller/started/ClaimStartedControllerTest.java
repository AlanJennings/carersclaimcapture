package uk.gov.dwp.carersallowance.controller.started;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import uk.gov.dwp.carersallowance.controller.PageOrder;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultFormController;

import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.MessageSourceTestUtils;

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
    private final static String MAPPING_FILE = "xml.mapping.claim";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Session session;

    @Mock
    private Model model;

    private DefaultFormController defaultFormController;

    @Mock
    private SessionManager sessionManager;

    @Mock
    private ChangeLanguageProcess changeLanguageProcess;

    @Mock
    private TransformationManager transformationManager;

    private MessageSource messageSource;

    private PageOrder pageOrder;

    private List<String> attributes;

    @Before
    public void setUp() throws Exception {
        messageSource = MessageSourceTestUtils.loadMessageSource("messages.properties");
        pageOrder = new PageOrder(messageSource, "claim");
        defaultFormController = new DefaultFormController(sessionManager, messageSource, transformationManager, pageOrder);

        when(sessionManager.getSessionIdFromCookie(request)).thenReturn("12345");
        when(request.getServletPath()).thenReturn("/allowance/benefits");
        when(sessionManager.getSession(sessionManager.getSessionIdFromCookie(request))).thenReturn(session);
        claimStartedController = new ClaimStartedController(false, "", defaultFormController, MAPPING_FILE, changeLanguageProcess, sessionManager);
        attributes = new ArrayList<>();
    }

    @Test
    public void testGetForm() throws Exception {
        assertThat(claimStartedController.getForm(request, response, model), is("/allowance/benefits"));
    }

    @Test
    public void testPostForm() throws Exception {
        when(session.getAttributeNames()).thenReturn(attributes);
        when(request.getServletPath()).thenReturn("/allowance/benefits");
        assertThat(claimStartedController.postForm(request, model), is("redirect:/allowance/eligibility#"));
    }
}