(function() {
  window.initialButtonState = function(answerY, answerN) {
    if (!$("#" + answerY).prop('checked') && !$("#" + answerN).prop('checked')) {
      $("#showAppropriateButtonWrap").css('display', "none");
    }
    if ($("#" + answerN).prop('checked')) {
      $("#actionButtonId").css('display', "none");
      $("#feedbackLinkId").css('display', "block");
      $("#showAppropriateButtonWrap").css('display', "block");
    }
    if ($("#" + answerY).prop('checked')) {
      $("#actionButtonId").css('display', "block");
      $("#feedbackLinkId").css('display', "none");
      return $("#showAppropriateButtonWrap").css('display', "block");
    }
  };

  window.answer = function(answerY, answerN) {
    $("#" + answerN).on("click", function() {
      $("#actionButtonId").css('display', "none");
      $("#feedbackLinkId").css('display', "block");
      return $("#showAppropriateButtonWrap").css('display', "block");
    });
    return $("#" + answerY).on("click", function() {
      $("#actionButtonId").css('display', "block");
      $("#feedbackLinkId").css('display', "none");
      return $("#showAppropriateButtonWrap").css('display', "block");
    });
  };

}).call(this);