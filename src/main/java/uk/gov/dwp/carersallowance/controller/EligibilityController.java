package uk.gov.dwp.carersallowance.controller;

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

@Controller
public class EligibilityController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EligibilityController.class);

    private static final String CURRENT_PAGE  = "/allowance/eligibility";
    private static final String PAGE_TITLE    = "Eligibility - Can you get Carer's Allowance?";

    private static final String[] FIELDS = {"over35HoursAWeek", "over16YearsOld", "originCountry"};

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
    public String postForm(HttpServletRequest request, HttpSession session, Model model, RedirectAttributes redirectAttrs) {
        return super.postForm(request, session, model, redirectAttrs);
    }

    @Override
    protected void validate(Map<String, String[]> fieldValues, String[] fields) {
        LOG.trace("Starting EligibilityController.validate");

        validateMandatoryFields(fieldValues, "Do you spend 35 hours or more each week caring for the person you care for?", "over35HoursAWeek");
        validateMandatoryFields(fieldValues, "Are you aged 16 or over?", "over16YearsOld");
        validateMandatoryFields(fieldValues, "Which country do you live in?", "originCountry");
        LOG.trace("Ending EligibilityController.validate");
    }
}

