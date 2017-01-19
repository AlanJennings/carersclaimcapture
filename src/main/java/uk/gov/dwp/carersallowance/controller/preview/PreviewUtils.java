package uk.gov.dwp.carersallowance.controller.preview;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.utils.C3Constants;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by peterwhitehead on 18/01/2017.
 */
@Component
public class PreviewUtils {
    private final MessageSource messageSource;

    @Inject
    public PreviewUtils(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String addThirdPartyMessage(final Session session) {
        if (C3Constants.NO.equals(session.getAttribute("thirdParty"))) {
            return mergeStrings(" ", (String)session.getAttribute("nameAndOrganisation"),
                    getMessage("preview.thirdParty.onBehalfOf"),
                    (String)session.getAttribute("carerFirstName"), (String)session.getAttribute("carerSurname"));
        }
        return mergeStrings(" ", (String)session.getAttribute("carerFirstName"), (String)session.getAttribute("carerSurname"));
    }

    public Boolean checkDateDifference(final String before, final String after, final Session session, final Integer check) {
        LocalDate dateBefore = getDate(before, session);
        LocalDate dateAfter = getDate(after, session);
        if (dateBefore != null && dateAfter != null) {
            long years = ChronoUnit.YEARS.between(dateBefore, dateAfter);
            return years < check;
        }
        return false;
    }

    public LocalDate getDate(final String date, final Session session) {
        final String day = (String)session.getAttribute(date + "_day");
        final String month = (String)session.getAttribute(date + "_month");
        final String year = (String)session.getAttribute(date + "_year");
        if (StringUtils.isNotEmpty(day) && StringUtils.isNotEmpty(month) && StringUtils.isNotEmpty(year)) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(year + month + day, formatter);
        }
        return null;
    }

    public String breakTypeGiven(final String value) {
        if (StringUtils.isEmpty(value)) {
            getMessage(C3Constants.NO);
        }
        return getDetailsMessage(C3Constants.YES);
    }

    public String anyBreakTypeGiven(final String... value) {
        if (ArrayUtils.isEmpty(value)) {
            getMessage(C3Constants.NO);
        }
        return getMessage(C3Constants.YES);
    }

    public Boolean checkYes(final String checkValue) {
        return C3Constants.YES.equals(checkValue);
    }

    public Boolean checkNo(final String checkValue) {
        return C3Constants.NO.equals(checkValue);
    }

    public String getDetailsMessage(final String value) {
        if (C3Constants.YES.equals(value)) {
            return mergeStrings(" - ", getMessage(value), getMessage("preview.detailsProvided.simple"));
        }
        return getMessage(value);
    }

    public String getDetailsNotProvidedMessage(final String value) {
        if (StringUtils.isEmpty(value)) {
            return getMessage("preview.detailsNotProvided");
        }
        return value;
    }

    public String firstElseSecond(final String first, final String second) {
        if (StringUtils.isNotEmpty(first)) {
            return first;
        }
        return second;
    }

    public void setDateIntoModel(final String date, final Session session, final Model model) {
        model.addAttribute(date + "_day", session.getAttribute(date + "_day"));
        model.addAttribute(date + "_month", session.getAttribute(date + "_month"));
        model.addAttribute(date + "_year", session.getAttribute(date + "_year"));
    }

    public String getEmailLabel(final Boolean saveForLaterSaveEnabled) {
        if (saveForLaterSaveEnabled) {
            return "carerWantsEmailContactNew.label";
        }
        return "carerWantsEmailContact.label";
    }

    public String getLink(final String indexIntoList) {
        return "/preview/redirect/" + indexIntoList;
    }

    public String getAddressIfNo(final String addCheckValue, final String startIndex, final Session session) {
        final String checkValue = (String)session.getAttribute(addCheckValue);
        if (C3Constants.NO.equals(checkValue)) {
            return getMessage(checkValue) + "<br/><br/>" + getAddressWithPostcode(startIndex, session);
        }
        return getMessage(checkValue);
    }

    public String getAddressWithPostcode(final String startIndex, final Session session) {
        final String lineOne = (String)session.getAttribute(startIndex + "AddressLineOne");
        final String lineTwo = (String)session.getAttribute(startIndex + "AddressLineTwo");
        final String lineThree = (String)session.getAttribute(startIndex + "AddressLineThree");
        final String postcode = (String)session.getAttribute(startIndex + "Postcode");
        String address = mergeStrings("<br/>", lineOne, lineTwo, lineThree, postcode);
        return address;
    }

    public String getFullName(final String startIndex, final Session session) {
        final String title = (String)session.getAttribute(startIndex + "Title");
        final String firstName = (String)session.getAttribute(startIndex + "FirstName");
        final String middleName = (String)session.getAttribute(startIndex + "MiddleName");
        final String surname = (String)session.getAttribute(startIndex + "Surname");
        final String fullName = mergeStrings(" ", title, firstName, middleName, surname);
        return fullName;
    }

    public String mergeStrings(final String delimiter, final String... values) {
        String merged = Arrays.asList(values).stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(delimiter));
        return merged;
    }

    public String notGivenIfEmpty(final String value, final String withExtraText) {
        if (StringUtils.isEmpty(value)) {
            return mergeStrings(" - ", getMessage("preview.notgiven"), getMessage(withExtraText));
        }
        return value;
    }

    public String notGivenIfEmptyElseDetailsProvided(final String value, final String withExtraText) {
        final String notGiven = notGivenIfEmpty(value, withExtraText);
        if (StringUtils.isNotEmpty(value)) {
            return getDetailsMessage(C3Constants.YES);
        }
        return notGiven;
    }

    public String getMessage(final String code) {
        if (code == null) {
            return code;
        }
        return messageSource.getMessage(code, null, null, Locale.getDefault());
    }

    public String getOnlyDetailsMessage(final String value) {
        if (StringUtils.isNotEmpty(value)) {
            return getMessage("preview.detailsProvided.simple");
        }
        return "";
    }
}
