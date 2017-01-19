package uk.gov.dwp.carersallowance.controller.started;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import uk.gov.dwp.carersallowance.utils.C3Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by peterwhitehead on 19/01/2017.
 */
@Component
public class ChangeLanguageProcess {

    public void processChangeLanguage(final HttpServletRequest request, final HttpServletResponse response) {
        setLanguage(request, response, getLanguage(request));
    }

    private void setLanguage(final HttpServletRequest request, final HttpServletResponse response, final String newLanguage) {
        Locale locale = LocaleUtils.toLocale(newLanguage);
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLanguage));
            Locale.setDefault(locale);
        }
    }

    private String getLanguage(final HttpServletRequest request) {
        if (request.getQueryString() != null && request.getQueryString().contains("lang=" + C3Constants.WELSH_LANG)) {
            return C3Constants.WELSH_LANG;
        }
        return C3Constants.ENGLISH_LANG;
    }
}
