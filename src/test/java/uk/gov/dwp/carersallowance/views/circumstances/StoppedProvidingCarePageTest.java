package uk.gov.dwp.carersallowance.views.circumstances;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.dwp.carersallowance.controller.circs.StoppedProvidingCareController;
import uk.gov.dwp.carersallowance.controller.started.C3Application;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = C3Application.class)
@ContextConfiguration
@WebAppConfiguration
public class StoppedProvidingCarePageTest {

    private static final String STOPPED_PROVIDING_CARE_PAGE = "/circumstances/report-changes/stopped-caring";
    private static final String NEXT_PAGE = "/circumstances/consent-and-declaration/declaration#";
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @InjectMocks
    private StoppedProvidingCareController controller;

    @Mock
    private HttpServletRequest request;

    @Mock
    private SessionManager sessionManager;

    private Session session;

    @Before
    public void setup() {
        //  Setup webapp context
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Get the controller from the webapp context
        controller = (StoppedProvidingCareController) webApplicationContext.getBean("stoppedProvidingCareController");

        // Setup session data
        session = new Session();
        session.setAttribute("changeTypeAnswer", "stoppedProvidingCare");

        // Inject mocks into the controller
        MockitoAnnotations.initMocks(this);

        // Set up mock responses - mocks session ID
        when(sessionManager.getSessionIdFromCookie(anyObject())).thenReturn("AB123456");
        when(sessionManager.getSession(sessionManager.getSessionIdFromCookie(anyObject()))).thenReturn(session);
    }

    @Test
    public void givenStoppedProvidingCarePageRequestedThenPagePresented() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(STOPPED_PROVIDING_CARE_PAGE);
        requestBuilder.servletPath(STOPPED_PROVIDING_CARE_PAGE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name(STOPPED_PROVIDING_CARE_PAGE))
                .andReturn();
    }

    @Test
    public void givenStoppedProvidingCareRequestThenNextPageReturned() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(STOPPED_PROVIDING_CARE_PAGE);
        requestBuilder.servletPath(STOPPED_PROVIDING_CARE_PAGE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + NEXT_PAGE))
                .andReturn();
    }
}
