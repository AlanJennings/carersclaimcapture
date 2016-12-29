package uk.gov.dwp.carersallowance.sessiondata;

import org.apache.commons.lang3.StringUtils;
import uk.gov.dwp.carersallowance.utils.Parameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
public class Session {
    private String sessionId;
    private Map<String, Object> data;
    public static final String SESSION_ID = "sessionId";

    public Session(String sessionId, Map<String, Object> data) {
        Parameters.validateMandatoryArgs(new Object[]{sessionId, data}, new String[]{ Session.SESSION_ID, "data" });
        this.sessionId = sessionId;
        this.data = data;
    }

    public Session() {
        this(UUID.randomUUID().toString(), new HashMap<>());
    }

    public Session(final String sessionId) {
        this(sessionId, new HashMap<>());
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setAttribute(final String name, final Object value) {
        if (StringUtils.isEmpty(name)) {
            return;
        }
        data.put(name, value);
    }

    public Object getAttribute(final String name) {
        return data.get(name);
    }

    public List<String> getAttributeNames() {
        return data.keySet().stream().collect(Collectors.toList());
    }

    public void removeAttribute(String fieldName) {
        data.remove(fieldName);
    }

    public String getSessionId() {
        return sessionId;
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
