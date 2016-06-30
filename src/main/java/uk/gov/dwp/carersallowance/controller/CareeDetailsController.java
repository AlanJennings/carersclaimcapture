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
public class CareeDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(CareeDetailsController.class);

    private static final String CURRENT_PAGE  = "/care-you-provide/their-personal-details";
    private static final String PAGE_TITLE    = "Details of the person you care for - About the person you care for";

    private static final String[] FIELDS = {"title",
                                            "firstName",
                                            "middleName",
                                            "surname",
                                            "nationalInsuranceNumber",
                                            "dateOfBirth_day",
                                            "dateOfBirth_month",
                                            "dateOfBirth_year",
                                            "relationship",
                                            "sameAddress",
                                            "address_lineOne",
                                            "address_lineTwo",
                                            "address_lineThree",
                                            "postcode"};

    @Autowired
    public CareeDetailsController(SessionManager sessionManager) {
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

        validateMandatoryField(fieldValues, "title", "Title");
        validateMandatoryField(fieldValues, "firstName", "First name");
        validateMandatoryField(fieldValues, "surname", "Last name");
        validateMandatoryDateField(fieldValues, "Date of Birth", "dateOfBirth", new String[]{"dateOfBirth_day", "dateOfBirth_month", "dateOfBirth_year"});
        validateMandatoryField(fieldValues, "relationship", "What's their relationship to you?");
        validateMandatoryField(fieldValues, "sameAddress", "Do they live at the same address as you?");
        // address and postcode are not mandatory

        LOG.trace("Ending BenefitsController.validate");
    }
}