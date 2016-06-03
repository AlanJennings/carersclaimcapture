(function() {
  var daysInMonth, hideCareStartDateWrap, initDateWarningOnChange, isLeapYear, isLegalDate, showCareStartDateWrap;
/*
  window.initEvents = function(spent35HoursCaringBeforeClaimY, spent35HoursCaringBeforeClaimN, day, month, year) {
    if (!$("#" + spent35HoursCaringBeforeClaimY).prop('checked')) {
      hideCareStartDateWrap(day, month, year);
    }
    $("#" + spent35HoursCaringBeforeClaimY).on("click", function() {
      return showCareStartDateWrap();
    });
    return $("#" + spent35HoursCaringBeforeClaimN).on("click", function() {
      return hideCareStartDateWrap(day, month, year);
    });
  };

  showCareStartDateWrap = function() {
    return $("#careStartDateWrap").slideDown(0).attr('aria-hidden', 'false');
  };

  hideCareStartDateWrap = function(day, month, year) {
    $("#careStartDateWrap").slideUp(0).attr('aria-hidden', 'true');
    $("#" + day).val("");
    $("#" + month).val("");
    return $("#" + year).val("");
  };
*/
  window.initDateWarning = function(warningId, day, month, year, text, testMode) {
    $("#" + day).on("change keyup", function() {
      return initDateWarningOnChange(warningId, day, month, year, text, testMode);
    });
    $("#" + month).on("change keyup", function() {
      return initDateWarningOnChange(warningId, day, month, year, text, testMode);
    });
    $("#" + year).on("change keyup", function() {
      return initDateWarningOnChange(warningId, day, month, year, text, testMode);
    });
    return initDateWarningOnChange(warningId, day, month, year, text, testMode);
  };

  initDateWarningOnChange = function(warningId, day, month, year, text, testMode) {
    var dayV, futureDate, monthV, showWarningMsg, yearV;
    dayV = $("#" + day).val();
    monthV = $("#" + month).val();
    yearV = $("#" + year).val();
    showWarningMsg = false;
    if (dayV.length > 0 && monthV.length > 0 && yearV.length > 0) {
      futureDate = new Date(yearV, monthV - 1, dayV);
      if (isLegalDate(dayV, monthV, yearV) && Date.today().add(3).months().getTime() <= futureDate.getTime()) {
        showWarningMsg = true;
      }
    }
    if (showWarningMsg) {
      $("#" + warningId).slideDown(0);
      if (!testMode) {
        return trackEvent('/your-claim-date/claim-date', 'Error', text);
      }
    } else {
      return $("#" + warningId).slideUp(0);
    }
  };

  isLegalDate = function(day, month, year) {
    if (month > 12) {
      return false;
    } else if (day > daysInMonth(year, month)) {
      return false;
    } else {
      return true;
    }
  };

  isLeapYear = function(year) {
    return ((year % 4 === 0) && (year % 100 !== 0)) || (year % 400 === 0);
  };

  daysInMonth = function(year, month) {
    if (isLeapYear(year)) {
      return [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month - 1];
    } else {
      return [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month - 1];
    }
  };

}).call(this);

//# sourceMappingURL=g_claimDate.js.map