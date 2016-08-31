package uk.gov.dwp.carersallowance.controller;

import java.util.Arrays;
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

import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class YourDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(YourDetailsController.class);

    private static final String CURRENT_PAGE  = "/about-you/your-details";
    private static final String PAGE_TITLE    = "Your details - About you - the carer";

    private static final String[] FIELDS = {"carerTitle",
                                            "carerFirstName",
                                            "carerMiddleName",
                                            "carerSurname",
                                            "carerNationalInsuranceNumber",
                                            "carerDateOfBirth_day",
                                            "carerDateOfBirth_month",
                                            "carerDateOfBirth_year"};

    /**
     * @deprecated update the test and remove these
     * @param sessionManager
     */
    public YourDetailsController(SessionManager sessionManager) {
        this(sessionManager, null);
    }

    @Autowired
    public YourDetailsController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
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

    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        throw new UnsupportedOperationException("validate(Map<String, String[]> fieldValues, String[] fields)");
    }

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(Map<String, String[]> fieldValues, String[] fields, String[] enabledFields) {
        LOG.trace("Starting YourDetailsController.validate");

        LOG.info("EnabledFields = {}", enabledFields == null ? null : Arrays.asList(enabledFields));

        for(String field: enabledFields) {
            LOG.debug("validating enabled field {}", field);
            validateField(fieldValues, field);
        }

//        validateMandatoryField(fieldValues, "carerTitle");
//        validateMandatoryField(fieldValues, "carerFirstName");
//        // "middleName" is optional,
//        validateMandatoryField(fieldValues, "carerSurname");
//        validateMandatoryField(fieldValues, "carerNationalInsuranceNumber");

//        validateMandatoryDateField(fieldValues, "carerDateOfBirth");

        LOG.trace("Ending YourDetailsController.validate");
    }
}
