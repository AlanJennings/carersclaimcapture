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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StoppedProvidingCareControllerTest {

    private static final String STOPPED_PROVIDING_CARE_PAGE = "/circumstances/report-changes/stopped-caring";
    private static final String NEXT_PAGE                   = "redirect:/circumstances/consent-and-declaration/declaration#";

    private StoppedProvidingCareController controller;

    @Mock
    private SessionManager sessionManager;

    @Mock
    private TransformationManager transformationManager;

    private MessageSource messageSource;

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
        controller = new StoppedProvidingCareController(sessionManager, messageSource, transformationManager, pageOrder);

        when(sessionManager.getSession(anyString())).thenReturn(session);
        when(request.getServletPath()).thenReturn(STOPPED_PROVIDING_CARE_PAGE);
    }

    @Test
    public void givenGetCallThenShouldReturnCurrentPage() {
        Assert.assertThat(controller.getForm(request, model), is(equalTo(STOPPED_PROVIDING_CARE_PAGE)));
    }

    @Test
    public void givenPostCallWithIncomeChangedResponseThenShouldReturnCorrectPage() {
        Assert.assertThat(controller.postForm(request, model), is(equalTo(NEXT_PAGE)));
    }
}
