package uk.gov.dwp.carersallowance.controller.defaultcontoller;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;


/**
 * General request handler for basic request.
 * TODO, migrate stuff to this as and when
 * @author drh
 *
 */
@Controller
public class DefaultFormController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFormController.class);

    private MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public DefaultFormController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);

        LOG.info("messageSource = {}", messageSource);
        if(messageSource instanceof ReloadableResourceBundleMessageSource) {
            messageSourceAccessor = new MessageSourceAccessor((ReloadableResourceBundleMessageSource)messageSource);
            LOG.debug("common messages = {}", messageSourceAccessor.getCommonMessages());
            LOG.debug("merged messages = {}", messageSourceAccessor.getProperties(null));
        }
    }

    @Override
    protected String getPageName() {
        // TODO Auto-generated method stub
        return null;
    }

//    @Override
//    public String[] getFields() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
    @Override
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        // TODO Auto-generated method stub

    }

    /**
     * all paths are two parts, except:
     *  preview
     *  your-income/{employment of some kind}/{various}
     *
     *  we can't use '** / *' as it intercepts all resources
     *  including the static resources
     *
     *  see {@link org.springframework.web.util.UrlPathHelper#getRequestUri(HttpServletRequest)
     *  if there are problems with the representation of the request path
     *
     * @throws NoSuchRequestHandlingMethodException
     */
    @RequestMapping(value={"/{first}", "/{first}/{second}", "/{first}/{second}/{third}"}, method = RequestMethod.GET)
    public String showForm(@PathVariable("first") Optional<String> first,
                           @PathVariable("second") Optional<String> second,
                           @PathVariable("third") Optional<String> third,
                           HttpServletRequest request,
                           Model model) throws NoSuchRequestHandlingMethodException {

        LOG.trace("Started DefaultFormController.showForm");
        try {
            String path = request.getServletPath();
            LOG.info("path = {}", path);
            switch(path) {
                case "non-existent path":
                    return super.showForm(request, model);

                default:
                    // we don't seem to be able to override the content of this regardless of what parameters are used
                    throw new NoSuchRequestHandlingMethodException(request);
            }

        } catch(NoSuchRequestHandlingMethodException e) {
            LOG.error("Unknown path", e);
            throw e;
        } finally {
            LOG.trace("Ending DefaultFormController.showForm");
        }
    }

    @RequestMapping(value="/{parent}/{child}", method = RequestMethod.POST)
    public String postForm(@PathVariable("first") Optional<String> first,
                           @PathVariable("second") Optional<String> second,
                           @PathVariable("third") Optional<String> third,
                           HttpServletRequest request,
                           Model model) throws NoSuchRequestHandlingMethodException {
        LOG.trace("Started DefaultFormController.postForm");
        try {
            String path = request.getServletPath();
            switch(path) {
                case "non-existent path":
                    return super.showForm(request, model);

                default:
                    // we don't seem to be able to override the content of this regardless of what parameters are used
                    throw new NoSuchRequestHandlingMethodException(request);
            }
        } catch(NoSuchRequestHandlingMethodException e) {
            LOG.error("Unknown path", e);
            throw e;
        } finally {
            LOG.trace("Ending DefaultFormController.postForm");
        }
    }

    private static class MessageSourceAccessor extends ReloadableResourceBundleMessageSource {
        public MessageSourceAccessor(ReloadableResourceBundleMessageSource parentSource) {
            this.setParentMessageSource(parentSource);
        }

        public Properties getCommonMessages() {
            return super.getCommonMessages();
        }

        public Properties getProperties(Locale locale) {
            PropertiesHolder propertiesHolder = super.getMergedProperties(locale);
            if(propertiesHolder == null) {
                return null;
            }
            Properties properties = new Properties();
            properties.putAll(propertiesHolder.getProperties());

            return properties;
        }

        /**
         * dummy method
         */
        @Override
        protected MessageFormat resolveCode(String code, Locale locale) {
            return null;
        }
    }
}

