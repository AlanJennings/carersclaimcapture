package uk.gov.dwp.carersallowance.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.dwp.carersallowance.sessiondata.Session;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@Component
public class CookieManager {
    private final Integer c3VersionSecondsToLive;
    private final String gACookieName;
    private final String sessionCookieName;
    private final Integer gACookieSecondsToLive;
    private final String gACidPrefix;
    private final Integer c3SessionSecondsToLive;
    private final String applicationVersionCookieName;
    private final String applicationVersion;

    @Inject
    public CookieManager(@Value("${application.seconds.to.live}") final Integer c3VersionSecondsToLive,
                         @Value("${ga.cookie.name}") final String gACookieName,
                         @Value("${session.cookie.name}") final String sessionCookieName,
                         @Value("${ga.cookie.seconds.to.live}") final Integer gACookieSecondsToLive,
                         @Value("${ga.cookie.cid.prefix}") final String gACidPrefix,
                         @Value("${session.seconds.to.live}") final Integer c3SessionSecondsToLive,
                         @Value("${application.version}") final String applicationVersion,
                         @Value("${application.version.cookie.name}") final String applicationVersionCookieName) {
        this.c3VersionSecondsToLive = c3VersionSecondsToLive;
        this.gACookieName = gACookieName;
        this.sessionCookieName = sessionCookieName;
        this.gACookieSecondsToLive = gACookieSecondsToLive;
        this.gACidPrefix = gACidPrefix;
        this.c3SessionSecondsToLive = c3SessionSecondsToLive;
        this.applicationVersion = applicationVersion;
        this.applicationVersionCookieName = applicationVersionCookieName;
    }

    public void addVersionCookie(final HttpServletResponse response) {
        addCookie(response, applicationVersionCookieName, appVersionNumber(), c3VersionSecondsToLive);
    }

    public String getVersionCookieValue(final HttpServletRequest request) {
        Cookie cookie = getCookie(request, applicationVersionCookieName);
        if (cookie == null) {
            return "";
        } else {
            return cookie.getValue();
        }
    }

    public String getSessionIdFromCookie(final HttpServletRequest request) {
        String sessionId = (String)request.getAttribute(Session.SESSION_ID);
        if (sessionId != null) {
            return sessionId;
        }
        Cookie cookie = getCookie(request, sessionCookieName);
        if (cookie == null) {
            return "";
        }
        return cookie.getValue();
    }

    public void addGaCookie(final HttpServletRequest request, final HttpServletResponse response) {
        addCookie(response, gACookieName, newGoogleAnalyticsClientId(request, gACookieName), gACookieSecondsToLive);
    }

    public void addSessionCookie(final HttpServletResponse response, final String sessionId) {
        addCookie(response, sessionCookieName, sessionId, c3SessionSecondsToLive);
    }

    private void addCookie(final HttpServletResponse response, final String name, final String value, final Integer maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String newGoogleAnalyticsClientId(final HttpServletRequest request, final String cookieName) {
        Cookie cookie = getCookie(request, cookieName);
        if (cookie == null) {
            return gACidPrefix + "." + Math.round(2147483647 * Math.random()) + "." + Math.round(2147483647 * Math.random());
        } else {
            return cookie.getValue();
        }
    }

    private Cookie getCookie(final HttpServletRequest request, final String cookieName) {
        Cookie[] cookies = request.getCookies();
        for (int n = 0; n < (cookies == null ? 0 : cookies.length); n++) {
            if (cookies[n].getName().equals(cookieName)) {
                return cookies[n];
            }
        }
        return null;
    }

    private String appVersionNumber() {
        return (applicationVersion.replaceAll("-.*", ""));
    }
}
