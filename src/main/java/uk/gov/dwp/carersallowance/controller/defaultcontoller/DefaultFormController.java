package uk.gov.dwp.carersallowance.controller.defaultcontoller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.utils.Parameters;

/**
 * General request handler for basic request.
 * TODO, migrate stuff to this as and when
 * @author drh
 *
 *         TODO detach from the AbstractFormController or split it into two and farm some of
 *         the stuff into a base for the specific controllers and not the general one
 */
@Controller
public class DefaultFormController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFormController.class);

    @Autowired
    private DefaultFormController defaultFormController;

    private static final String[] PAGES = {
        "/allowance/benefits",
        "/allowance/eligibility",
        "/allowance/approve",
        "/disclaimer/disclaimer",
        "/third-party/third-party",
        "/your-claim-date/claim-date",
        "/about-you/your-details",
        "/about-you/marital-status",
        "/about-you/contact-details",
        "/about-you/nationality-and-residency",
        "/about-you/other-eea-state-or-switzerland",
        "/your-partner/personal-details",
        "/care-you-provide/their-personal-details",
        "/care-you-provide/more-about-the-care",
        "/breaks/breaks-in-care",
        "/education/your-course-details",
        "/your-income/your-income",
        //removed to get through journey
//            "/your-income/employment/been-employed",
//            "/your-income/self-employment/self-employment-dates",
//            "/your-income/self-employment/pensions-and-expenses",
//            "/your-income/employment/additional-info",
//            "/your-income/statutory-sick-pay",
//            "/your-income/smp-spa-sap",
//            "/your-income/fostering-allowance",
//            "/your-income/direct-payment",
//            "/your-income/other-income",
        "/pay-details/how-we-pay-you",
        "/information/additional-info",
        "/preview",
        "/consent-and-declaration/declaration",
        "/submit-claim"
    };

    @Autowired
    public DefaultFormController(SessionManager sessionManager, MessageSource messageSource) {
        super(sessionManager, messageSource);
        pageList = new ArrayList<>(Arrays.asList(PAGES));
    }


}

