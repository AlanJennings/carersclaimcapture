package uk.gov.dwp.carersallowance.controller.income;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class EmploymentDetailsController extends AbstractFormController {
    private static final String YOUR_INCOME_PAGE      = "/your-income/your-income";
    private static final String PARENT_PAGE           = "/your-income/employment/been-employed";
//    private static final String CURRENT_PAGE          = "/your-income/employment/job-details"; // parameter to indicate which job index
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

//    @Override
//    public String getCurrentPage(HttpServletRequest request) {
//        return CURRENT_PAGE;
//    }
//
    @Override
    public String getNextPage(HttpServletRequest request) {
        return NEXT_PAGE;
    }
}
