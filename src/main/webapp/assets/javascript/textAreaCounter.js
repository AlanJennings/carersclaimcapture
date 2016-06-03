(function() {
  var checkValidCharacters, executeEvent;

  executeEvent = function(selector, maxChars) {
    var elem, helper, num, text;
    elem = $(selector);
    checkValidCharacters(elem, maxChars);
    helper = elem.parent().find(".countdown");
    text = helper.html();
    num = maxChars - elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n").length;
    if ((text !== void 0) && (text.length > 0)) {
      return helper.html(text.replace(/-?([0-9]+)/, num));
    }
  };

  checkValidCharacters = function(elem, maxChars) {
    var newElem, newMaxChars;
    newElem = elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n");
    newMaxChars = maxChars;
    if (newElem.lastIndexOf("\r\n") === maxChars - 1) {
      newMaxChars = maxChars - 1;
    }
    return elem.val(elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n").substr(0, newMaxChars));
  };

  window.areaCounter = function(textarea) {
    var selector;
    selector = "#" + textarea.selector;
    if ($(selector).length > 0) {
      $(selector).on("click", function() {
        return executeEvent(selector, textarea.maxChars);
      });
      $(selector).on("blur", function() {
        return executeEvent(selector, textarea.maxChars);
      });
      return $(selector).on("keyup", function() {
        return executeEvent(selector, textarea.maxChars);
      });
    }
  };

}).call(this);

//# sourceMappingURL=textAreaCounter.js.map
