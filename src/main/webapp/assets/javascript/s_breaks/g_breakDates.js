(function() {
  var dateOnChange, getDate, hideHasBreakEndedWrap, hideTime, isNotMondayOrFriday, showHasBreakEndedWrap, showTime;

  window.initEvents = function(breakStartDate, breakEndedDate, breakStartTime, breakEndedTime, hasBreakEndedYes, hasBreakEndedNo, day, month, year) {
    if (isNotMondayOrFriday(breakStartDate)) {
      hideTime(breakStartTime);
    }
    if (isNotMondayOrFriday(breakEndedDate)) {
      hideTime(breakEndedTime);
    }
    dateOnChange(breakStartDate, function(id) {
      var isIt;
      isIt = isNotMondayOrFriday(id);
      if (isIt) {
        hideTime(breakStartTime);
      }
      if (!isIt) {
        return showTime(breakStartTime);
      }
    });
    dateOnChange(breakEndedDate, function(id) {
      var isIt;
      isIt = isNotMondayOrFriday(id);
      if (isIt) {
        hideTime(breakEndedTime);
      }
      if (!isIt) {
        return showTime(breakEndedTime);
      }
    });
    if (!$("#" + hasBreakEndedYes).prop('checked')) {
      hideHasBreakEndedWrap(day, month, year);
    }
    $("#" + hasBreakEndedYes).on("click", function() {
      return showHasBreakEndedWrap();
    });
    return $("#" + hasBreakEndedNo).on("click", function() {
      return hideHasBreakEndedWrap(day, month, year);
    });
  };

  isNotMondayOrFriday = function(id) {
    var date;
    date = getDate(id);
    return !(date !== void 0 && (date.getDay() === 1 || date.getDay() === 5));
  };

  getDate = function(id) {
    var d, date, m, values, y;
    values = $("#" + id + " li input").map(function() {
      return $(this).val();
    });
    m = parseInt(values[1], 10);
    d = parseInt(values[0], 10);
    y = parseInt(values[2], 10);
    date = new Date(y, m - 1, d);
    if (date.getFullYear() === y && date.getMonth() + 1 === m && date.getDate() === d) {
      return date;
    } else {
      return void 0;
    }
  };

  hideTime = function(id) {
    $("#" + id).val("");
    return $("#" + id).parent("li").slideUp(0).attr('aria-hidden', 'true');
  };

  showTime = function(id) {
    return $("#" + id).parent("li").slideDown(0).attr('aria-hidden', 'false');
  };

  dateOnChange = function(id, f) {
    return $("#" + id + " li input").on("change paste keyup", function() {
      return f(id);
    });
  };

  showHasBreakEndedWrap = function() {
    return $("#hasBreakEndedWrap").slideDown(0).attr('aria-hidden', 'false');
  };

  hideHasBreakEndedWrap = function(day, month, year) {
    $("#hasBreakEndedWrap").slideUp(0).attr('aria-hidden', 'true');
    $("#" + day).val("");
    $("#" + month).val("");
    return $("#" + year).val("");
  };

}).call(this);

//# sourceMappingURL=g_breakDates.js.map
