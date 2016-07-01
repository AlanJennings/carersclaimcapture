(function() {
    var checkValidCharacters, executeEvent;

    executeEvent = function(selector, maxChars) {
        var elem = $(selector);
        checkValidCharacters(elem, maxChars);

        var helper = elem.parent().find(".countdown");
        var text = helper.html();
        var num = maxChars - elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n").length;

        if ((text !== void 0) && (text.length > 0)) {
            return helper.html(text.replace(/-?([0-9]+)/, num));
        }
    };

    checkValidCharacters = function(elem, maxChars) {
        var newElem = elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n");
        var newMaxChars = maxChars;
        if (newElem.lastIndexOf("\r\n") === maxChars - 1) {
            newMaxChars = maxChars - 1;
        }
        return elem.val(elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n").substr(0, newMaxChars));
    };

    window.areaCounter = function(id, maxChars) {
        var selector = $("#" + id);
        if (selector.length > 0) {
            selector.on("click", function() {
                return executeEvent("#" + id, maxChars);
            });
            selector.on("blur", function() {
                return executeEvent("#" + id, maxChars);
            });
            return selector.on("keyup", function() {
                return executeEvent("#" + id, maxChars);
            });
        }
    };

}).call(this);

