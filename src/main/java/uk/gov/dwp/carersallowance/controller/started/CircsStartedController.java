package uk.gov.dwp.carersallowance.controller.started;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultChangeOfCircsController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@Controller
public class CircsStartedController {
    private static final Logger LOG = LoggerFactory.getLogger(CircsStartedController.class);

    private final DefaultChangeOfCircsController defaultChangeOfCircsController;

    private static final String CURRENT_CIRCS_PAGE = "/circumstances/report-changes/selection";
    private static final String CHANGE_SELECTION_PAGE = "/circumstances/report-changes/change-selection";
    private static final String ORIGIN_NI = "GB-NIR";
    private final String originTag;

    @Inject
    public CircsStartedController(final @Value("${origin.tag}") String originTag,
                                  final DefaultChangeOfCircsController defaultChangeOfCircsController) {
        this.defaultChangeOfCircsController = defaultChangeOfCircsController;
        this.originTag = originTag;
    }

    @RequestMapping(value = CURRENT_CIRCS_PAGE, method = RequestMethod.POST)
    public String postCircsForm(final HttpServletRequest request, final Model model) {
        return defaultChangeOfCircsController.postForm(request, model);
    }

    @RequestMapping(value = CURRENT_CIRCS_PAGE, method = RequestMethod.GET)
    public String showCircsForm(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        // Load replicate data in by setting circs replica xml filename here ...
        String replicaData = null;
        defaultChangeOfCircsController.createSessionVariables(request, response, replicaData);
        if (originTag.equals(ORIGIN_NI)) {
            defaultChangeOfCircsController.showForm(request, model);
            return CURRENT_CIRCS_PAGE;
        }
        return "redirect:" + CHANGE_SELECTION_PAGE;
    }
}
