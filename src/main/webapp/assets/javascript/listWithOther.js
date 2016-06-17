(function() {
  $(function() {
    $("input[class='listWithOther otherValue']").each(function() {
      if (!($(this).prop("checked"))) {
        return $(this).closest("ul").next().hide();
      }
    });
    $("input[class='listWithOther otherValue']").change(function() {
      $(this).closest("ul").next().find("textarea").trigger("blur");
      return $(this).closest("ul").next().slideDown(0);
    });
    return $("input[class='listWithOther']").change(function() {
      var textArea;
      textArea = $(this).closest("ul").next().find("textarea");
      return $(this).closest("ul").next().slideUp(0, (function() {
        return textArea.val("");
      }));
    });
  });

}).call(this);

//# sourceMappingURL=listWithOther.js.map
