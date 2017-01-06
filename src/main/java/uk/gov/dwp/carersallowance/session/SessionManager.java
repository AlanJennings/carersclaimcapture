package uk.gov.dwp.carersallowance.session;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uk.gov.dwp.carersallowance.encryption.ClaimEncryptionService;
import uk.gov.dwp.carersallowance.xml.XmlBuilder;
import uk.gov.dwp.carersallowance.xml.XmlClaimReader;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.sessiondata.SessionDataFactory;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;


@Service
public class SessionManager {
    private static final Logger LOG = LoggerFactory.getLogger(SessionManager.class);

    private final SessionDataFactory sessionDataFactory;
    private final CookieManager cookieManager;
    private final ClaimEncryptionService claimEncryptionService;

    @Inject
    public SessionManager(final CookieManager cookieManager,
                          final SessionDataFactory sessionDataFactory,
                          final ClaimEncryptionService claimEncryptionService){
        this.cookieManager          = cookieManager;
        this.sessionDataFactory     = sessionDataFactory;
        this.claimEncryptionService = claimEncryptionService;
    }

    public String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public Session getSession(final String sessionId) {
        return claimEncryptionService.decryptClaim(sessionDataFactory.getSessionDataService().getSessionData(sessionId));
    }

    public Session createSession(final String sessionId) {
        return sessionDataFactory.getSessionDataService().createSessionData(sessionId);
    }

    public void removeSession(final String sessionId) {
        sessionDataFactory.getSessionDataService().removeSessionData(sessionId);
    }

    public void saveSession(final Session session) {
        sessionDataFactory.getSessionDataService().saveSessionData(claimEncryptionService.encryptClaim(session));
    }

    public void createSessionVariables(final HttpServletRequest request, final HttpServletResponse response, final String xmlFile, final URL mappingFile) {
        cookieManager.addVersionCookie(response);
        cookieManager.addGaCookie(request, response);
        createSessionData(request, response, xmlFile, mappingFile);
    }

    private void createSessionData(final HttpServletRequest request, final HttpServletResponse response, final String xmlFile, final URL mappingFile) {
        final String sessionId = createSessionId();
        Session session = createSession(sessionId);
        request.setAttribute(Session.SESSION_ID, sessionId);
        cookieManager.addSessionCookie(response, sessionId);
        if (xmlFile != null && xmlFile.length() > 0) {
            loadReplicaData(session, xmlFile, mappingFile);
        }
    }

    private void loadReplicaData(Session session, final String xmlFile, final URL mappingFile) {
        try {
            LOG.info("Using XMLFile " + xmlFile);
            LOG.info("Using mapping file " + mappingFile);
            List<String> xmlMappings = XmlBuilder.readLines(mappingFile);
            XPathMappingList valueMappings = new XPathMappingList();
            valueMappings.add(xmlMappings);
            String xml = IOUtils.toString(XmlClaimReader.class.getClassLoader().getResourceAsStream(xmlFile),Charset.defaultCharset());
            XmlClaimReader claimReader = new XmlClaimReader(xml, valueMappings, true);

            Map<String, Object> values = claimReader.getValues();
            for (String name : values.keySet()) {
                LOG.info("Replica setting data for name:" + name + " to:" + values.get(name));
                session.setAttribute(name, values.get(name));
            }
            session.removeAttribute("transactionId");

            LOG.info("5 " );
            // TODO load the replica data from xml ... but its not there ! Hows it done in scala ??
            session.setAttribute("over35HoursAWeek", "yes");
            session.setAttribute("over16YearsOld", "yes");
            session.setAttribute("originCountry", "GB");
        } catch (Exception e) {
            LOG.error("Exception loading replica data {}", e.toString(), e);
        }
        saveSession(session);
    }

    public String getSessionIdFromCookie(final HttpServletRequest request) {
        return cookieManager.getSessionIdFromCookie(request);
    }
}
