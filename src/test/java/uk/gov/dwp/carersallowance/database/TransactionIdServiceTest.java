package uk.gov.dwp.carersallowance.database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionIdServiceTest {
    private TransactionIdService transactionIdService;

    @Mock
    private DatabaseService databaseService;

    private final static String ORIGIN_TAG = "GB";

    @Before
    public void setUp() throws Exception {
        transactionIdService = new TransactionIdService(databaseService, ORIGIN_TAG);
    }

    @Test
    public void testGetTransactionId() throws Exception {
        when(databaseService.getTransactionId(ORIGIN_TAG)).thenReturn("16100034567");
        assertThat(transactionIdService.getTransactionId(), is("16100034567"));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testGetTransactionIdFails() throws Exception {
        when(databaseService.getTransactionId(ORIGIN_TAG)).thenThrow(DataIntegrityViolationException.class);
        transactionIdService.getTransactionId();
    }
}