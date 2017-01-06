package uk.gov.dwp.carersallowance.controller.circs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;

import javax.servlet.http.HttpServletRequest;

@Controller
public class StoppedProvidingCareController extends AbstractFormController {

    public static final Logger LOG = LoggerFactory.getLogger(StoppedProvidingCareController.class);

    private static final String NEXT_PAGE = "/circumstances/consent-and-declaration/declaration";
    private static final String PREVIOUS_PAGE = "/circumstances/identification/about-you";
    private static final String CURRENT_PAGE = "/circumstances/report-changes/stopped-caring";

    public StoppedProvidingCareController(final SessionManager sessionManager,
                                          final MessageSource messageSource,
                                          final TransformationManager transformationManager) {
        super(sessionManager, messageSource, transformationManager);
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        return NEXT_PAGE;
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return PREVIOUS_PAGE;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(HttpServletRequest request, Model model) {
        return super.getForm(request, model);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, Model model) {
        LOG.trace("Starting StoppedProvidingCareController.postForm");
        try {
            return super.postForm(request, model);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending StoppedProvidingCareController.postForm\n");
        }
    }
}
