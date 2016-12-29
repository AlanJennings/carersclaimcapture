package uk.gov.dwp.carersallowance.controller.defaultcontoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import uk.gov.dwp.carersallowance.controller.AbstractFormController;

import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;

import java.util.Arrays;

@Controller
public class DefaultChangeOfCircsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultChangeOfCircsController.class);

    private static final String ORIGIN_PAGE           = "/circumstances/report-changes/selection";
    private static final String CHANGE_SELECTION_PAGE = "/circumstances/report-changes/change-selection";

    private static final String[] PAGES = {
            ORIGIN_PAGE,
            CHANGE_SELECTION_PAGE
    };

    public DefaultChangeOfCircsController(final SessionManager sessionManager, final MessageSource messageSource, final TransformationManager transformationManager) {
        super(sessionManager, messageSource, transformationManager);
        pageList = Arrays.asList(DefaultChangeOfCircsController.PAGES);
    }
}