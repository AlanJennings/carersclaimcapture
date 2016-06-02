package uk.gov.dwp.carersallowance.controller.started;

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
public class YourIncomeController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(YourIncomeController.class);

    private static final String CURRENT_PAGE  = "/your-income/your-income";
    private static final String PAGE_TITLE    = "Your income";

    private static final String[] FIELDS = {"beenEmployedSince6MonthsBeforeClaim",
                                            "beenSelfEmployedSince1WeekBeforeClaim",
                                            "yourIncome"/*,
                                            "yourIncome_sickpay",       // these should be multi-valued checkboxes
                                            "yourIncome_patmatadoppay",
                                            "yourIncome_fostering",
                                            "yourIncome_directpay",
                                            "yourIncome_anyother",
                                            "yourIncome_none"*/};

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage() {
        // TODO this depends on the answers to beenEmployedSince6MonthsBeforeClaim
        //    & beenSelfEmployedSince1WeekBeforeClaim
        //    & yourIncome

        return super.getNextPage();
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
// TODO the dates are from earlier in the claim
        validateMandatoryFields(fieldValues, "Have you been in employment since 1 December 2015?", "beenEmployedSince6MonthsBeforeClaim");
        validateMandatoryFields(fieldValues, "Have you been in self-employment since 25 May 2016?", "beenSelfEmployedSince1WeekBeforeClaim");
        validateMandatoryFields(fieldValues, "What other income have you had since 1 June 2016?", "yourIncome");

        LOG.trace("Ending BenefitsController.validate");
    }
}
