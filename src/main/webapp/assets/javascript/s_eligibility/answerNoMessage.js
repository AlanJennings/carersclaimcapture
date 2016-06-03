(function() {
/*
  window.initEvents = function(id, answer_yes, answer_no) {
    $("#" + answer_yes).on("click", function() {
      return $("#" + id).hide();
    });
    return $("#" + answer_no).on("click", function() {
      $("#" + id).show();
      return $("#" + id).css('display', "block");
    });
  };

  window.originWarning = function(answer1, answer2, answer3, warning) {
    if (!$("#" + answer3).prop('checked')) {
      $("#" + warning).hide();
    }
    $("#" + answer1).on("click", function() {
      return $("#" + warning).hide();
    });
    $("#" + answer2).on("click", function() {
      return $("#" + warning).hide();
    });
    return $("#" + answer3).on("click", function() {
      return $("#" + warning).show();
    });
  };
*/
  window.gaEvents = function(goodButton, badButton, otherButton) {
    $("#" + goodButton).on("click", function() {
      return trackEvent(window.location.pathname, $(this).attr("id"), "CorrectCountry");
    });
    $("#" + badButton).on("click", function() {
      return trackEvent(window.location.pathname, $(this).attr("id"), "WrongCountry");
    });
    return $("#" + otherButton).on("click", function() {
      return trackEvent(window.location.pathname, $(this).attr("id"), "AnotherCountry");
    });
  };

}).call(this);
