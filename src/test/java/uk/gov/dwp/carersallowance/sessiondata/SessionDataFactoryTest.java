package uk.gov.dwp.carersallowance.sessiondata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by peterwhitehead on 30/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionDataFactoryTest {
    private SessionDataFactory sessionDataFactory;

    @Mock
    private SessionDataDatabaseServiceImpl sessionDataDatabaseService;

    @Mock
    private SessionDataMapServiceImpl sessionDataMapService;

    @Test
    public void testGetSessionDataService() throws Exception {
        sessionDataFactory = new SessionDataFactory(false, sessionDataDatabaseService, sessionDataMapService);
        assertThat(sessionDataFactory.getSessionDataService(), is(sessionDataMapService));
    }

    @Test
    public void testGetSessionDataServiceDB() throws Exception {
        sessionDataFactory = new SessionDataFactory(true, sessionDataDatabaseService, sessionDataMapService);
        assertThat(sessionDataFactory.getSessionDataService(), is(sessionDataDatabaseService));
    }
}