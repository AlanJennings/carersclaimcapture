(function() {
    // TODO: might need to clear details when it hides the inputs

    window.initPanelState = function(panelId, triggerId, triggerValue) {
        var show = false;
        $("#" + triggerId + " input").each(function(){
            var that = $(this);
            if(that.val() === triggerValue) {
                if(that.attr('type') === "radio" || that.attr('type') === "checkbox") {
                    if(that.is(':checked')) {
                        show = true;
                    }
                } else {
                    show = true;
                }
            }
        });

        if(show === true) {
            $("#" + panelId).show();
        } else {
            $("#" + panelId).hide();
        }
    };

    window.initPanelEvents = function(panelId, triggerId, triggerValue) {
        return $("#" + triggerId + " input").on("change", function() {
            if ($(this).val() === triggerValue) {
                $("#" + panelId).show();
                return $("#" + panelId).css('display', "block");
            } else {
                return $("#" + panelId).hide();
            }
        });
    };
}).call(this);
