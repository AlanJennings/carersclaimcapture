package uk.gov.dwp.carersallowance.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to submit the overall claim
 * It does not expect any request parameters, and does not validate anything
 * It just creates the claim XML and sends it.  If is successful, then it redirects to a success page
 * otherwise it redirects to a retry page.  We may do a waiting page, as we currently do. (TODO)
 */
public class IndexPageController {
    /**
     * This allows an easy to submit route, but is only temporary.
     */
    @RequestMapping(value="/index")
    public String showForm(HttpServletRequest request) {
        return "/index";
    }
}
