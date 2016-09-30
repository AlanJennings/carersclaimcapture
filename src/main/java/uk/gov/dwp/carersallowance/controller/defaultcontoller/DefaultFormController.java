package uk.gov.dwp.carersallowance.controller.defaultcontoller;

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

    @Autowired
    public DefaultFormController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    public String handleRequest(HttpServletRequest request, Model model) throws NoSuchRequestHandlingMethodException {
        LOG.trace("Started DefaultFormController.handleRequest");
        Parameters.validateMandatoryArgs(request, "request");
        try {
            String path = request.getServletPath();
            String method = request.getMethod();

            LOG.info("method = {}, path = {}", method, path);
            if("GET".equalsIgnoreCase(method)) {
                return super.showForm(request, model);
            } else if("POST".equalsIgnoreCase(method)) {
                return super.postForm(request, request.getSession(), model);
            } else {
                LOG.error("Request method {} is not supported in request: {}", method, path);
                throw new NoSuchRequestHandlingMethodException(request);
            }

        } finally {
            LOG.trace("Ending DefaultFormController.defaultShowForm");
        }
    }
}

