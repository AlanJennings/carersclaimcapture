package uk.gov.dwp.carersallowance.session;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.gov.dwp.carersallowance.controller.XmlBuilder;
import uk.gov.dwp.carersallowance.controller.XmlClaimReader;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.sessiondata.SessionDataFactory;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;

@Service
public class SessionManager {
    private static final Logger LOG = LoggerFactory.getLogger(SessionManager.class);
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

    public void createSessionVariables(final HttpServletRequest request, final HttpServletResponse response, final String xmlFile) {
        cookieManager.addVersionCookie(response);
        cookieManager.addGaCookie(request, response);
        createSessionData(request, response, xmlFile);
    }

    private void createSessionData(final HttpServletRequest request, final HttpServletResponse response, final String xmlFile) {
        final String sessionId = createSessionId();
        Session session = createSession(sessionId);
        request.setAttribute(Session.SESSION_ID, sessionId);
        cookieManager.addSessionCookie(response, sessionId);
        if (xmlFile != null && xmlFile.length() > 0) {
            loadReplicaData(session, xmlFile);
        }
    }

    private void loadReplicaData(Session session, final String xmlFile) {
        try {
            URL claimTemplateUrl = XmlClaimReader.class.getClassLoader().getResource("xml.mapping.claim");
            List<String> xmlMappings = XmlBuilder.readLines(claimTemplateUrl);
            XPathMappingList valueMappings = new XPathMappingList();
            valueMappings.add(xmlMappings);

            URL xmlfile = XmlClaimReader.class.getClassLoader().getResource(xmlFile);
            String xml = FileUtils.readFileToString(new File(xmlfile.toURI()), Charset.defaultCharset());

            XmlClaimReader claimReader = new XmlClaimReader(xml, valueMappings, true);
            Map<String, Object> values = claimReader.getValues();
            for (String name : values.keySet()) {
                LOG.info("Replica setting data for name:" + name + " to:" + values.get(name));
                session.setAttribute(name, values.get(name));
            }
        } catch (Exception e) {
            LOG.error("Exception loading replica data {}", e.toString(), e);
        }
        saveSession(session);
    }

    public String getSessionIdFromCookie(final HttpServletRequest request) {
        return cookieManager.getSessionIdFromCookie(request);
    }
}
