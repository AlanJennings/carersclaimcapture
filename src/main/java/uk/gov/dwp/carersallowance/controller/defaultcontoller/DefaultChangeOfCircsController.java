package uk.gov.dwp.carersallowance.controller.defaultcontoller;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class DefaultChangeOfCircsController extends AbstractFormController {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultChangeOfCircsController.class);

    private static final String ORIGIN_PAGE           = "/circumstances/report-changes/origin";
    private static final String CHANGE_SELECTION_PAGE = "/circumstances/report-changes/change-selection";

    private static final String ORIGIN_NI = "GB-NIR";
    private static final String[] PAGES = {
            ORIGIN_PAGE,
            CHANGE_SELECTION_PAGE
    };

    @Value("${origin.tag}")
    private String originTag;

    public DefaultChangeOfCircsController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
        pageList = Arrays.asList(DefaultChangeOfCircsController.PAGES);
    }


    // Tactical solution until generic start page controller is done
    public String handleRequest(HttpServletRequest request, HttpServletResponse response, Model model) throws NoSuchRequestHandlingMethodException {
        LOG.info("Started DefaultFormController.handleRequest, getServletPath:"+request.getServletPath() + " Origin:"+ originTag);

        if (request.getServletPath().equals(ORIGIN_PAGE) && !originTag.equals(ORIGIN_NI)){
            return "redirect:" + CHANGE_SELECTION_PAGE;
        }

        return super.handleRequest(request,response,model);

    }
}