package uk.gov.dwp.carersallowance.controller;

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

import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class EducationController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(EducationController.class);

    private static final String CURRENT_PAGE  = "/education/your-course-details";
    private static final String PAGE_TITLE    = "Your course details - Education";

    private static final String[] FIELDS = {"beenInEducationSinceClaimDate",
                                            "courseTitle",
                                            "nameOfSchoolCollegeOrUniversity",
                                            "nameOfMainTeacherOrTutor",
                                            "courseContactNumber",
                                            "educationStartDate_day",
                                            "educationStartDate_month",
                                            "educationStartDate_year",
                                            "educationExpectedEndDate_day",
                                            "educationExpectedEndDate_month",
                                            "educationExpectedEndDate_year"};

    @Autowired
    public EducationController(SessionManager sessionManager) {
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

        validateMandatoryField(fieldValues, "beenInEducationSinceClaimDate", "Have you been on a course of education since your claim date?");

        if(fieldValue_Equals(fieldValues, "beenInEducationSinceClaimDate", "yes")) {
            validateMandatoryField(fieldValues, "courseTitle", "Have you been on a course of education since your claim date?");
            validateMandatoryField(fieldValues, "nameOfSchoolCollegeOrUniversity", "Have you been on a course of education since your claim date?");
            validateMandatoryField(fieldValues, "nameOfMainTeacherOrTutor", "Have you been on a course of education since your claim date?");

            validateMandatoryDateField(fieldValues, "educationStartDate", "When did you start the course?");
            validateMandatoryDateField(fieldValues, "educationeducationExpectedEndDate", "When did the course end or when will you finish?");
        }

        LOG.trace("Ending BenefitsController.validate");
    }
}
