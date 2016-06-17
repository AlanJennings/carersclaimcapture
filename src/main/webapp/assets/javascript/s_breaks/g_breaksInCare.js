(function() {
  window.initSummary = function(deleteId) {
    if ($("ul.break-data").children().length === 0) {
      $("#breaks").hide();
    }
    $(".breaks-prompt").hide();
    $("ul").on("click", "input[name='changerow']", function() {
      var li;
      li = $(this).closest("li");
      return window.location.href = "/breaks/break/" + li.attr("id");
    });
    $("#backButton").on("click", function(event) {
      if ($("#backButton").attr("disabled") === "disabled") {
        return event.preventDefault();
      }
    });
    if ($("#warningMessageWrap").length > 0) {
      $("#warningMessageWrap").hide();
    }
    return $("ul").on("click", "input[name='deleterow']", function() {
      var li, prompt;
      li = $(this).closest("li");
      prompt = li.next();
      return prompt.slideDown(function() {
        prompt.find("#noDelete").on("click", function() {
          return prompt.slideUp();
        });
        return prompt.find("#yesDelete").on("click", function() {
          var id;
          id = li.attr("id");
          $("#" + deleteId).val(id);
          return $("#deleteForm").submit();
        });
      });
    });
  };

  window.initEvents = function(answer_yes, answer_no, testMode) {
    $("#" + answer_yes).on("click", function() {
      if ($("#warningMessageWrap").length > 0) {
        $("#warningMessageWrap").show();
        if (!testMode) {
          return trackEvent(window.location.pathname, "Error", $("#warningMessageWrap").text().trim());
        }
      }
    });
    return $("#" + answer_no).on("click", function() {
      if ($("#warningMessageWrap").length > 0) {
        return $("#warningMessageWrap").hide();
      }
    });
  };

  window.updateNextLabel = function(answer_yes, answer_no, textNext, textReturn) {
    var button;
    button = $("button.button");
    if ($("#" + answer_yes).is(":checked")) {
      button.text(textNext);
    }
    $("#" + answer_yes).on("click", function() {
      return button.text(textNext);
    });
    return $("#" + answer_no).on("click", function() {
      return button.text(textReturn);
    });
  };

}).call(this);

//# sourceMappingURL=g_breaksInCare.js.map
