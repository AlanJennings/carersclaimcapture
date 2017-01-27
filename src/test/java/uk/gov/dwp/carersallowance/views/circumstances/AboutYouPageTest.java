package uk.gov.dwp.carersallowance.views.circumstances;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import uk.gov.dwp.carersallowance.controller.PageOrder;
import uk.gov.dwp.carersallowance.controller.circs.AboutYouController;
import uk.gov.dwp.carersallowance.controller.started.C3Application;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.MessageSourceTestUtils;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = C3Application.class)
@ContextConfiguration
@WebAppConfiguration
public class AboutYouPageTest {

    private static final String ABOUT_YOU_PAGE = "/circumstances/identification/about-you";
    private static final String NEXT_PAGE = "/circumstances/report-changes/stopped-caring#";

    private MockMvc mockMvc;

    private AboutYouController controller;

    private MessageSource messageSource;

    @Mock
    private TransformationManager transformationManager;

    private PageOrder pageOrder;

    @Mock
    private HttpServletRequest request;

    @Mock
    private SessionManager sessionManager;

    private Session session;

    @Before
    public void setup() throws Exception {
        // Inject mocks into the controller
        MockitoAnnotations.initMocks(this);

        messageSource = MessageSourceTestUtils.loadMessageSource("messages.properties");
        pageOrder = new PageOrder(messageSource, "circs");
        controller = new AboutYouController(sessionManager, messageSource, transformationManager, pageOrder);

        //  Setup webapp context
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver).build();

        // Setup session data
        session = new Session();
        session.setAttribute("changeTypeAnswer", "stoppedProvidingCare");

        // Set up mock responses - mocks session ID
        when(sessionManager.getSessionIdFromCookie(anyObject())).thenReturn("AB123456");
        when(sessionManager.getSession(sessionManager.getSessionIdFromCookie(anyObject()))).thenReturn(session);
    }

    @Test
    public void givenAboutYouPageRequestedThenAboutYouPagePresented() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(ABOUT_YOU_PAGE);
        requestBuilder.servletPath(ABOUT_YOU_PAGE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name(ABOUT_YOU_PAGE))
                .andReturn();
    }

    @Test
    public void givenAboutYouPagePostRequestThenAppropriatePageReturned() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(ABOUT_YOU_PAGE);
        requestBuilder.servletPath(ABOUT_YOU_PAGE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + NEXT_PAGE))
                .andReturn();
    }
}


