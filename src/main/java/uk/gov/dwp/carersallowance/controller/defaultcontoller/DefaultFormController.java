package uk.gov.dwp.carersallowance.controller.defaultcontoller;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 *         TODO detach from the AbstractFormController or split it into two and farm some of
 *         the stuff into a base for the specific controllers and not the general one
 */
@Controller
public class DefaultFormController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFormController.class);

    @Autowired
    public DefaultFormController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    /**
     * Data driven request holder
     * @throws NoSuchRequestHandlingMethodException
     */
    public String handleRequest(HttpServletRequest request, HttpServletResponse response, Model model) throws NoSuchRequestHandlingMethodException {
        LOG.info("Started DefaultFormController.handleRequest");
        Parameters.validateMandatoryArgs(request, "request");
        try {
            checkVersionCookie(request);
            addVersionCookie(response);

            String path = request.getServletPath();
            String method = request.getMethod();

            String page = null;
            LOG.info("method = {}, path = {}", method, path);
            if (HTTP_GET.equalsIgnoreCase(method)) {
                return super.showForm(request, model);
            } else if (HTTP_POST.equalsIgnoreCase(method)) {
                return super.postForm(request, request.getSession(), model);
            } else {
                LOG.error("Request method {} is not supported in request: {}", method, path);
                throw new NoSuchRequestHandlingMethodException(request);
            }
        } catch (NoSuchRequestHandlingMethodException e) {
            LOG.error("NoSuchRequestHandlingMethodException", e);
            throw e;
        } catch (RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.info("Ending DefaultFormController.handleRequest");
        }
    }
}

