package uk.gov.dwp.carersallowance.controller.started;

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

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class ConsentDeclarationController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(ConsentDeclarationController.class);

    private static final String CURRENT_PAGE  = "/consent-and-declaration/declaration";
    private static final String PAGE_TITLE    = "Declaration - Consent and declaration";

    private static final String[] FIELDS = {"tellUsWhyInformation",
                                            "tellUsWhyPerson"};

    @Autowired
    public ConsentDeclarationController(SessionManager sessionManager) {
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

        validateMandatoryField(fieldValues, "tellUsWhyInformation", "Do you agree to the Carer's Allowance Unit contacting anyone mentioned in this form?");
        if(fieldValue_Equals(fieldValues, "tellUsWhyInformation", "no")) {
            validateMandatoryField(fieldValues, "tellUsWhyPerson", "List anyone you don't want to be contacted and say why.");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
