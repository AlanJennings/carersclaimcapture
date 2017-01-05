package uk.gov.dwp.carersallowance.submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@Component
public class SubmitClaimServiceImpl implements SubmitClaimService {
    private static final Logger LOG = LoggerFactory.getLogger(SubmitClaimServiceImpl.class);

    private final RestTemplate restTemplate;
    private final String crUrl;

    @Inject
    public SubmitClaimServiceImpl(final RestTemplate restTemplate,
                                  @Value("${cr.url}") final String crUrl) {
        this.restTemplate = restTemplate;
        this.crUrl = crUrl;
    }

    @Override
    public void sendClaim(final String xml) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            final MediaType mediaTypeXml = new MediaType("application", "xml", StandardCharsets.UTF_8);
            headers.setContentType(mediaTypeXml);
            final HttpEntity<String> request = new HttpEntity<>(xml, headers);
            LOG.info("Posting claim to :"+crUrl);
            final ResponseEntity<String> response = restTemplate.exchange(crUrl + "/submission", HttpMethod.POST, request, String.class);
            LOG.debug("RESPONSE:{}", response.getStatusCode());
            //TODO Handle response from submit to claim received
        } catch (RestClientException rce) {
            LOG.error("sendClaim error:", rce);
        } catch (Exception e) {
            LOG.error("sendClaim error:", e);
        }
    }
}
