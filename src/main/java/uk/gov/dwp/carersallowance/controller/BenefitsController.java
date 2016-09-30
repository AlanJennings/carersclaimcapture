package uk.gov.dwp.carersallowance.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import uk.gov.dwp.carersallowance.session.SessionManager;

@Controller
public class BenefitsController extends AbstractFormController {

    @Autowired
    public BenefitsController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
    }

    public String showForm(HttpServletRequest request, Model model) {
        return super.showForm(request, model);
    }

    public String postForm(HttpServletRequest request, HttpSession session, Model model) {
        return super.postForm(request, session, model);
    }
}
