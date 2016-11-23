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

//@Controller
public class CareeDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(CareeDetailsController.class);

    private static final String PAGE_NAME     = "page.their-personal-details";
    private static final String CURRENT_PAGE  = "/care-you-provide/their-personal-details";

// careeTitle, careeFirstName, careeMiddleName, careeSurname, careeNationalInsuranceNumber, careeDateOfBirth_day, careeDateOfBirth_month, careeDateOfBirth_year, careeRelationship, careeSameAddress, careeAddressLineOne, careeAddressLineTwo, careeAddressLineThree, careePostcode};
    @Autowired
    public CareeDetailsController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    @Override
    public String getCurrentPage(HttpServletRequest request) {
        return CURRENT_PAGE;
    }

    @Override
    protected String getPageName() {
        return PAGE_NAME;
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

        validateMandatoryField(fieldValues, "careeTitle");
        validateMandatoryField(fieldValues, "careeFirstName");
        validateMandatoryField(fieldValues, "careeSurname");
        validateMandatoryDateField(fieldValues, "careeDateOfBirth");
        validateMandatoryField(fieldValues, "careeRelationship");
        validateMandatoryField(fieldValues, "careeSameAddress");
        // address and postcode are not mandatory

        LOG.trace("Ending BenefitsController.validate");
    }
}
