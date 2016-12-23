package uk.gov.dwp.carersallowance.session;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.dwp.carersallowance.sessiondata.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by peterwhitehead on 23/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionManagerTest {
    private SessionManager sessionManager;

    private MockSessionDataServiceImpl sessionDataService;

    @Mock
    private CookieManager cookieManager;

    private Session session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private ArgumentCaptor<Object> objectCaptor;

    @Before
    public void setUp() throws Exception {
        session = new Session("1234");
        sessionDataService = new MockSessionDataServiceImpl();
        sessionManager = new SessionManager(cookieManager, sessionDataService);
        objectCaptor = ArgumentCaptor.forClass(Object.class);
    }

    @Test
    public void testGetSession() throws Exception {
        sessionDataService.createSessionData("1234");
        final Session session1 = sessionManager.getSession("1234");
        org.assertj.core.api.Assertions.assertThat(session1).isEqualToComparingFieldByField(session);
    }

    @Test
    public void testCreateSession() throws Exception {
        org.assertj.core.api.Assertions.assertThat(sessionManager.createSession("1234")).isEqualToComparingFieldByField(session);
    }

    @Test
    public void testRemoveSession() throws Exception {
        sessionDataService.createSessionData("1234");
        org.assertj.core.api.Assertions.assertThat(sessionManager.removeSession("1234")).isEqualToComparingFieldByField(session);
    }

    @Test
    public void testCreateSessionVariables() throws Exception {
        sessionManager.createSessionVariables(request, response);
        verify(cookieManager, times(1)).addGaCookie(request, response);
        verify(cookieManager, times(1)).addSessionCookie(Matchers.any(HttpServletResponse.class), anyString());
        verify(cookieManager, times(1)).addVersionCookie(response);
        verify(request, times(1)).setAttribute(anyString(), objectCaptor.capture());
        final String sessionId = objectCaptor.getValue().toString();
        org.assertj.core.api.Assertions.assertThat(sessionDataService.getSessionData(sessionId)).isEqualToComparingFieldByField(sessionManager.getSession(sessionId));
    }

    @Test
    public void testGetSessionIdFromCookie() throws Exception {
        final String sessionId = "1234";
        when(cookieManager.getSessionIdFromCookie(request)).thenReturn(sessionId);
        assertThat(sessionManager.getSessionIdFromCookie(request), is(sessionId));
    }

    @Test
    public void testSaveSession() throws Exception {
        final String sessionId = "1234";
        final Session session1 = sessionDataService.createSessionData("1234");
        session1.setAttribute("test", "test2");
        sessionManager.saveSession(session1);
        org.assertj.core.api.Assertions.assertThat(session1).isEqualToComparingFieldByField(sessionManager.getSession(sessionId));
    }
}