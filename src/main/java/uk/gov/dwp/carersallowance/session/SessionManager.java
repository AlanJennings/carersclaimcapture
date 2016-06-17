package uk.gov.dwp.carersallowance.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import uk.gov.dwp.carersallowance.utils.Parameters;

@Service
public class SessionManager {
    private Map<String, Session> sessions;

    public SessionManager() {
        sessions = new HashMap<String, Session>();
    }

    public Session getSession(String sessionId) { return getSession(sessionId, false); }
    /**
     * We don't need to synchronize this as new sessions are created using statistically
     * unique IDs that don't overlap
     */
    public Session getSession(String sessionId, boolean create) {
        Session session = sessions.get(sessionId);
        if(session == null) {
            if(create) {
                session = new Session();
                sessions.put(session.getSessionId(),  session);
            } else {
                throw new NoSessionException("No Session for Session ID: " + sessionId);
            }
        }

        return session;
    }

    public Session removeSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Session is nested so it's constructors can stay private, but still be used by SessionManager
     */
    public static class Session {
        private String              sessionId;
        private Map<String, Object> data;

        private Session(String sessionId, Map<String, Object> data) {
            Parameters.validateMandatoryArgs(new Object[]{sessionId, data}, new String[]{"sessionId", "data"});

            this.sessionId = sessionId;
            this.data = data;
        }

        private Session() {
            this(UUID.randomUUID().toString(), new HashMap<>());
        }

        public String getSessionId() {
            return  sessionId;
        }

        /**
         * At some point this will be expanded to that all access to the internal data is wrapped
         * See also {@link FieldValue}
         * @return
         */
        public Map<String, Object> getData() {
            return data;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();

            buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
            buffer.append("=[");
            buffer.append("sessionId = ").append(sessionId);
            buffer.append(", data = ").append(data);
            buffer.append("]");

            return buffer.toString();
        }
    }
}
