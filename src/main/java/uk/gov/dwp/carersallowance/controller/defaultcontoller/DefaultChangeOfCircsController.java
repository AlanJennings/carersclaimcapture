package uk.gov.dwp.carersallowance.controller.defaultcontoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import uk.gov.dwp.carersallowance.controller.AbstractFormController;

import uk.gov.dwp.carersallowance.controller.PageOrder;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;

import java.util.Arrays;

@Controller
public class DefaultChangeOfCircsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultChangeOfCircsController.class);

    public DefaultChangeOfCircsController(final SessionManager sessionManager,
                                          final MessageSource messageSource,
                                          final TransformationManager transformationManager,
                                          @Qualifier("pageCircsOrder") final PageOrder pageCircsOrder) {
        super(sessionManager, messageSource, transformationManager, pageCircsOrder);
    }
}