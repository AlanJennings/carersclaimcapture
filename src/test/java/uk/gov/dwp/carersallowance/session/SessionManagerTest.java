package uk.gov.dwp.carersallowance.session;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import uk.gov.dwp.carersallowance.encryption.ClaimEncryptionService;
import uk.gov.dwp.carersallowance.encryption.ClaimEncryptionServiceImpl;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.sessiondata.SessionDataFactory;
import uk.gov.dwp.carersallowance.sessiondata.SessionDataMapServiceImpl;

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
    private static final String XML_MAPPING_FILE = "xml.mapping.claim";

    private SessionManager sessionManager;

    private SessionDataMapServiceImpl sessionDataService;

    @Mock
    private CookieManager cookieManager;

    @Mock
    private SessionDataFactory sessionDataFactory;

    @Mock
    private MessageSource messageSource;

    private ClaimEncryptionService claimEncryptionService;

    private Session session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private ArgumentCaptor<Object> objectCaptor;


    @Before
    public void setUp() throws Exception {
        session = new Session("1234");
        sessionDataService = new SessionDataMapServiceImpl();
        claimEncryptionService = new ClaimEncryptionServiceImpl(false, messageSource);
        when(sessionDataFactory.getSessionDataService()).thenReturn(sessionDataService);
        sessionManager = new SessionManager(cookieManager, sessionDataFactory, claimEncryptionService);
        sessionManager.setXmlMappingFile(XML_MAPPING_FILE);
        objectCaptor = ArgumentCaptor.forClass(Object.class);
    }

    @Test
    public void testGetSession() throws Exception {
        session = sessionDataService.createSessionData("1234");
        final Session session1 = sessionManager.getSession("1234");
        org.assertj.core.api.Assertions.assertThat(session1).isEqualToComparingFieldByField(session);
    }

    @Test
    public void testCreateSession() throws Exception {
        org.assertj.core.api.Assertions.assertThat(sessionManager.createSession("1234")).isEqualToComparingFieldByField(session);
    }

    @Test(expected = NoSessionException.class)
    public void testRemoveSession() throws Exception {
        sessionDataService.createSessionData("1234");
        sessionManager.removeSession("1234");
        sessionDataService.getSessionData("1234");
    }

    @Test
    public void testCreateSessionVariables() throws Exception {
        sessionManager.createSessionVariables(request, response, "claimreader-claimant.xml");
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

    @Test
    public void testLoadDefaultData() throws Exception {
        sessionManager.createSessionVariables(request, response, "claimreader-claimdate.xml");
        verify(request, times(1)).setAttribute(anyString(), objectCaptor.capture());
        final String sessionId = objectCaptor.getValue().toString();
        session = sessionManager.getSession(sessionId);
        assertThat(session.getAttribute("over35HoursAWeek"), is("yes"));
        assertThat(session.getAttribute("over16YearsOld"), is("yes"));
        assertThat(session.getAttribute("originCountry"), is("GB"));
    }

    @Test
    public void testLoadClaimDate() throws Exception {
        sessionManager.createSessionVariables(request, response, "claimreader-claimdate.xml");
        verify(request, times(1)).setAttribute(anyString(), objectCaptor.capture());
        final String sessionId = objectCaptor.getValue().toString();
        session = sessionManager.getSession(sessionId);
        assertThat(session.getAttribute("dateOfClaim_day"), is("20"));
        assertThat(session.getAttribute("dateOfClaim_month"), is("04"));
        assertThat(session.getAttribute("dateOfClaim_year"), is("2016"));
    }

    @Test
    public void testLoadReplicaData() throws Exception {
        sessionManager.createSessionVariables(request, response, "claimreader-claimant.xml");
        verify(request, times(1)).setAttribute(anyString(), objectCaptor.capture());
        final String sessionId = objectCaptor.getValue().toString();
        session = sessionManager.getSession(sessionId);
        assertThat(session.getAttribute("appVersion"), is("0.27"));
        assertThat(session.getAttribute("origin"), is("GB"));
        assertThat(session.getAttribute("language"), is("English"));
        assertThat(session.getAttribute("dateOfClaim_day"), is("20"));
        assertThat(session.getAttribute("dateOfClaim_month"), is("04"));
        assertThat(session.getAttribute("dateOfClaim_year"), is("2016"));
        assertThat(session.getAttribute("carerSurname"), is("Bloggs"));
        assertThat(session.getAttribute("carerFirstName"), is("Joe"));
        assertThat(session.getAttribute("carerDateOfBirth_day"), is("20"));
        assertThat(session.getAttribute("carerDateOfBirth_month"), is("10"));
        assertThat(session.getAttribute("carerDateOfBirth_year"), is("1990"));
    }
}