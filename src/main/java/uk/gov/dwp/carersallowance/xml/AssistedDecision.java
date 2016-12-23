package uk.gov.dwp.carersallowance.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static uk.gov.dwp.carersallowance.xml.AssistedDecisionConstants.*;

public class AssistedDecision {
    private static final Logger LOG = LoggerFactory.getLogger(AssistedDecision.class);
    private int oldPersonAge;
    private Map<String, Object> claimvalues;
    private String reason;
    private String decision;

    private static final DateTimeFormatter FORMATTERDDMMYYY = DateTimeFormatter.ofPattern("ddMMyyyy");

    public AssistedDecision(@Value("${age.hide.paydetails}") final int oldPersonAge, Map<String, Object> claimvalues) {
        this.oldPersonAge = oldPersonAge;
        this.claimvalues = claimvalues;
        try {
            if (dateOfClaimOver3MonthsAway() || isTooYoungDecision() || notCaring35HoursDecision() || noQualifyingBenefitDecision()
                    || isAFIPQBDecision() || eeaPaymentsAbroad() || residencyDecision() || isInEducationDecision()
                    || isBlankShowTableDecision() || isTooOldDecision()) {
                // We have created an assisted decision
            } else {
                defaultDecision();
            }
        } catch (AssistedDecisionException e) {
            errorDecision(e.getMessage());
        }
    }

    private boolean dateOfClaimOver3MonthsAway() {
        LocalDate dateOfClaim = getDateFromClaim("dateOfClaim");
        if (dateOfClaim != null && dateOfClaim.isAfter(LocalDate.now().plusMonths(3))) {
            setDecision("CLAIM3MONTHS", "Claim date over 3 months into future.", "Potential disallowance decision,no table");
            return true;
        }
        return false;
    }

    // If carers is 15 years and 9 months then OK. If younger than this then raise decision.
    private boolean isTooYoungDecision() {
        LocalDate carersDob = getDateFromClaim("carerDateOfBirth");
        LocalDate today = LocalDate.now();
        if (carersDob != null && carersDob.plusMonths(FIFTEENYEARSAND9MONTHS).compareTo(today) > 0) {
            setDecision("TOOYOUNG", "Customer does not turn 16 in next 3 months. Send Proforma 491 to customer.", "Potential disallowance decision,no table");
            return true;
        }
        return false;
    }

    private boolean notCaring35HoursDecision() {
        if (getFromClaim("over35HoursAWeek").equals(NOANSWER)) {
            setDecision("35HOURS", "Not caring 35 hours a week.", "Potential disallowance decision,no table");
            return true;
        }
        return false;
    }

    private boolean noQualifyingBenefitDecision() {
        if (getFromClaim("benefitsAnswer").equals("NONE")) {
            setDecision("NO QB", "DP on No QB. Check CIS.", "Potential disallowance decision,show table");
            return true;
        }
        return false;
    }

    private boolean isAFIPQBDecision() {
        if (getFromClaim("benefitsAnswer").equals("AFIP")) {
            setDecision("AFIP QB", "Assign to AFIP officer on CAMLite workflow.", "None,show table");
            return true;
        }
        return false;
    }

    private boolean eeaPaymentsAbroad() {
        if ((getFromClaim("eeaGuardQuestion").equals(YESANSWER) && getFromClaim("benefitsFromEEADetails").equals(YESANSWER)) ||
                (getFromClaim("eeaGuardQuestion").equals(YESANSWER) && getFromClaim("workingForEEADetails").equals(YESANSWER))) {
            setDecision("EEAPAYMENTS", "Assign to Exportability in CAMLite workflow.", "None,show table");
            return true;
        }
        return false;
    }

    private boolean isInEducationDecision() {
        if (getFromClaim("beenInEducationSinceClaimDate").equals(YESANSWER)) {
            setDecision("EDUCATION", "Send DS790/790B COMB to customer.", "None,show table");
            return true;
        }
        return false;
    }

    // Assign to Exportability in CAMLite workflow. (Consider refugee status)
    private boolean residencyDecision() {
        if (getFromClaim("alwaysLivedInUK").equals(NOANSWER) && getFromClaim("liveInUKNow").equals(NOANSWER)) {
            setDecision("RESIDENCY", "Assign to Exportability in CAMLite workflow.", "None,show table");
            return true;
        } else if (getFromClaim("alwaysLivedInUK").equals(NOANSWER) && getFromClaim("liveInUKNow").equals(YESANSWER) && getFromClaim("arrivedInUK").equals("less")) {
            setDecision("RESIDENCY", "Assign to Exportability in CAMLite workflow. (Consider refugee status)", "None,show table");
            return true;
        }
        return false;
    }

    private boolean isTooOldDecision() {
        LocalDate claimDate = getDateFromClaim("dateOfClaim");
        LocalDate carersDob = getDateFromClaim("carerDateOfBirth");
        if (claimDate != null && carersDob != null && carersDob.plusYears(oldPersonAge).compareTo(claimDate) <= 0) {
            setDecision("TOO-OLD", "Check CIS for benefits. Send Pro517 if relevant.", "Potential underlying entitlement,show table");
            return true;
        }
        return false;
    }

    // If we got something unusual such as Trips52weeks, Additional Info, Employed, No Bank details etc.
    private boolean isBlankShowTableDecision() {
        if (getFromClaim("nationality").equals(ANOTHERNATIONALITY) && getFromClaim("actualnationality") != null && getFromClaim("actualnationality").length() > 0) {
            setDecision("ANOTHER-NATIONALITY", "None", "None,show table");
            return true;
        } else if (getFromClaim("trip52Weeks").equals(YESANSWER) && getFromClaim("tripDetails").length() > 0) {
            setDecision("TRIP52WEEKS", "None", "None,show table");
            return true;
        }
        // TODO AFTER BREAKS
        else if (getFromClaim("xxx got breaks").equals(YESANSWER)) {
            setDecision("BREAKS", "None", "None,show table");
            return true;
        } else if (getFromClaim("beenEmployedSince6MonthsBeforeClaim").equals(YESANSWER)) {
            setDecision("EMPLOYED", "None", "None,show table");
            return true;
        } else if (getFromClaim("beenSelfEmployedSince1WeekBeforeClaim").equals(YESANSWER)) {
            setDecision("SELFEMPLOYED", "None", "None,show table");
            return true;
        } else if (getFromClaim("yourIncome_sickpay").equals(YESANSWER)
                || getFromClaim("yourIncome_patmatadoppay").equals(YESANSWER)
                || getFromClaim("yourIncome_fostering").equals(YESANSWER)
                || getFromClaim("yourIncome_directpay").equals(YESANSWER)
                || getFromClaim("yourIncome_rentalincome").equals(YESANSWER)
                || getFromClaim("yourIncome_anyother").equals(YESANSWER)) {
            setDecision("INCOME", "None", "None,show table");
            return true;
        } else if (getFromClaim("likeToPay").equals(NOANSWER)) {
            setDecision("NOBANKDETAILS", "None", "None,show table");
            return true;
        } else if (getFromClaim("anythingElse").equals(YESANSWER) && getFromClaim("anythingElseText").length() > 0) {
            setDecision("ADDITIONALINFO", "None", "None,show table");
            return true;
        }
        return false;
    }

    private boolean defaultDecision() {
        setDecision("DEFAULT", "Check CIS for benefits. Send Pro517 if relevant.", "Potential award,show table");
        return true;
    }

    private boolean errorDecision(String message) {
        setDecision("ERROR", "Error creating assisted decision. " + message, "None,show table");
        return true;
    }

    private LocalDate getDateFromClaim(String datetype) throws AssistedDecisionException {
        LocalDate date = null;
        Integer d = getIntFromClaim(datetype + "_day");
        Integer m = getIntFromClaim(datetype + "_month");
        Integer y = getIntFromClaim(datetype + "_year");
        try {
            if (d != null && m != null && y != null) {
                String datestring = String.format("%02d%02d%04d", d, m, y);
                date = LocalDate.parse(datestring, FORMATTERDDMMYYY);
            }
        } catch (RuntimeException e) {
            LOG.error("Exception formatting date type {}:{}{}{}", datetype, d, m, y, e);
            throw new AssistedDecisionException("Error extracting date from " + datetype);
        }
        return date;
    }

    private void setDecision(String comment, String reason, String decision) {
        LOG.info("{} - Created assisted decision with reason:\"{}\" and decision:\"{}\"", comment, reason, decision);
        this.reason = reason;
        this.decision = decision;
    }

    private String getFromClaim(String key) {
        if (claimvalues.containsKey(key) && claimvalues.get(key).getClass().equals(String.class)) {
            return (String) claimvalues.get(key);
        }
        return "";
    }

    private Integer getIntFromClaim(String key) {
        if (claimvalues.containsKey(key) && claimvalues.get(key).getClass().equals(Integer.class)) {
            return (Integer) claimvalues.get(key);
        } else if (claimvalues.containsKey(key) && claimvalues.get(key).getClass().equals(String.class)) {
            try {
                return Integer.valueOf((String) claimvalues.get(key));
            } catch (NumberFormatException e) {
                LOG.error("Failed to convert claim element:{} to integer", key, e);
            }
        }
        return null;
    }

    public String getReason() {
        return reason;
    }

    public String getDecision() {
        return decision;
    }
}
