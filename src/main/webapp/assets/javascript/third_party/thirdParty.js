(function() {
  var hideThirdPartyWrap, showThirdPartyWrap;

  window.initEvents = function(thirdPartyYourTheCarer, thirdPartyYourNotTheCarer, organisation) {
    if (!$("#" + thirdPartyYourNotTheCarer).prop('checked')) {
      hideThirdPartyWrap(organisation);
    }
    if ($("#" + thirdPartyYourNotTheCarer).prop('checked')) {
      showThirdPartyWrap();
    }
    $("#" + thirdPartyYourNotTheCarer).on("click", function() {
      return showThirdPartyWrap();
    });
    return $("#" + thirdPartyYourTheCarer).on("click", function() {
      return hideThirdPartyWrap(organisation);
    });
  };

  hideThirdPartyWrap = function(organisation) {
    var emptyOrganisation;
    emptyOrganisation = function() {
      return $("#" + organisation).val("");
    };
    return $("#thirdPartyWrap").slideUp(0, emptyOrganisation).attr('aria-hidden', 'true');
  };

  showThirdPartyWrap = function() {
    return $("#thirdPartyWrap").slideDown(0).attr('aria-hidden', 'false');
  };

}).call(this);
