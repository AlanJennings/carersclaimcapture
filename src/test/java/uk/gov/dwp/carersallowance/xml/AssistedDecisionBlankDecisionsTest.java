package uk.gov.dwp.carersallowance.xml;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.carersallowance.xml.AssistedDecisionConstants.*;

public class AssistedDecisionBlankDecisionsTest {
    private Map<String, Object> claimvalues;

    @Before
    public void setUp() throws Exception {
        claimvalues = new HashMap();
        claimvalues.put("benefitsAnswer", QBPIP);
    }

    // "Check CIS decision when all empty (happy path)"
    @Test
    public void happyPathTest() {
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(DEFAULTCHECKCISREASON));
        assertThat(assistedDecision.getDecision(), is(DEFAULTCHECKCISDECISION));
    }

    // "Show table decision when another nationality"
    @Test
    public void anotherNationalityShowTable() {
        claimvalues.put("nationality", ANOTHERNATIONALITY);
        claimvalues.put("actualnationality", "French");

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Show table decision when got trips abroad greater than 52 weeks"
    @Test
    public void tripsAbroadMoreThan52WeeksShowTable() {
        claimvalues.put("trip52Weeks", YESANSWER);
        claimvalues.put("tripDetails", "Went away for 2 years");

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // TODO BREAKS
    // "Show table decision when got breaks"
    @Test
    public void gotBreaksShowTable() {
        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
//        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
//        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }


    // "Show table decision when got self employed"
    @Test
    public void selfEmployedShowTable() {
        claimvalues.put("beenSelfEmployedSince1WeekBeforeClaim", YESANSWER);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Show table decision when got employed"
    @Test
    public void employedShowTable() {
        claimvalues.put("beenEmployedSince6MonthsBeforeClaim", YESANSWER);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Show table decision when got SSP Sickpay Income"
    @Test
    public void incomeSSPShowTable() {
        claimvalues.put("yourIncome_sickpay", YESANSWER);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Show table decision when got Paternity Maternity or Adoption Income"
    @Test
    public void incomePatMatAdoptionShowTable() {
        claimvalues.put("yourIncome_patmatadoppay", YESANSWER);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Show table decision when got Fostering Income"
    @Test
    public void incomeFosteringShowTable() {
        claimvalues.put("yourIncome_fostering", YESANSWER);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Show table decision when got Direct Payment Income"
    @Test
    public void incomeDirectPayShowTable() {
        claimvalues.put("yourIncome_directpay", YESANSWER);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Show table decision when got Rental Income"
    @Test
    public void incomeRentalShowTable() {
        claimvalues.put("yourIncome_rentalincome", YESANSWER);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Show table decision when got Any Other Income"
    @Test
    public void incomeAnyOtherShowTable() {
        claimvalues.put("yourIncome_anyother", YESANSWER);

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }

    // "Show table decision when got additional information"
    @Test
    public void gotAdditionalInfoShowTable() {
        claimvalues.put("anythingElse", YESANSWER);
        claimvalues.put("anythingElseText", "Some more info about my claim");

        AssistedDecision assistedDecision = new AssistedDecision(65, claimvalues);
        assertThat(assistedDecision.getReason(), is(NONESHOWTABLEREASON));
        assertThat(assistedDecision.getDecision(), is(NONESHOWTABLEDECISION));
    }
}