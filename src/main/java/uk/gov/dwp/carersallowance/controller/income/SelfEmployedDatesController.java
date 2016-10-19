package uk.gov.dwp.carersallowance.controller.income;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class SelfEmployedDatesController extends AbstractFormController {
//    private static final String PAGE_NAME     = "page.self-employment-dates";
//    private static final String CURRENT_PAGE  = "/your-income/self-employment/self-employment-dates";

    @Autowired
    public SelfEmployedDatesController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return super.getPreviousPage(request, YourIncomeController.getIncomePageList(request.getSession()));
    }

//    @Override
//    protected String getPageName() {
//        return PAGE_NAME;
//    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        return super.getNextPage(request, YourIncomeController.getIncomePageList(request.getSession()));
    }

//    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
//    public String showForm(HttpServletRequest request, Model model) {
//        return super.showForm(request, model);
//    }
//
//    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
//    public String postForm(HttpServletRequest request, HttpSession session, Model model) {
//        return super.postForm(request, session, model);
//    }
}
