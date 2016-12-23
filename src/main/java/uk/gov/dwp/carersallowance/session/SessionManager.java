package uk.gov.dwp.carersallowance.session;

import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.sessiondata.SessionDataService;

@Service
public class SessionManager {
    private final SessionDataService sessionDataService;
    private final CookieManager cookieManager;

    @Inject
    public SessionManager(final CookieManager cookieManager, final SessionDataService sessionDataService) {
        this.cookieManager = cookieManager;
        this.sessionDataService = sessionDataService;
    }
    public Session getSession(final String sessionId) {
        return sessionDataService.getSessionData(sessionId);
    }

    public String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public Session createSession(final String sessionId) {
        return sessionDataService.createSessionData(sessionId);
    }

    public Session removeSession(final String sessionId) {
        return sessionDataService.removeSessionData(sessionId);
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
