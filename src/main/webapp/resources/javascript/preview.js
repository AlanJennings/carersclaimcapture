(function() {
  window.trackChange = function() {};

  $(".review-action a.button").click(function() {
    var id;
    id = $(this).attr("id");
    return trackEvent('check-your-answers', 'change', id);
  });

  $(".review-action a.previewChangeLink").click(function() {
    var id;
    id = $(this).attr("id");
    return trackEvent('check-your-answers', 'change', id);
  });

  $(".button-print").click(function() {
    return trackEvent('check-your-answers', 'print');
  });

}).call(this);

