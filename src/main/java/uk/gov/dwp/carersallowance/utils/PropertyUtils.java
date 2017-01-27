package uk.gov.dwp.carersallowance.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class PropertyUtils {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyUtils.class);

    public static String trimQuotes(String string) {
        if (string == null) {
            return null;
        }

        string = string.trim();
        if (string.length() >= 2 && string.charAt(0) == '"' && string.charAt(string.length() - 1) == '"') {
            LOG.debug("Trimming external quotes");
            return string.substring(1, string.length() - 1);
        }

        return string;
    }

    public static String getCurrentPage(final HttpServletRequest request) {
        return request.getServletPath();
    }
}