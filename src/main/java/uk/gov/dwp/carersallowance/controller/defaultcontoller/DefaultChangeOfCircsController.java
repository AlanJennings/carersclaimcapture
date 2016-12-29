package uk.gov.dwp.carersallowance.controller.defaultcontoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import uk.gov.dwp.carersallowance.controller.AbstractFormController;

import uk.gov.dwp.carersallowance.session.SessionManager;

import java.util.Arrays;

@Controller
public class DefaultChangeOfCircsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultChangeOfCircsController.class);

    private static final String ORIGIN_PAGE             = "/circumstances/report-changes/selection";
    private static final String CHANGE_SELECTION_PAGE   = "/circumstances/report-changes/change-selection";
    private static final String ABOUT_YOU_PAGE          = "/circumstances/identification/about-you";

    private static final String[] PAGES = {
            ORIGIN_PAGE,
            CHANGE_SELECTION_PAGE,
            ABOUT_YOU_PAGE
    };

    public DefaultChangeOfCircsController(final SessionManager sessionManager, final MessageSource messageSource) {
        super(sessionManager, messageSource);
        pageList = Arrays.asList(DefaultChangeOfCircsController.PAGES);
    }
}