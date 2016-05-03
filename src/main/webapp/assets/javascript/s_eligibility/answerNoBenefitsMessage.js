(function() {
  var benefitsSelector;

  benefitsSelector = function(selector) {
    return $("#" + selector + " input");
  };

  window.initEvents = function(benefitsAnswerId, nobValue) {
    return benefitsSelector(benefitsAnswerId).on("change", function() {
      if ($(this).val() === nobValue) {
        $("#answerNoMessageWrap").show();
        return $("#answerNoMessageWrap").css('display', "block");
      } else {
        return $("#answerNoMessageWrap").hide();
      }
    });
  };

}).call(this);

