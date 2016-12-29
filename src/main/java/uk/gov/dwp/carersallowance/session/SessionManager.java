package uk.gov.dwp.carersallowance.session;

import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.sessiondata.SessionDataFactory;

@Service
public class SessionManager {
    private final SessionDataFactory sessionDataFactory;
    private final CookieManager cookieManager;

    @Inject
    public SessionManager(final CookieManager cookieManager, final SessionDataFactory sessionDataFactory) {
        this.cookieManager = cookieManager;
        this.sessionDataFactory = sessionDataFactory;
    }

    public String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public Session getSession(final String sessionId) {
        return sessionDataFactory.getSessionDataService().getSessionData(sessionId);
    }

    public Session createSession(final String sessionId) {
        return sessionDataFactory.getSessionDataService().createSessionData(sessionId);
    }

    public void removeSession(final String sessionId) {
        sessionDataFactory.getSessionDataService().removeSessionData(sessionId);
    }

    public void saveSession(final Session session) {
        sessionDataFactory.getSessionDataService().saveSessionData(session);
    }

    public void createSessionVariables(final HttpServletRequest request, final HttpServletResponse response) {
        cookieManager.addVersionCookie(response);
        cookieManager.addGaCookie(request, response);
        createSessionData(request, response);
    }

    private void createSessionData(final HttpServletRequest request, final HttpServletResponse response) {
        final String sessionId = createSessionId();
        createSession(sessionId);
        request.setAttribute(Session.SESSION_ID, sessionId);
        cookieManager.addSessionCookie(response, sessionId);
    }

    public String getSessionIdFromCookie(final HttpServletRequest request) {
        return cookieManager.getSessionIdFromCookie(request);
    }
}
