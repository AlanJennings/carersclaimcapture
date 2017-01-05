package uk.gov.dwp.carersallowance.submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.carersallowance.database.Status;
import uk.gov.dwp.carersallowance.database.TransactionIdService;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

@Component
public class SubmitClaimServiceImpl implements SubmitClaimService {
    private static final Logger LOG = LoggerFactory.getLogger(SubmitClaimServiceImpl.class);

    private final RestTemplate restTemplate;
    private final String crUrl;
    private final TransactionIdService transactionIdService;

    @Inject
    public SubmitClaimServiceImpl(final RestTemplate restTemplate,
                                  @Value("${cr.url}") final String crUrl,
                                  final TransactionIdService transactionIdService) {
        this.restTemplate = restTemplate;
        this.crUrl = crUrl;
        this.transactionIdService = transactionIdService;
    }

    @Override
    @Async
    public void sendClaim(final String xml, final String transactionId) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            final MediaType mediaTypeXml = new MediaType("application", "xml", StandardCharsets.UTF_8);
            headers.setContentType(mediaTypeXml);
            final HttpEntity<String> request = new HttpEntity<>(xml, headers);
            LOG.info("Posting claim to :{}", crUrl);
            transactionIdService.setTransactionStatusById(transactionId, Status.SUBMITTED.getStatus());
            final ResponseEntity<String> response = restTemplate.exchange(crUrl + "/submission", HttpMethod.POST, request, String.class);
            LOG.debug("RESPONSE:{}", response.getStatusCode());
            //TODO Handle response from submit to claim received
            processResponse(response, transactionId);
        } catch (RestClientException rce) {
            LOG.error("sendClaim error:", rce);
        } catch (Exception e) {
            LOG.error("sendClaim error:", e);
        }
    }

    private void processResponse(final ResponseEntity<String> response, final String transactionId) {
        if (response.getStatusCode() == HttpStatus.OK) {
            transactionIdService.setTransactionStatusById(transactionId, Status.SUCCESS.getStatus());
        } else {
            transactionIdService.setTransactionStatusById(transactionId, Status.SERVICE_UNAVAILABLE.getStatus());
        }
    }
}
