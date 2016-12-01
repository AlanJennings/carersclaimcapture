package uk.gov.dwp.carersallowance.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by peterwhitehead on 12/10/2016.
 */
//@Controller
@RequestMapping("/error")
public class SimpleErrorController implements ErrorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleErrorController.class);

    private final ErrorAttributes errorAttributes;
    private final static String MAIN_ERROR_PAGE = "/error/error";
    private final MessageSource messageSource;

    @Inject
    public SimpleErrorController(final ErrorAttributes errorAttributes, final MessageSource messageSource) {
        this.errorAttributes = errorAttributes;
        this.messageSource = messageSource;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping
    public String error(final HttpServletRequest request) {
        LOGGER.info("/error called");
        final Map<String, Object> body = getErrorAttributes(request, false);
        LOGGER.error("Unable to process request. error: {}", body.get("message"));
        request.setAttribute("error", body.get("status") + ": " + body.get("error"));
        return MAIN_ERROR_PAGE;
    }

    private Map<String, Object> getErrorAttributes(final HttpServletRequest request, final Boolean includeStackTrace) {
        final RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }
}

