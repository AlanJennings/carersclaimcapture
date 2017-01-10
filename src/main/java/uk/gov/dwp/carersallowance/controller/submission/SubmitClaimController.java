package uk.gov.dwp.carersallowance.controller.submission;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.submission.SubmitClaimService;

import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList.MappingException;

/**
 * Controller to submit the overall claim
 * It does not expect any request parameters, and does not validate anything
 * It just creates the claim XML and sends it.  If is successful, then it redirects to a success page
 * otherwise it redirects to a retry page.  We may do a waiting page, as we currently do. (TODO)
 */
@Controller
public class SubmitClaimController {
    private static final Logger LOG = LoggerFactory.getLogger(SubmitClaimController.class);

    private static final String CURRENT_PAGE       = "/submit-claim";
    private static final String SUCCESS_PAGE       = "/async-submitting";

    private SubmitClaimService submitClaimService;

    @Autowired
    public SubmitClaimController(final SubmitClaimService submitClaimService) {
        this.submitClaimService = submitClaimService;
    }

    /**
     * This allows an easy to submit route, but is only temporary.
     */
    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(final HttpServletRequest request) {
        return postForm(request);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(final HttpServletRequest request) {

        LOG.trace("Starting SubmitClaimController.postForm");
        try {
            LOG.debug("request.getParameterMap() = {}", request.getParameterMap()); // log these jsut in case

            submitClaimService.sendClaim(request);
            LOG.info("Sent claim");

            return "redirect:" + SUCCESS_PAGE;
        } catch(IOException | InstantiationException | ParserConfigurationException | MappingException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw new RuntimeException("Oh no its all gone horribly wrong", e);
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending SubmitClaimController.postForm");
        }
    }
}
