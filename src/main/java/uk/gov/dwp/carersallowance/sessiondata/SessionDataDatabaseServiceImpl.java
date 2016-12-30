package uk.gov.dwp.carersallowance.sessiondata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.carersallowance.session.NoSessionException;
import utils.XorEncryption;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@Service
public class SessionDataDatabaseServiceImpl implements SessionDataService {
    private static final Logger LOG = LoggerFactory.getLogger(SessionDataDatabaseServiceImpl.class);

    private final String sdUrl;
    private final String sdKey;
    private final Integer claimExpiryTime;
    private final RestTemplate restTemplate;

    public SessionDataDatabaseServiceImpl(final @Value("${sd.url}") String sdUrl,
                                          final @Value("${session.data.uuid.secret.key}") String sdKey,
                                          final @Value("${session.data.claim.expiry}") Integer claimExpiryTime,
                                          final RestTemplate restTemplate) {
        this.sdUrl = sdUrl;
        this.sdKey = sdKey;
        this.claimExpiryTime = claimExpiryTime;
        this.restTemplate = restTemplate;
    }

    private XStream createXStream() {
        XStream xStream = new XStream(new StaxDriver());
        xStream.alias("map", java.util.Map.class);
        xStream.alias("map", java.util.HashMap.class);
        return xStream;
    }

    @Override
    public Session getSessionData(final String sessionId) {
        final Session session = getSessionFromSessionData(sessionId);
        if (session == null) {
            throw new NoSessionException("No Session for Session ID: " + sessionId);
        }
        return session;
    }

    @Override
    public Session createSessionData(final String sessionId) {
        final Session session = new Session(sessionId);
        return saveSessionData(session);
    }

    @Override
    public Session saveSessionData(final Session session) {
        saveSessionToSessionData(session);
        return session;
    }

    @Override
    public void removeSessionData(final String sessionId) {
        processRequest("", sessionId, "/session/delete/" + XorEncryption.encryptUuid(sessionId, sdKey), HttpMethod.DELETE);
    }

    private Session getSessionFromSessionData(final String sessionId) {
        final SessionData sessionData = processRequest(sessionId, "/session/load/" + XorEncryption.encryptUuid(sessionId, sdKey), HttpMethod.POST);

        if (isEmpty(sessionData)) {
            throw new NoSessionException("No Session for Session ID: " + sessionId);
        }
        if ((sessionData.getMinutesSinceLastActive() != null && Integer.valueOf(sessionData.getMinutesSinceLastActive()) > claimExpiryTime)) {
            throw new SessionTimeoutException("Session timed out, last active:" + sessionData.getMinutesSinceLastActive() + " timeout period:" + claimExpiryTime);
        }
        final String xml = new String(DatatypeConverter.parseBase64Binary(sessionData.getPayload()));
        XStream xStream = createXStream();
        Map<String, Object> data = (Map<String, Object>)xStream.fromXML(xml);
        return new Session(sessionId, data);
    }

    private Boolean isEmpty(SessionData sessionData) {
        return sessionData == null || (sessionData.getMinutesSinceLastActive() == null && sessionData.getPayload() == null && sessionData.getSessionId() == null);
    }

    private void saveSessionToSessionData(final Session session) {
        XStream xStream = createXStream();
        final String xmlMessage = xStream.toXML(session.getData());
        final String xml = DatatypeConverter.printBase64Binary(xmlMessage.getBytes());
        processRequest(xml, session.getSessionId(), "/session/save/" + XorEncryption.encryptUuid(session.getSessionId(), sdKey), HttpMethod.POST);
    }

    private String processRequest(final String message, final String sessionId, final String url, final HttpMethod method) {
        try {
            final ResponseEntity<String> response = restTemplate.exchange(sdUrl + url, method, createRequest(message), String.class);
            return processResponse(response, sessionId);
        } catch (RestClientException rce) {
            LOG.error("session data service is unavailable! {}. sessionId:{}.", rce.getMessage(), sessionId, rce);
            throw new SessionDataServiceException("Session data service is unavailable! " + rce.getMessage() + ".", rce);
        } catch (SessionDataServiceException mde) {
            throw mde;
        } catch (Exception e) {
            LOG.error("Session data failed to save claim. sessionId:{}.", sessionId, e);
            throw new SessionDataServiceException("Session data service is unavailable! " + e.getMessage() + ".", e);
        }
    }

    private SessionData processRequest(final String sessionId, final String url, final HttpMethod method) {
        try {
            final ResponseEntity<SessionData> response = restTemplate.exchange(sdUrl + url, method, createRequest(""), SessionData.class);
            return processResponse(response, sessionId, true);
        } catch (RestClientException rce) {
            LOG.error("session data service is unavailable! {}. sessionId:{}.", rce.getMessage(), sessionId, rce);
            throw new SessionDataServiceException("Session data service is unavailable! " + rce.getMessage() + ".", rce);
        } catch (SessionDataServiceException mde) {
            throw mde;
        } catch (Exception e) {
            LOG.error("Session data process claim. sessionId:{}.", sessionId, e);
            throw new SessionDataServiceException("Session data service is unavailable! " + e.getMessage() + ".", e);
        }
    }

    private HttpEntity<String> createRequest(final String message) {
        final HttpHeaders headers = new HttpHeaders();
        final MediaType mediaType = new MediaType("application", "xml", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        return new HttpEntity<>(message, headers);
    }

    private String processResponse(final ResponseEntity<String> response, final String sessionId) {
        final Integer responseStatus = response.getStatusCode().value();
        checkStatus(responseStatus, sessionId, response.toString());
        return response.getBody();
    }

    private SessionData processResponse(final ResponseEntity<SessionData> response, final String sessionId, final Boolean sessionData) {
        final Integer responseStatus = response.getStatusCode().value();
        checkStatus(responseStatus, sessionId, response.toString());
        return response.getBody();
    }

    private void checkStatus(final Integer responseStatus, final String sessionId, final String response) {
        LOG.info("Session data service got status:{} for sessionId:{}.", responseStatus, sessionId);
        switch (responseStatus) {
            case org.apache.http.HttpStatus.SC_OK:
                break;
            case org.apache.http.HttpStatus.SC_REQUEST_TIMEOUT:
                LOG.error("Session data service response - REQUEST_TIMEOUT:{} :{} sessionId:{}.", responseStatus, response, sessionId);
                throw new SessionDataServiceException("Failed distribution because of a timeout.");
            case org.apache.http.HttpStatus.SC_SERVICE_UNAVAILABLE:
                LOG.error("Session data service response - SERVICE_UNAVAILABLE:{} :{}. sessionId:{}.", responseStatus, response, sessionId);
                throw new SessionDataServiceException("Session data service is Unavailable.");
            case org.apache.http.HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE:
                LOG.error("Session data service response - UNSUPPORTED_MEDIA_TYPE:{} :{}. sessionId:{}. This should not happen.", responseStatus, response, sessionId);
                throw new SessionDataServiceException("Session data service returned unsupported media type: " + responseStatus + " : " + response + ". sessionId:" + sessionId + ".");
            default:
                LOG.error("Session data service response:{} :{}. sessionId:{}.", responseStatus, response, sessionId);
                throw new SessionDataServiceException("Session data service returned error: " + responseStatus + " : " + response + ". sessionId:" + sessionId + ".");
        }
    }
}
