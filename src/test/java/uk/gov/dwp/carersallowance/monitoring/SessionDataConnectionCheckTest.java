package uk.gov.dwp.carersallowance.monitoring;

import gov.dwp.carers.CADSHealthCheck;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by peterwhitehead on 23/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionDataConnectionCheckTest {
    private SessionDataConnectionCheck sessionDataConnectionCheck;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        sessionDataConnectionCheck = new SessionDataConnectionCheck("c3", "4.00", "http:/localhost:9015", restTemplate);
    }

    @Test
    public void testCheck() throws Exception {
        whenRestTemplateCalledReturn(HttpStatus.OK);
        thenCheckShouldReturn(CADSHealthCheck.Result.healthy());
    }

    @Test
    public void testCheckUnhealthy() throws Exception {
        whenRestTemplateCalledReturn(HttpStatus.SERVICE_UNAVAILABLE);
        thenCheckShouldReturn(CADSHealthCheck.Result.unhealthy("Session data ping failed: 503."));
    }

    @Test
    public void testCheckUnhealthyException() throws Exception {
        whenRestTemplateThrowsException();
        thenCheckShouldReturn(CADSHealthCheck.Result.unhealthy(new RestClientException("test")).toString());
    }

    private void whenRestTemplateCalledReturn(final HttpStatus status) {
        final ResponseEntity<String> response = new ResponseEntity("", status);
        when(restTemplate.getForEntity(anyString(), Mockito.<Class<String>>any())).thenReturn(response);
    }

    private void whenRestTemplateThrowsException() {
        when(restTemplate.getForEntity(anyString(), Mockito.<Class<String>>any())).thenThrow(new RestClientException("test"));
    }

    private void thenCheckShouldReturn(final CADSHealthCheck.Result result) {
        assertThat("connection check matches expected result", sessionDataConnectionCheck.check(), is(result));
    }

    private void thenCheckShouldReturn(final String result) {
        assertThat("connection check matches expected result", sessionDataConnectionCheck.check().toString(), is(result));
    }
}