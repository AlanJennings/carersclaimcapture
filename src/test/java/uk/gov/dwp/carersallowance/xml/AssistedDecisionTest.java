package uk.gov.dwp.carersallowance.xml;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static uk.gov.dwp.carersallowance.xml.AssistedDecisionConstants.*;

public class AssistedDecisionTest {
    private Map<String, Object> claimvalues;

    @Before
    public void setUp() throws Exception {
        claimvalues = new HashMap();
    }

    //  "Create an assisted decision section if date of claim > 3 months"
    @Test
    public void dateOfClaimGreaterThan3MonthsAwayTest() {
        LocalDateTime date3and1fromNow = LocalDateTime.now(ZoneId.systemDefault()).plusMonths(3).plusDays(1);
        claimvalues.put("dateOfClaim_day", String.valueOf(date3and1fromNow.getDayOfMonth()));
        claimvalues.put("dateOfClaim_month", String.valueOf(date3and1fromNow.getMonthValue()));
        claimvalues.put("dateOfClaim_year", String.valueOf(date3and1fromNow.getYear()));

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is("Claim date over 3 months into future."));
        assertThat(assistedDecision.getDecision(), is("Potential disallowance decision,no table"));
    }

    // "Default assisted decision if date of claim <= 3 month"
    @Test
    public void dateOfClaimExactly3MonthsAwayTest() {
        LocalDateTime date3and1fromNow = LocalDateTime.now(ZoneId.systemDefault()).plusMonths(3);
        claimvalues.put("dateOfClaim_day", String.valueOf(date3and1fromNow.getDayOfMonth()));
        claimvalues.put("dateOfClaim_month", String.valueOf(date3and1fromNow.getMonthValue()));
        claimvalues.put("dateOfClaim_year", String.valueOf(date3and1fromNow.getYear()));

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }

    // "Create an assisted decision section if care less than 35 hours"
    @Test
    public void careLessThan35HoursTest() {
        claimvalues.put("over35HoursAWeek", NOANSWER);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is("Not caring 35 hours a week."));
        assertThat(assistedDecision.getDecision(), is("Potential disallowance decision,no table"));
    }

    // "Default assisted decision if care more than 35 hours"
    @Test
    public void careMoreThan35HoursTest() {
        claimvalues.put("over35HoursAWeek", YESANSWER);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }

    // "Create an assisted decision section if no benefits ( no QB )"
    @Test
    public void noEEAandNoQBTest() {
        final String QBNONE = "NONE";
        claimvalues.put("benefitsAnswer", QBNONE);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is("DP on No QB. Check CIS."));
        assertThat(assistedDecision.getDecision(), is("Potential disallowance decision,show table"));
    }

    // "Create an assisted decision section if no EEA and AFIP"
    @Test
    public void noEEAandAFIPTest() {
        claimvalues.put("benefitsAnswer", QBAFIP);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(ASSIGNTOAFIPREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }


    @Test
    public void EeaAndHadBenefitsPaymentsTest() {
        claimvalues.put("eeaGuardQuestion", YESANSWER);
        claimvalues.put("benefitsFromEEADetails", YESANSWER);
        claimvalues.put("workingForEEADetails", NOANSWER);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(EXPORTABILITYREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    @Test
    public void EeaAndHadWorkPaymentsTest() {
        claimvalues.put("eeaGuardQuestion", YESANSWER);
        claimvalues.put("benefitsFromEEADetails", NOANSWER);
        claimvalues.put("workingForEEADetails", YESANSWER);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(EXPORTABILITYREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Default assisted decision if no EEA insurance or working"
    @Test
    public void neitherEeaInsuranceOrWorkingTest() {
        claimvalues.put("eeaGuardQuestion", YESANSWER);
        claimvalues.put("benefitsFromEEADetails", NOANSWER);
        claimvalues.put("workingForEEADetails", NOANSWER);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }

    // "Create an assisted decision section if no EEA and in education"
    @Test
    public void notEEAandInEducationTest() {
        claimvalues.put("eeaGuardQuestion", NOANSWER);
        claimvalues.put("beenInEducationSinceClaimDate", YESANSWER);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(EDUCATIONDS790REASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Create an assisted decision section if no EEA but yes to EEA guard question and in education"
    @Test
    public void notEEAInEducationTest() {
        claimvalues.put("eeaGuardQuestion", YESANSWER);
        claimvalues.put("benefitsFromEEADetails", NOANSWER);
        claimvalues.put("workingForEEADetails", NOANSWER);
        claimvalues.put("beenInEducationSinceClaimDate", YESANSWER);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(EDUCATIONDS790REASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Default assisted decision if no EEA and not in education"
    @Test
    public void notEEAandNotEducationTest() {
        claimvalues.put("eeaGuardQuestion", NOANSWER);
        claimvalues.put("beenInEducationSinceClaimDate", NOANSWER);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }

    // "never lived in UK then create an exportability assisted decision section "
    @Test
    public void neverLivedInUKTest() {
        claimvalues.put("alwaysLivedInUK", NOANSWER);
        claimvalues.put("liveInUKNow", NOANSWER);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is("Assign to Exportability in CAMLite workflow."));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "lived in UK less than 3 years then create an exportability assisted decision section "
    @Test
    public void livedInUKlessThan3Years() {
        claimvalues.put("alwaysLivedInUK", NOANSWER);
        claimvalues.put("liveInUKNow", YESANSWER);
        claimvalues.put("arrivedInUK", "less");
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is("Assign to Exportability in CAMLite workflow."));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Lived in UK more than 3 years no workflow so drops through to default"
    @Test
    public void livedInUKMoreThan3YearsTest() {
        claimvalues.put("alwaysLivedInUK", NOANSWER);
        claimvalues.put("liveInUKNow", YESANSWER);
        claimvalues.put("arrivedInUK", "more");
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }

    // "Simplest claim (happy path no income breaks addinfo etc.) creates CIS decision"
    @Test
    public void happyPathTest() {
        claimvalues.put("benefitsAnswer", QBPIP);
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }
}