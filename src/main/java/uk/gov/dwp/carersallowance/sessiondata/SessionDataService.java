package uk.gov.dwp.carersallowance.sessiondata;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
public interface SessionDataService {
    Session getSessionData(final String sessionId);
    Session saveSessionData(final Session session);
    Session removeSessionData(final String sessionId);
    Session createSessionData(final String sessionId);
}
