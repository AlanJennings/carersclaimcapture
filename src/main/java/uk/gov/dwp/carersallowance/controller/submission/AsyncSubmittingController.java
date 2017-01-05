package uk.gov.dwp.carersallowance.controller.submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dwp.carersallowance.database.TransactionIdService;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;

import javax.servlet.http.HttpServletRequest;
import uk.gov.dwp.carersallowance.database.Status;

@Controller
public class AsyncSubmittingController {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncSubmittingController.class);

    private static final String CURRENT_PAGE        = "/async-submitting";
    private static final String SUCCESS_PAGE        = "/thankyou/apply-carers";
    private static final String ERROR               = "/error";
    private static final String TIMEOUT_ERROR       = "/timeout";
    private static final String ERROR_RETRY         = "/retry-error";

    private static final String SUCCESS             = Status.SUCCESS.getStatus();
    private static final String ACKNOWLEDGED        = Status.ACKNOWLEDGED.getStatus();
    private static final String GENERATED           = Status.GENERATED.getStatus();
    private static final String SUBMITTED           = Status.SUBMITTED.getStatus();
    private static final String SERVICE_UNAVAILABLE = Status.SERVICE_UNAVAILABLE.getStatus();

    private final TransactionIdService transactionIdService;
    private final SessionManager sessionManager;

    @Autowired
    public AsyncSubmittingController(final TransactionIdService transactionIdService, final SessionManager sessionManager) {
        this.transactionIdService = transactionIdService;
        this.sessionManager = sessionManager;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(HttpServletRequest request) {
        return CURRENT_PAGE;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request) {
        LOG.trace("Starting AsyncSubmittingController.postForm");
        try {
            final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
            final String transactionId = (String)session.getAttribute("transactionId");
            String transactionStatus = transactionIdService.getTransactionStatusById(transactionId);

            LOG.info("Checking transaction status:{} for transactionId:{}", transactionStatus, transactionId);
            return processTransactionStatusResponse(transactionStatus);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AsyncSubmittingController.postForm");
        }
    }

    private String processTransactionStatusResponse(final String transactionStatus) {
        if (SUCCESS.equals(transactionStatus) || ACKNOWLEDGED.equals(transactionStatus)) {
            return "redirect:" + SUCCESS_PAGE;
        }
        if (SUBMITTED.equals(transactionStatus) || GENERATED.equals(transactionStatus)) {
            return "redirect:" + CURRENT_PAGE;
        }
        if (SERVICE_UNAVAILABLE.equals(transactionStatus)) {
            return "redirect:" + ERROR_RETRY;
        }
        if ("".equals(transactionStatus)) {
            return "redirect:" + TIMEOUT_ERROR;
        }
        return "redirect:" + ERROR;
    }
}
