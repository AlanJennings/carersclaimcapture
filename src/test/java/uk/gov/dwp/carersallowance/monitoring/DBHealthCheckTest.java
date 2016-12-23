package uk.gov.dwp.carersallowance.monitoring;

import gov.dwp.carers.CADSHealthCheck;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.dwp.carersallowance.database.DatabaseService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by peterwhitehead on 07/07/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class DBHealthCheckTest {

    private DBHealthCheck dbHealthCheck;

    @Mock
    private DatabaseService databaseService;

    @Before
    public void setUp() throws Exception {
        dbHealthCheck = new DBHealthCheck("c3", "4.00", databaseService);
    }

    @Test
    public void testCheck() throws Exception {
        whenDatabaseHealthCalledReturn();
        thenCheckShouldReturn(CADSHealthCheck.Result.healthy());
    }

    private void whenDatabaseHealthCalledReturn() {
        when(databaseService.health()).thenReturn(true);
    }

    private void thenCheckShouldReturn(final CADSHealthCheck.Result result) {
        assertThat("health check matches expected result", dbHealthCheck.check(), is(result));
    }
}