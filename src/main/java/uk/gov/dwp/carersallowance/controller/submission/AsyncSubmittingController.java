package uk.gov.dwp.carersallowance.controller.submission;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dwp.carersallowance.database.TransactionIdService;
import uk.gov.dwp.carersallowance.session.NoSessionException;

import javax.servlet.http.HttpServletRequest;
import uk.gov.dwp.carersallowance.database.Status;
import uk.gov.dwp.carersallowance.utils.C3Constants;

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

    @Autowired
    public AsyncSubmittingController(final TransactionIdService transactionIdService) {
        this.transactionIdService = transactionIdService;
    }

    @RequestMapping(value=SUCCESS_PAGE, method = RequestMethod.GET)
    public String showThankYouPage(HttpServletRequest request) {
        return SUCCESS_PAGE;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(HttpServletRequest request, final Model model) {
        return postForm(request, model);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, final Model model) {
        LOG.trace("Starting AsyncSubmittingController.postForm");
        try {
            final String transactionId = request.getParameter(C3Constants.TRANSACTION_ID);
            String retryCount = request.getParameter("retryCount");
            if (StringUtils.isEmpty(retryCount)) {
                retryCount = "0";
            }
            request.setAttribute(C3Constants.TRANSACTION_ID, transactionId);
            request.setAttribute(C3Constants.IS_CLAIM, C3Constants.TRUE.equals(request.getParameter(C3Constants.IS_CLAIM)));
            request.setAttribute("retryCount", Integer.valueOf(retryCount) + 1);

            if (StringUtils.isEmpty(transactionId)) {
                return processTransactionStatusResponse(null, Integer.valueOf(retryCount));
            }

            String transactionStatus = transactionIdService.getTransactionStatusById(transactionId);
            if (StringUtils.isEmpty(transactionStatus)) {
                transactionStatus = GENERATED;
            }
            LOG.info("Checking transaction status:{} for transactionId:{}", transactionStatus, transactionId);
            return processTransactionStatusResponse(transactionStatus, Integer.valueOf(retryCount));
        } catch (NoSessionException nse) {
            return "redirect:" + SUCCESS_PAGE;
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AsyncSubmittingController.postForm");
        }
    }

    private String processTransactionStatusResponse(final String transactionStatus, final Integer retryCount) {
        if (SUCCESS.equals(transactionStatus) || ACKNOWLEDGED.equals(transactionStatus)) {
            return "redirect:" + SUCCESS_PAGE;
        }
        if ((SUBMITTED.equals(transactionStatus) || GENERATED.equals(transactionStatus)) && retryCount < 5) {
            return CURRENT_PAGE;
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
