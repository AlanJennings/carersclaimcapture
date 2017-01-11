package uk.gov.dwp.carersallowance.xml;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.carersallowance.utils.C3Constants;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.carersallowance.xml.AssistedDecisionConstants.*;

public class AssistedDecisionAgeTest {
    private Map<String, Object> claimvalues;


    @Before
    public void setUp() throws Exception {
        claimvalues = new HashMap();
    }

    // "Default AD if customer EQUALS 15 and 9 year old today"
    @Test
    public void carerUnder15Years9MonthTest() {
        LocalDate dob = LocalDate.now().minusMonths(FIFTEENYEARSAND9MONTHS);
        setDateInClaim("carerDateOfBirth", dob);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }


    // "Create 'disallowance' AD if customer LESS THAN 15 and 9 year old today i.e. born 1 day later"
    @Test
    public void carerLessThan15Years9MonthsByOneDay() {
        LocalDate dob = LocalDate.now().minusMonths(FIFTEENYEARSAND9MONTHS).plusDays(1);
        setDateInClaim("carerDateOfBirth", dob);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(CARERSTOOYOUNGREASON));
        assertThat(assistedDecision.getDecision(), is(POTENTIALDISALLOWDECISION));
    }

    // "Default AD if customer OLDER THAN 15 and 9 year old today i.e. i.e. born 1 day earlier"
    @Test
    public void carerExactly15Years9Months() {
        LocalDate dob = LocalDate.now().minusMonths(FIFTEENYEARSAND9MONTHS).minusDays(1);
        setDateInClaim("carerDateOfBirth", dob);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }

    // "Create 'show table' AD if no bank details provided"
    @Test
    public void noBankDetails() {
        claimvalues.put("likeToPay", C3Constants.NO);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    //"Create 'underlying entitlement' AD if customer EQUALS 65 today and claim date=today"
    @Test
    public void carerOver65ClaimingToday() {
        setDateInClaim("carerDateOfBirth", 1, 12, 1945);
        setDateInClaim("dateOfClaim", 1, 12, 2010);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(OLDPERSONREASON));
        assertThat(assistedDecision.getDecision(), is(OLDPERSONDECISION));
    }

    // "Default AD if customer 65 tomorrow and claim date=today with bank details"
    @Test
    public void carer65TomorrowAndClaimingToday() {
        setDateInClaim("carerDateOfBirth", 2, 12, 1945);
        setDateInClaim("dateOfClaim", 1, 12, 2010);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }

    // "Create 'show table' AD if customer 65 tomorrow and claim date=today without bank details"
    // This scala test is redundant since bank detals are not used in calculating Assisted Decision.

    // "Create 'underlying entitlement' AD if customer 65 yesterday claim date=today"
    @Test
    public void carer65YesterdayClaimingToday() {
        setDateInClaim("carerDateOfBirth", 30, 11, 1945);
        setDateInClaim("dateOfClaim", 1, 12, 2010);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(OLDPERSONREASON));
        assertThat(assistedDecision.getDecision(), is(OLDPERSONDECISION));
    }

    // "Create 'underlying entitlement' AD if customer is 65 tomorrow but OLDER when claim date is next week"
    @Test
    public void carer65TomorrowClaimingNextWeek() {
        setDateInClaim("carerDateOfBirth", 2, 12, 1945);
        setDateInClaim("dateOfClaim", 10, 12, 2010);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(OLDPERSONREASON));
        assertThat(assistedDecision.getDecision(), is(OLDPERSONDECISION));
    }

    // "Happy path Default AD if customer is in the middle say 30 years old"
    @Test
    public void carerOnly30() {
        setDateInClaim("carerDateOfBirth", 1, 12, 1980);
        setDateInClaim("dateOfClaim", 1, 12, 2010);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }

    private void setDateInClaim(String datetype, LocalDate date) {
        claimvalues.put(datetype + "_day", date.getDayOfMonth());
        claimvalues.put(datetype + "_month", date.getMonthValue());
        claimvalues.put(datetype + "_year", date.getYear());
    }

    private void setDateInClaim(String datetype, Integer d, Integer m, Integer y) {
        claimvalues.put(datetype + "_day", d);
        claimvalues.put(datetype + "_month", m);
        claimvalues.put(datetype + "_year", y);
    }
}