package uk.gov.dwp.carersallowance.controller.submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dwp.carersallowance.database.TransactionIdService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AsyncSubmittingController {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncSubmittingController.class);

    private static final String CURRENT_PAGE       = "/async-submitting";
    private static final String SUCCESS_PAGE       = "/thankyou/apply-carers";

    private TransactionIdService transactionIdService;

    @Autowired
    public AsyncSubmittingController(TransactionIdService transactionIdService) {
        this.transactionIdService = transactionIdService;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(HttpServletRequest request) {
        return CURRENT_PAGE;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request) {
        LOG.trace("Starting AsyncSubmittingController.postForm");
        try {
//            String transactionStatus = transactionIdService.getTransactionStatusById(claim.transactionId.getOrElse(""));
//
//            Logger.info("Checking transaction status: {} for {} {}", transactionStatus, claim.key, claim.uuid);
//            switch(transactionStatus) {
//                case SUCCESS || ACKNOWLEDGED :
//                    Redirect(redirectThankYou);
//                    break;
//                case GENERATED || SUBMITTED :
//                    Redirect(redirectSubmitting);
//                    break;
//                case SERVICE_UNAVAILABLE :
//                    Redirect(redirectErrorRetry);
//                    break;
//                case "" :
//                    Redirect(redirectTimeout);
//                    break;
//                default :
//                    Redirect(redirectError);
//            }
            return "redirect:" + SUCCESS_PAGE;
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AsyncSubmittingController.postForm");
        }
    }
}
