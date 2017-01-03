package uk.gov.dwp.carersallowance.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.gov.dwp.carersallowance.controller.started.C3Application;
import uk.gov.dwp.carersallowance.database.TransactionIdService;

import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@ActiveProfiles({ "testpostgres" })
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:test.application.properties")
@SpringBootTest(classes = { C3Application.class }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationTransactionIdTest {
    @Inject
    private TransactionIdService transactionIdService;

    @Test
    public void testGetTransactionId() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        final String date = simpleDateFormat.format(new Date());
        assertThat(transactionIdService.getTransactionId(), is(date.substring(2,4) + "010000001"));
    }
}