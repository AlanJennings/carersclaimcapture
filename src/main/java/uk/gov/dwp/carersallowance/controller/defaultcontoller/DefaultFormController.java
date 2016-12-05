package uk.gov.dwp.carersallowance.controller.defaultcontoller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.utils.Parameters;

/**
 * General request handler for basic request.
 * TODO, migrate stuff to this as and when
 * @author drh
 *
 * TODO detach from the AbstractFormController or split it into two and farm some of
 * the stuff into a base for the specific controllers and not the general one
 */
@Controller
public class DefaultFormController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFormController.class);

    private static final String HTTP_POST = "POST";
    private static final String HTTP_GET = "GET";

    @Autowired
    public DefaultFormController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    public boolean supportsRequest(HttpServletRequest request) {
        LOG.trace("Started DefaultFormController.supportsRequest");
        Parameters.validateMandatoryArgs(request, "request");
        try {
            String method = request.getMethod();
            if(HTTP_GET.equalsIgnoreCase(method) == false && HTTP_POST.equalsIgnoreCase(method) == false) {
                LOG.info("Unsupported request method: {}", method);
                return false;
            }

            String path = request.getServletPath();
            LOG.info("method = {}, path = {}", method, path);
            String fieldsKey = path + ".fields";
            String fields = getMessageSource().getMessage(fieldsKey, null, null, Locale.getDefault()); // If there are no fields, but is an entry do we get null or ""?
            if(fields == null) {
                LOG.info("Unsupported request: {}", path);
                return false;
            }
            return true;

        } finally {
            LOG.trace("Ending DefaultFormController.supportsRequest");
        }
    }

    /**
     * Data driven request holder
     * @throws NoSuchRequestHandlingMethodException
     */
    public String handleRequest(HttpServletRequest request, Model model) throws NoSuchRequestHandlingMethodException {
        LOG.trace("Started DefaultFormController.handleRequest");
        Parameters.validateMandatoryArgs(request, "request");
        try {
            String path = request.getServletPath();
            String method = request.getMethod();

            String page = null;
            LOG.info("method = {}, path = {}", method, path);
            if(HTTP_GET.equalsIgnoreCase(method)) {
                return super.showForm(request, model);
            } else if(HTTP_POST.equalsIgnoreCase(method)) {
                return super.postForm(request, request.getSession(), model);
            } else {
                LOG.error("Request method {} is not supported in request: {}", method, path);
                throw new NoSuchRequestHandlingMethodException(request);
            }

        } catch(NoSuchRequestHandlingMethodException e) {
            LOG.error("NoSuchRequestHandlingMethodException", e);
            throw e;
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending DefaultFormController.defaultShowForm");
        }
    }
}

