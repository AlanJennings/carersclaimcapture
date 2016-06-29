package uk.gov.dwp.carersallowance.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class YourDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(YourDetailsController.class);

    private static final String CURRENT_PAGE  = "/about-you/your-details";
    private static final String PAGE_TITLE    = "Your details - About you - the carer";

    private static final String[] FIELDS = {"title",
                                            "firstName",
                                            "middleName",
                                            "surname",
                                            "nationalInsuranceNumber",
                                            "dateOfBirth_day",
                                            "dateOfBirth_month",
                                            "dateOfBirth_year"};

    @Autowired
    public YourDetailsController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String[] getFields() {
        return FIELDS;
    }

    @Override
    public String getPageTitle() {
        return PAGE_TITLE;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String showForm(HttpServletRequest request, Model model) {
        return super.showForm(request, model);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, HttpSession session, Model model) {
        return super.postForm(request, session, model);
    }

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting BenefitsController.validate");

        validateMandatoryFields(fieldValues, "Title", "title");
        validateMandatoryFields(fieldValues, "First name", "firstName");
        // "middleName" is optional,
        validateMandatoryFields(fieldValues, "Last name","surname");
        validateMandatoryFields(fieldValues, "National Insurance number", "nationalInsuranceNumber");

        validateMandatoryDateField(fieldValues, "Date of Birth", "dateOfBirth", new String[]{"dateOfBirth_day", "dateOfBirth_month", "dateOfBirth_year"});

        LOG.trace("Ending BenefitsController.validate");
    }
}
