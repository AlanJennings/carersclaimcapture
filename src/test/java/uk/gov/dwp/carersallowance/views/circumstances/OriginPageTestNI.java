package uk.gov.dwp.carersallowance.views.circumstances;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import uk.gov.dwp.carersallowance.controller.PageOrder;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultChangeOfCircsController;
import uk.gov.dwp.carersallowance.controller.started.C3Application;
import uk.gov.dwp.carersallowance.controller.started.ChangeLanguageProcess;
import uk.gov.dwp.carersallowance.controller.started.CircsStartedController;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.MessageSourceTestUtils;

import javax.servlet.http.HttpServletRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = C3Application.class)
@ContextConfiguration
@WebAppConfiguration
@TestPropertySource(properties = { "origin.tag=GB-NIR", "session.data.to.db=false"})
public class OriginPageTestNI {

    private static final String ORIGIN_PAGE = "/circumstances/report-changes/selection";
    private final static String MAPPING_FILE = "xml.mapping.circs";

    private MockMvc mockMvc;

    private CircsStartedController controller;

    private MessageSource messageSource;

    @Mock
    private TransformationManager transformationManager;

    private PageOrder pageOrder;

    @Mock
    private HttpServletRequest request;

    @Mock
    private SessionManager sessionManager;

    private Session session;

    @Mock
    private ChangeLanguageProcess changeLanguageProcess;

    private DefaultChangeOfCircsController defaultChangeOfCircsController;

    @Before
    public void setup() throws Exception {
        // Inject mocks into the controller
        MockitoAnnotations.initMocks(this);

        session = new Session();
        when(sessionManager.getSessionIdFromCookie(anyObject())).thenReturn("AB123456");
        when(sessionManager.getSession(sessionManager.getSessionIdFromCookie(anyObject()))).thenReturn(session);

        messageSource = MessageSourceTestUtils.loadMessageSource("messages.properties");
        pageOrder = new PageOrder(messageSource, "circs");
        defaultChangeOfCircsController = new DefaultChangeOfCircsController(sessionManager, messageSource, transformationManager, pageOrder);
        controller = new CircsStartedController(defaultChangeOfCircsController, MAPPING_FILE, false, null, "GB-NIR", changeLanguageProcess);

        //  Setup webapp context
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver).build();
    }

    @Test
    public void givenOriginPageRequestedThenOriginPagePresented() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(ORIGIN_PAGE);
        requestBuilder.servletPath(ORIGIN_PAGE);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name(ORIGIN_PAGE))
                .andReturn();
    }
}