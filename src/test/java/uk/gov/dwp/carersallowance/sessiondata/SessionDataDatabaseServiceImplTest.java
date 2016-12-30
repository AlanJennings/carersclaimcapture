package uk.gov.dwp.carersallowance.sessiondata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.carersallowance.session.NoSessionException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


/**
 * Created by peterwhitehead on 30/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionDataDatabaseServiceImplTest {
    private SessionDataDatabaseServiceImpl sessionDataDatabaseServiceImpl;

    @Mock
    private RestTemplate restTemplate;

    private final String sdURL = "http://localhost:9015";

    @Before
    public void setUp() throws Exception {
        sessionDataDatabaseServiceImpl = new SessionDataDatabaseServiceImpl(sdURL, "88a978e1-e927-4bb4-6722-18cdbc6d0516", 90, restTemplate);
    }

    @Test(expected = NoSessionException.class)
    public void testGetSessionDataNoData() throws Exception {
        final ResponseEntity<SessionData> response = new ResponseEntity(new SessionData(), HttpStatus.OK);
        when(restTemplate.exchange(anyString(), Mockito.<HttpMethod> any(), Mockito.<HttpEntity<String>> any(), Mockito.<Class<SessionData>> any())).thenReturn(response);
        sessionDataDatabaseServiceImpl.getSessionData("8a20d772-b998-486d-ba05-fd3ef75d4fd2");
    }

    @Test
    public void testCreateSessionData() throws Exception {

    }

    @Test
    public void testSaveSessionData() throws Exception {

    }

    @Test
    public void testRemoveSessionData() throws Exception {

    }
}