package uk.gov.dwp.carersallowance.controller.circs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dwp.carersallowance.controller.AbstractFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AboutYouController extends AbstractFormController {

    public static final Logger LOG = LoggerFactory.getLogger(AboutYouController.class);

    private static final String CURRENT_PAGE            = "/circumstances/identification/about-you";
    private static final String PREVIOUS_PAGE           = "/circumstances/report-changes/change-selection";

    private static final String STOPPED_CARING_PAGE     = "/circumstances/report-changes/stopped-caring";
    private static final String EMPLOYMENT_CHANGE_PAGE  = "/circumstances/report-changes/employment-change";
    private static final String BREAKS_IN_CARE_PAGE     = "/circumstances/report-changes/breaks-in-care";
    private static final String ADDRESS_CHANGE_PAGE     = "/circumstances/report-changes/address-change";
    private static final String PAYMENT_CHANGE_PAGE     = "/circumstances/report-changes/payment-change";
    private static final String OTHER_CHANGE_PAGE       = "/circumstances/report-changes/other-change";

    private static final String CHANGE_TYPE_ANSWER_KEY = "changeTypeAnswer";

    /*  Change type answers */
    private static final String STOPPED_PROVIDING_CARE = "stoppedProvidingCare";
    private static final String INCOME_CHANGED = "incomeChanged";
    private static final String PATIENT_AWAY = "patientAway";
    private static final String CARER_AWAY = "carerAway";
    private static final String CHANGE_OF_ADDRESS = "changeOfAddress";
    private static final String CHANGE_PAYMENT_DETAILS = "changePaymentDetails";
    private static final String SOMETHING_ELSE = "somethingElse";

    public AboutYouController(SessionManager sessionManager, MessageSource messageSource, final TransformationManager transformationManager) {
        super(sessionManager, messageSource, transformationManager);
    }

    @Override
    public String getNextPage(HttpServletRequest request) {
        //  Get session data
        final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
        Map<String,Object> sessionData = session.getData();
        String changeTypeAnswer = (String) sessionData.get(CHANGE_TYPE_ANSWER_KEY);

        //  Set return value based on change type answer in session data
        String returnValue;
        switch (changeTypeAnswer) {
            case STOPPED_PROVIDING_CARE:
                returnValue = STOPPED_CARING_PAGE;
                break;
            case INCOME_CHANGED:
                returnValue = EMPLOYMENT_CHANGE_PAGE;
                break;
            case PATIENT_AWAY:
                returnValue = BREAKS_IN_CARE_PAGE;
                break;
            case CARER_AWAY:
                returnValue = BREAKS_IN_CARE_PAGE;
                break;
            case CHANGE_OF_ADDRESS:
                returnValue = ADDRESS_CHANGE_PAGE;
                break;
            case CHANGE_PAYMENT_DETAILS:
                returnValue = PAYMENT_CHANGE_PAGE;
                break;
            case SOMETHING_ELSE:
                returnValue = OTHER_CHANGE_PAGE;
                break;
            default:
                returnValue = null;
        }
        return returnValue;
    }

    @Override
    public String getPreviousPage(HttpServletRequest request) {
        return PREVIOUS_PAGE;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(HttpServletRequest request, Model model) {
        return super.getForm(request, model);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, Model model) {
        LOG.trace("Starting AboutYouController.postForm");
        try {
            return super.postForm(request, model);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AboutYouController.postForm\n");
        }
    }
}
