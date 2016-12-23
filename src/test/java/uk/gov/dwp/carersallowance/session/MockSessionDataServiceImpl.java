package uk.gov.dwp.carersallowance.session;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.sessiondata.SessionDataService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@Profile("test")
@Service
public class MockSessionDataServiceImpl implements SessionDataService {
    private Map<String, Session> sessions;

    public MockSessionDataServiceImpl() {
        sessions = new ConcurrentHashMap<>();
    }

    @Override
    public Session getSessionData(final String sessionId) {
        Session session = sessions.get(sessionId);
        if (session == null) {
            throw new NoSessionException("No Session for Session ID: " + sessionId);
        }
        return session;
    }

    @Override
    public Session createSessionData(final String sessionId) {
        final Session session = new Session(sessionId);
        sessions.put(sessionId, session);
        return session;
    }

    @Override
    public Session saveSessionData(final Session session) {
        return session;
    }

    @Override
    public Session removeSessionData(final String sessionId) {
        return sessions.remove(sessionId);
    }
}
