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
public class AdditionalInfoController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(AdditionalInfoController.class);

    private static final String CURRENT_PAGE  = "/information/additional-info";
    private static final String PAGE_TITLE    = "Additional information - Information";

    private static final String[] FIELDS = {"anythingElse",
                                            "anythingElseText",
                                            "welshCommunication"};

    @Autowired
    public AdditionalInfoController(SessionManager sessionManager) {
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

        validateMandatoryFields(fieldValues, "Do you want to tell us any additional information about your claim?", "anythingElse");
        if(fieldValue_Equals(fieldValues, "anythingElse", "yes")) {
            validateMandatoryFields(fieldValues, "Tell us anything else you think we should know about your claim", "anythingElseText");
        }

        validateMandatoryFields(fieldValues, "Do you live in Wales and want to receive future communications in Welsh?", "maritalStatus");

        LOG.trace("Ending BenefitsController.validate");
    }
}
