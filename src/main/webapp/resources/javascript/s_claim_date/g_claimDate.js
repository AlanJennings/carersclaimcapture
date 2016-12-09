(function() {
  var daysInMonth, hideCareStartDateWrap, initDateWarningOnChange, isLeapYear, isLegalDate, showCareStartDateWrap;
  
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
    var dayV = $("#" + day).val();
    var monthV = $("#" + month).val();
    var yearV = $("#" + year).val();
    var showWarningMsg = false;
    
    if (dayV.length > 0 && monthV.length > 0 && yearV.length > 0) {
      var futureDate = new Date(yearV, monthV - 1, dayV);
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