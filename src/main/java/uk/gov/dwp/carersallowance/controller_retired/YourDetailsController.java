package uk.gov.dwp.carersallowance.controller_retired;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class YourDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(YourDetailsController.class);

    private static final String PAGE_NAME     = "page.your-details";
    private static final String CURRENT_PAGE  = "/about-you/your-details";

    @Autowired
    public YourDetailsController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

    /**
     * We can replace currentPage with the value from message.properties, IF we match all urls; i.e. single controller
     * (but not static resources) and manage to move validations to a data driven design (javascript is the sticking point)
     */
    @Override
    public String getCurrentPage(HttpServletRequest request) {
        return CURRENT_PAGE;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String showForm(HttpServletRequest request, Model model) {
        return super.showForm(request, model);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, HttpSession session, Model model) {
        return super.postForm(request, session, model);
    }

    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting YourDetailsController.validate");

        validateField(fieldValues, "carerTitle");
        validateField(fieldValues, "carerFirstName");
        // "middleName" is optional,
        validateField(fieldValues, "carerSurname");
        validateField(fieldValues, "carerNationalInsuranceNumber");

        validateField(fieldValues, "carerDateOfBirth");

        LOG.trace("Ending YourDetailsController.validate");
    }

}
