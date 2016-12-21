package uk.gov.dwp.carersallowance.controller.defaultcontoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class DefaultChangeOfCircsController extends AbstractFormController{

    private static final String[] PAGES = {
        "/circumstances/report-changes/selection"
    };

    @Autowired
    public DefaultChangeOfCircsController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
        pageList = new ArrayList<>(Arrays.asList(PAGES));
    }

}
