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
import uk.gov.dwp.carersallowance.utils.C3Constants;
import uk.gov.dwp.carersallowance.utils.LoadFile;
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
    private final String originTag;

    @Inject
    public SessionManager(final CookieManager cookieManager,
                          final SessionDataFactory sessionDataFactory,
                          final ClaimEncryptionService claimEncryptionService,
                          @Value("${origin.tag}") final String originTag){
        this.cookieManager = cookieManager;
        this.sessionDataFactory = sessionDataFactory;
        this.claimEncryptionService = claimEncryptionService;
        this.originTag = originTag;
    }

    public String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public Session getSession(final String sessionId) {
        return claimEncryptionService.decryptClaim(sessionDataFactory.getSessionDataService().getSessionData(sessionId));
    }

    private Session createSession(final String sessionId, final String claimType) {
        return sessionDataFactory.getSessionDataService().createSessionData(sessionId, claimType);
    }

    public void removeSession(final String sessionId) {
        sessionDataFactory.getSessionDataService().removeSessionData(sessionId);
    }

    public void saveSession(final Session session) {
        sessionDataFactory.getSessionDataService().saveSessionData(claimEncryptionService.encryptClaim(session));
    }

    public void createSessionVariables(final HttpServletRequest request, final HttpServletResponse response, final String xmlFile, final URL mappingFile, final String xmlSchemaVersion, final String claimType) {
        cookieManager.addVersionCookie(response);
        cookieManager.addGaCookie(request, response);
        createSessionData(request, response, xmlFile, mappingFile, xmlSchemaVersion, claimType);
    }

    private void createSessionData(final HttpServletRequest request, final HttpServletResponse response, final String xmlFile, final URL mappingFile, final String xmlSchemaVersion, final String claimType) {
        final String sessionId = createSessionId();
        Session session = createSession(sessionId, claimType);
        request.setAttribute(Session.SESSION_ID, sessionId);
        cookieManager.addSessionCookie(response, sessionId);
        if (xmlFile != null && xmlFile.length() > 0) {
            loadReplicaData(session, xmlFile, mappingFile);
        }
        session.setAttribute("xmlVersion", xmlSchemaVersion);
        session.setAttribute("originTag", originTag);
        session.setAttribute("appVersion", cookieManager.getApplicationVersionNumber());
        sessionDataFactory.getSessionDataService().saveSessionData(session);
    }

    private void loadReplicaData(Session session, final String xmlFile, final URL mappingFile) {
        try {
            LOG.info("Using XMLFile " + xmlFile);
            LOG.info("Using mapping file " + mappingFile);
            List<String> xmlMappings = LoadFile.readLines(mappingFile);
            XPathMappingList valueMappings = new XPathMappingList();
            valueMappings.add(xmlMappings);
            String xml = IOUtils.toString(XmlClaimReader.class.getClassLoader().getResourceAsStream(xmlFile),Charset.defaultCharset());
            XmlClaimReader claimReader = new XmlClaimReader(xml, valueMappings, true);

            Map<String, Object> values = claimReader.getValues();
            for (String name : values.keySet()) {
                LOG.info("Replica setting data for name:" + name + " to:" + values.get(name));
                session.setAttribute(name, values.get(name));
            }
            session.removeAttribute(C3Constants.TRANSACTION_ID);
            session.setAttribute("over35HoursAWeek", C3Constants.YES);
            session.setAttribute("over16YearsOld", C3Constants.YES);
            session.setAttribute("originCountry", "GB");
        } catch (Exception e) {
            LOG.error("Exception loading replica data {}", e.toString(), e);
        }
    }

    public String getSessionIdFromCookie(final HttpServletRequest request) {
        return cookieManager.getSessionIdFromCookie(request);
    }
}
