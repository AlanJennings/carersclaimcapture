package uk.gov.dwp.carersallowance.controller.started;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dwp.carersallowance.controller.XmlBuilder;
import uk.gov.dwp.carersallowance.controller.XmlClaimReader;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultFormController;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@Controller
public class ClaimStartedController {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimStartedController.class);

    private final DefaultFormController defaultFormController;

    private static final String CURRENT_PAGE = "/allowance/benefits";

    @Inject
    public ClaimStartedController(final DefaultFormController defaultFormController) {
        this.defaultFormController = defaultFormController;
    }

    @RequestMapping(value = CURRENT_PAGE, method = RequestMethod.GET)
    public String showForm(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        defaultFormController.createSessionVariables(request, response, "defaultClaim.xml");
        defaultFormController.showForm(request, model);
        return CURRENT_PAGE;
    }

    @RequestMapping(value = CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(final HttpServletRequest request, final Model model) {
        return defaultFormController.postForm(request, model);
    }
}
