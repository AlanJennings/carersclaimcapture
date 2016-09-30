package uk.gov.dwp.carersallowance.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultFormController;

/**
 * If a request comes in and matches the excluded patterns, return null (i.e. check the next handler in the chain)
 * If it matches a specific handler transfer to the handler (underlying behaviour)
 * If it does not match the specific behaviour call the defaultController, if the default handler does not handle it return null
 * otherwise return null;
 *
 * @author drh
 */
public class FilteredRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    private static final Logger LOG = LoggerFactory.getLogger(FilteredRequestMappingHandlerMapping.class);

    private AntPathMatcher pathMatcher;
    private List<String>   excludePatterns;
//    private String         defaultControllerName;

    public FilteredRequestMappingHandlerMapping() {
        pathMatcher = new AntPathMatcher();
        excludePatterns = new ArrayList<>();
    }

//    public void setDefaultController(String className) {
//        if(StringUtils.isBlank(className)) {
//            defaultControllerName = null;
//        }
//        defaultControllerName = className;
//    }

    /**
     * @deprecated try this disabled and see if it stil works.
     */
    public void setDefaultHandler(Object defaultHandler) {
        super.setDefaultHandler(defaultHandler);
    }

    public void setExclude(String pattern) {
        if(StringUtils.isBlank(pattern)) {
            return;
        }

        String trimmed = pattern.trim();
        LOG.info("setting exclude path {}", trimmed);
        excludePatterns.add(trimmed);
    }

    private HandlerMethod getDefaultControllerMethod(HttpServletRequest request) {
        LOG.trace("Started FilteredRequestMappingHandlerMapping.getFudgedHandlerMethod");
        try {
            if(request == null) {
                return null;
            }

            try {
                Object controller = getApplicationContext().getBean(DefaultFormController.class);   // TODO try this with className
                HandlerMethod handlerMethod = new HandlerMethod(controller, "handleRequest", HttpServletRequest.class, Model.class);
                return handlerMethod;
            } catch (NoSuchMethodException e) {
                LOG.error("Unsupported request", e);
                return null;
            } catch (RuntimeException e) {
                LOG.error("Unexpected RuntimeException: ", e);
                return null;

            }

        } finally {
            LOG.trace("Ending FilteredRequestMappingHandlerMapping.getFudgedHandlerMethod");
        }
    }

    /**
     * return null if this path is excluded
     * return null if we cannot match a handler for this request
     * otherwise return the handlerMethod that matches this request.
     *
     * see also {@link AbstractHandlerMapping#getHandlerInternal(HttpServletRequest)}
     *
     * We cannot override {@link HandlerMapping#getHandler(HttpServletRequest)} as it is final
     * in {@link AbstractHandlerMapping#getHandler(HttpServletRequest)}
     * @throws Exception
     */
    @Override
    protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
        LOG.trace("Started FilteredRequestMappingHandlerMapping.getHandlerInternal");
        try {
            if(request == null) {
                return null;
            }

            String path = request.getServletPath(); // from the end of the application path until the args
            for(String pattern: excludePatterns) {
                if(pathMatcher.match(pattern, path)) {
                    LOG.debug("Excluding Path({}) as it matches {}", path, pattern);
                    return null;
                }
            }
            LOG.debug("path = {}", path);

            HandlerMethod method = super.getHandlerInternal(request);
            if(method == null) {
                method = getDefaultControllerMethod(request);
            }

            LOG.info("handler bean = {}", method == null ? null : method.getBean());
            LOG.info("hander method = {}", method);
            return method;

        } finally {
            LOG.trace("Ending FilteredRequestMappingHandlerMapping.getHandlerInternal");
        }
    }
}
