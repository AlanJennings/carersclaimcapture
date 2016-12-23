package uk.gov.dwp.carersallowance.controller.defaultcontoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;

import uk.gov.dwp.carersallowance.session.SessionManager;

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
    public DefaultFormController(final SessionManager sessionManager, final MessageSource messageSource) {
        super(sessionManager, messageSource);
    }
}

