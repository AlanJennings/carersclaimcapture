(function() {
  var valueEntered;

  valueEntered = function(selector) {
    if (($("#" + selector).val()) !== "") {
      return "yes";
    } else {
      return "no";
    }
  };

  window.trackInputOnEventInit = function(label, trackChangesTo, whenEventTriggered) {
    if ($("#" + trackChangesTo)) {
      return $("." + whenEventTriggered).on("click", function() {
        return trackEvent(window.location.pathname, label, valueEntered(trackChangesTo));
      });
    }
  };

}).call(this);

//# sourceMappingURL=trackInputOnEvent.js.map
