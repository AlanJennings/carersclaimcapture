package uk.gov.dwp.carersallowance.controller.allowance;

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

@Controller
public class EligibilityController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EligibilityController.class);

    private static final String CURRENT_PAGE = "/allowance/eligibility";
    private static final String FORWARD = "redirect:/allowance/approve";

    private static final String[] FIELDS = {"over35HoursAWeek", "over16YearsOld", "originCountry"};

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String showForm(HttpServletRequest request, Model model) {
        LOG.trace("Starting EligibilityController.showForm");
        try {
            LOG.debug("model = {}", model);

            syncSessionToModel(request, FIELDS, model);

            return CURRENT_PAGE;        // returns the view name
        } finally {
            LOG.trace("Ending EligibilityController.showForm");
        }
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request,
                           @ModelAttribute("over35HoursAWeek") String over35HoursAWeek,
                           @ModelAttribute("over16YearsOld") String over16YearsOld,
                           @ModelAttribute("originCountry") String originCountry,
                           Model model,
                           RedirectAttributes redirectAttrs) {

        LOG.trace("Starting EligibilityController.postForm");
        try {
            LOG.debug("model = {}", model);
            LOG.debug("over35HoursAWeek = {}, over16YearsOld = {}, originCountry = {}", over35HoursAWeek, over16YearsOld, originCountry);

            syncModelToSession(model, FIELDS, request);
            validate(model.asMap(), FIELDS);

            if(hasErrors()) {
                LOG.info("there are validation errors, re-showing form");
                model.addAttribute("errors", getValidationSummary());
                return showForm(request, model);
            }

            return FORWARD;
        } finally {
            LOG.trace("Ending EligibilityController.postForm");
        }
    }

    @Override
    protected void validate(Map<String, Object> fieldValues, String[] fields) {
        LOG.trace("Starting EligibilityController.validate");

        getValidationSummary().reset();

        validateMandatoryField(fieldValues, "over35HoursAWeek", "Do you spend 35 hours or more each week caring for the person you care for?");
        validateMandatoryField(fieldValues, "over16YearsOld", "Are you aged 16 or over?");
        validateMandatoryField(fieldValues, "originCountry", "Which country do you live in?");
        LOG.trace("Ending EligibilityController.validate");
    }
}

