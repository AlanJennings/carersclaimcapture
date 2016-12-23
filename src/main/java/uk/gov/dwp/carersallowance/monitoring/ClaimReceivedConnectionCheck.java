package uk.gov.dwp.carersallowance.monitoring;

import gov.dwp.carers.CADSHealthCheck;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

/**
 * Created by peterwhitehead on 28/06/2016.
 */
@Component
public class ClaimReceivedConnectionCheck extends CADSHealthCheck {

    private final String crUrl;
    private final RestTemplate restTemplate;

    @Inject
    public ClaimReceivedConnectionCheck(final @Value("${application.name}") String applicationName,
                                        final @Value("${application.version}") String applicationVersion,
                                        final @Value("${cr.url}") String crUrl,
                                        final RestTemplate restTemplate) {
        super(applicationName, applicationVersion.replace("-SNAPSHOT", ""), "-connection-cr");
        this.crUrl = crUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    protected CADSHealthCheck.Result check() {
        CADSHealthCheck.Result rtnMsg;
        try {
            final String submissionServerEndpoint = crUrl + "/ping";
            final ResponseEntity<String> response = restTemplate.getForEntity(submissionServerEndpoint, String.class);
            if (response.getStatusCode().value() == HttpStatus.SC_OK) {
                rtnMsg = CADSHealthCheck.Result.healthy();
            } else {
                rtnMsg = Result.unhealthy("Claim received ping failed: " + response.getStatusCode().value() + ".");
            }
        } catch (Exception e) {
            rtnMsg = Result.unhealthy(e);
        }
        return rtnMsg;
    }
}
