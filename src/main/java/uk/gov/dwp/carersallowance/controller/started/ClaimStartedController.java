package uk.gov.dwp.carersallowance.controller.started;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.CookieManager;
import uk.gov.dwp.carersallowance.session.SessionManager;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@Controller
public class ClaimStartedController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimStartedController.class);

    private static final String CURRENT_PAGE       = "/allowance/benefits";
    private final CookieManager cookieManager;

    @Inject
    public ClaimStartedController(final SessionManager sessionManager,
                                  final MessageSource messageSource,
                                  final CookieManager cookieManager) {
        super(sessionManager, messageSource);
        this.cookieManager = cookieManager;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String showForm(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        LOG.debug("Starting ClaimStartedController.showForm");
        try {
            if (request.getQueryString() == null || !request.getQueryString().contains("changing=true")) {
                cookieManager.addVersionCookie(response);
                cookieManager.addGaCookie(request, response);
                cookieManager.addSessionCookie(response, sessionManager.createSessionId());
            }
            super.showForm(request, model);
            return CURRENT_PAGE;
        } catch (RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.debug("Ending ClaimStartedController.showForm");
        }
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(final HttpServletRequest request, final HttpSession session, final Model model) {
        return super.postForm(request, session, model);
    }
}
