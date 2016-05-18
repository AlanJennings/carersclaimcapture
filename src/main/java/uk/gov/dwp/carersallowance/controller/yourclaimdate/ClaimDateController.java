package uk.gov.dwp.carersallowance.controller.yourclaimdate;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;

//TODO
@Controller
public class ClaimDateController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimDateController.class);

    private static final String CURRENT_PAGE = "/your-claim-date/claim-date";
    private static final String NEXT_PAGE    = "redirect:/about-you/your-details";

    private static final String[] FIELDS = {"benefitsAnswer"};

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    public String getNextPage() {
        return NEXT_PAGE;
    }

    @Override
    public String[] getFields() {
        return FIELDS;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String showForm(HttpServletRequest request, Model model) {
        return super.showForm(request, model);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request,
                           @ModelAttribute("benefitsAnswer") String benefitsAnswer,
                           Model model,
                           RedirectAttributes redirectAttrs) {

        LOG.trace("Starting BenefitsController.postForm");
        LOG.debug("model = {}, redirectAttrs = {}", model, redirectAttrs);
        LOG.debug("benefitsAnswer = {}", benefitsAnswer);

        syncModelToSession(model, FIELDS, request);
        validate(model.asMap(), FIELDS);

        if(hasErrors()) {
            LOG.info("there are validation errors, re-showing form");
            model.addAttribute("errors", getValidationSummary());
            return showForm(request, model);
        }

        LOG.trace("Ending BenefitsController.postForm");
        return NEXT_PAGE;
    }

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(Map<String, Object> fieldValues, String[] fields) {
        LOG.trace("Starting BenefitsController.validate");

        getValidationSummary().reset();

        validateMandatoryField(fieldValues, "benefitsAnswer", "What benefit does the person you care for get?");
        LOG.trace("Ending BenefitsController.validate");
    }
}
