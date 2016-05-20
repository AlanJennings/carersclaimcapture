package uk.gov.dwp.carersallowance.controller.allowance;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;

@Controller
public class BenefitsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(BenefitsController.class);

    private static final String PREVIOUS_PAGE = "";
    private static final String CURRENT_PAGE  = "/allowance/benefits";
    private static final String NEXT_PAGE     = "/allowance/eligibility";
    private static final String PAGE_TITLE    = "What benefit does the person you care for get? - Can you get Carer's Allowance?";

    private static final String[] FIELDS = {"benefitsAnswer"};

    @Override
    public String getPreviousPage() {
        return PREVIOUS_PAGE;
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage() {
        return NEXT_PAGE;
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
    public String postForm(HttpServletRequest request, HttpSession session, Model model, RedirectAttributes redirectAttrs) {
        return super.postForm(request, session, model, redirectAttrs);
    }

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting BenefitsController.validate");

        validateMandatoryField(fieldValues, "benefitsAnswer", "What benefit does the person you care for get?");

        LOG.trace("Ending BenefitsController.validate");
    }
}
