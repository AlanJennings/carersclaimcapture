package uk.gov.dwp.carersallowance.controller;

import java.util.List;
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

import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class EmploymentDetailsController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EmploymentDetailsController.class);

    private static final String PAGE_NAME             = "page.job-details";
    private static final String YOUR_INCOME_PAGE      = "/your-income/your-income";
    private static final String PARENT_PAGE           = "/your-income/employment/been-employed";
    private static final String CURRENT_PAGE          = "/your-income/employment/job-details"; // parameter to indicate which job index
    private static final String NEXT_PAGE             = "/your-income/employment/last-wage";

    private static final String  FIELD_COLLECTION_NAME = EmploymentSummaryController.FIELD_COLLECTION_NAME;

    @Autowired
    public EmploymentDetailsController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        List<Map<String, String>> employments = getFieldCollections(request.getSession(), FIELD_COLLECTION_NAME, true);
        if(employments == null || employments.isEmpty()) {
            return YOUR_INCOME_PAGE;
        }
        return PARENT_PAGE;
    }

    @Override
    public String getCurrentPage(HttpServletRequest request) {
        return CURRENT_PAGE;
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        return NEXT_PAGE;
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
        LOG.trace("Started EmploymentDetailsController.validate");
        // TODO the dates are from earlier in the claim

        validateMandatoryField(fieldValues, "employerName");
        validateMandatoryField(fieldValues, "employmentPhoneNumber");
        validateRegexField(fieldValues, "Contact number", "phoneNumber", PHONE_REGEX);
        validateAddressFields(fieldValues, "Address", "employerAddress", new String[]{"employmentAddressLineOne", "employmentAddressLineTwo", "employmentAddressLineThree"});
        validateMandatoryField(fieldValues, "startJobBeforeClaimDate");
        validateMandatoryField(fieldValues, "finishedThisJob");

        if(fieldValue_Equals(fieldValues, "startJobBeforeClaimDate", "no")) {
            validateMandatoryDateField(fieldValues, "jobStartDate");
        }
        if(fieldValue_Equals(fieldValues, "finishedThisJob", "yes")) {
            validateMandatoryDateField(fieldValues, "lastWorkDate");
        }

        LOG.trace("Ending EmploymentDetailsController.validate");
    }
}
