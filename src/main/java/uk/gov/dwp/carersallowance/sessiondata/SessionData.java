package uk.gov.dwp.carersallowance.sessiondata;

/**
 * Created by peterwhitehead on 28/12/2016.
 */
public class SessionData {
    private String sessionId;
    private String payload;
    private String minutesSinceLastActive;

    public SessionData() {}

    public SessionData(final String sessionId, final String payload, final String minutesSinceLastActive) {
        this.sessionId = sessionId;
        this.payload = payload;
        this.minutesSinceLastActive = minutesSinceLastActive;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getMinutesSinceLastActive() {
        return minutesSinceLastActive;
    }

    public void setMinutesSinceLastActive(String minutesSinceLastActive) {
        this.minutesSinceLastActive = minutesSinceLastActive;
    }
}
