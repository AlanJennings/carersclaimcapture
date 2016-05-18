package uk.gov.dwp.carersallowance.controller.allowance;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;

@Controller
public class ApproveContoller extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EligibilityController.class);

    private static final String CURRENT_PAGE = "/allowance/approve";
    private static final String NEXT_PAGE    = "redirect:/disclaimer/disclaimer";

    private static final String[] FIELDS = {};

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
                           Model model,
                           RedirectAttributes redirectAttrs) {

        LOG.trace("Starting ApproveContoller.postForm");
        try {
            LOG.debug("model = {}", model);

            syncModelToSession(model, FIELDS, request);
            validate(model.asMap(), FIELDS);

            if(hasErrors()) {
                LOG.info("there are validation errors, re-showing form");
                model.addAttribute("errors", getValidationSummary());
                return showForm(request, model);
            }

            return NEXT_PAGE;
        } finally {
            LOG.trace("Starting ApproveContoller.postForm");
        }
    }

    @Override
    protected void validate(Map<String, Object> fieldValues, String[] fields) {
        getValidationSummary().reset();
    }
}
