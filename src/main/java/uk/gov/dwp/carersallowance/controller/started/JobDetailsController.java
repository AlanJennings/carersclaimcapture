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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class JobDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(JobDetailsController.class);

    private static final String CURRENT_PAGE  = "/your-income/employment/job-details/"; // parameter to indicate which job index
    private static final String PAGE_TITLE    = "Employer details - Your income";

    private static final String[] FIELDS = {"employerName",
                                            "phoneNumber",
                                            "address_lineOne",
                                            "address_lineTwo",
                                            "address_lineThree",
                                            "postcode",
                                            "startJobBeforeClaimDate",
                                            "jobStartDate_day",
                                            "jobStartDate_month",
                                            "jobStartDate_year",
                                            "finishedThisJob",
                                            "lastWorkDate_day",
                                            "lastWorkDate_month",
                                            "lastWorkDate_year",
                                            "p45LeavingDate_day",
                                            "p45LeavingDate_month",
                                            "p45LeavingDate_year",
                                            "hoursPerWeek"};

    @Autowired
    public JobDetailsController(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    public String getCurrentPage() {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        // TODO

        return super.getNextPage(request);
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

        validateMandatoryFields(fieldValues, "Employer's name", "employerName");
        validateMandatoryFields(fieldValues, "Contact number", "phoneNumber");
        validateRegexField(fieldValues, "Contact number", "phoneNumber", PHONE_REGEX);
        validateMandatoryFields(fieldValues, "Address", "address_lineOne", "address_lineTwo");
        validateMandatoryFields(fieldValues, "Did you start this job before 1 May 2016?", "startJobBeforeClaimDate");
        validateMandatoryFields(fieldValues, "Have you finished this job?", "finishedThisJob");

        if(fieldValue_Equals(fieldValues, "startJobBeforeClaimDate", "no")) {
            validateMandatoryDateField(fieldValues, "Job start date", "jobStartDate", new String[]{"jobStartDate_day", "jobStartDate_month", "jobStartDate_year"});
        }
        if(fieldValue_Equals(fieldValues, "startJobBeforeClaimDate", "no")) {
            validateMandatoryDateField(fieldValues, "Date last worked", "lastWorkDate", new String[]{"lastWorkDate_day", "lastWorkDate_month", "lastWorkDate_year"});
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
