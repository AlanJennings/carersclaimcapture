(function() {
  var hideEmail, resetEmail, showEmail;

  window.emailInit = function(wantsEmailContactY, wantsEmailContactN, email, emailConfirmation) {
    if ($("#" + wantsEmailContactY).prop('checked')) {
      showEmail();
    } else if ($("#" + wantsEmailContactN).prop('checked')) {
      hideEmail(email, emailConfirmation);
    } else {
      resetEmail();
    }
    $("#" + wantsEmailContactY).on("click", function() {
      return showEmail();
    });
    return $("#" + wantsEmailContactN).on("click", function() {
      return hideEmail(email, emailConfirmation);
    });
  };

  resetEmail = function() {
    $("#emailWrap").slideUp(0).attr('aria-hidden', 'true');
    $("#emailYesHelper").slideUp(0).attr('aria-hidden', 'true');
    return $("#emailNoHelper").slideUp(0).attr('aria-hidden', 'true');
  };

  showEmail = function() {
    $("#emailWrap").slideDown(0).attr('aria-hidden', 'false');
    $("#emailYesHelper").slideDown(0).attr('aria-hidden', 'false');
    return $("#emailNoHelper").slideUp(0).attr('aria-hidden', 'true');
  };

  hideEmail = function(email, emailConfirmation) {
    $("#" + email).val("");
    $("#" + emailConfirmation).val("");
    $("#emailWrap").slideUp(0).attr('aria-hidden', 'true');
    $("#emailYesHelper").slideUp(0).attr('aria-hidden', 'true');
    return $("#emailNoHelper").slideDown(0).attr('aria-hidden', 'false');
  };

}).call(this);

//# sourceMappingURL=email.js.map
