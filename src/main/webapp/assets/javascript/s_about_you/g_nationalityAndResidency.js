(function() {
  var clearDownStreamInputs, clearDownStreamTextAreas, clearInput, hideWrapper, setVisibility, showWrapper;

  window.initNationalityEvents = function(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails) {
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    $("#" + nationalityBritish).on("click", function() {
      return setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    });
    $("#" + nationalityAnotherCountry).on("click", function() {
      return setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    });
    $("#" + alwaysLivedInUKY).on("click", function() {
      return setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    });
    $("#" + alwaysLivedInUKN).on("click", function() {
      return setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    });
    $("#" + liveInUKNowY).on("click", function() {
      return setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    });
    $("#" + liveInUKNowN).on("click", function() {
      return setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    });
    $("#" + arrivedInUKless).on("click", function() {
      return setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    });
    $("#" + arrivedInUKmore).on("click", function() {
      return setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    });
    $("#" + trip52weeksY).on("click", function() {
      return setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    });
    return $("#" + trip52weeksN).on("click", function() {
      return setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails);
    });
  };

  setVisibility = function(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails) {
    $("#" + tripDetails).trigger("blur");
    if ($("#" + nationalityAnotherCountry).prop('checked')) {
      showWrapper(actualNationalityWrap);
    } else {
      hideWrapper(actualNationalityWrap);
    }
    if ($("#" + alwaysLivedInUKN).prop('checked')) {
      showWrapper(alwaysLivedInUKNoWrap);
    } else {
      hideWrapper(alwaysLivedInUKNoWrap);
    }
    if ($("#" + liveInUKNowY).prop('checked')) {
      showWrapper(liveInUKNowYesWrap);
    } else {
      hideWrapper(liveInUKNowYesWrap);
    }
    if ($("#" + arrivedInUKless).prop('checked')) {
      showWrapper(arrivedInUKRecentWrap);
    } else {
      hideWrapper(arrivedInUKRecentWrap);
    }
    if ($("#" + trip52weeksY).prop('checked')) {
      return showWrapper(trip52weeksYesWrap);
    } else {
      return hideWrapper(trip52weeksYesWrap);
    }
  };

  showWrapper = function(wrapper) {
    return $("#" + wrapper).slideDown(0).attr('aria-hidden', 'false');
  };

  hideWrapper = function(wrapper) {
    clearDownStreamInputs(wrapper);
    clearDownStreamTextAreas(wrapper);
    return $("#" + wrapper).slideUp(0).attr('aria-hidden', 'true');
  };

  clearDownStreamInputs = function(wrapper) {
    return $("#" + wrapper).find("input").each(clearInput);
  };

  clearDownStreamTextAreas = function(wrapper) {
    return $("#" + wrapper).find("textarea").each(clearInput);
  };

  clearInput = function() {
    if ($(this).attr("type") === "radio") {
      $(this).prop('checked', false);
      return $(this).parent().removeClass("selected");
    } else {
      return $(this).val("");
    }
  };

}).call(this);

//# sourceMappingURL=g_nationalityAndResidency.js.map
