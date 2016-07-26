(function() {
    // TODO: might need to clear details when it hides the inputs

    window.initPanelState = function(panelId, triggerId, triggerValue, clearOnHide) {
        var show = false;
        $("#" + triggerId + " input").add("select#" + triggerId).each(function(){
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
            showPanel(panelId, clearOnHide);
        } else {
            $("#" + panelId).hide();
            hidePanel(panelId, clearOnHide);
            
        }
    };

    window.initPanelEvents = function(panelId, triggerId, triggerValue, clearOnHide) {
        return $("#" + triggerId + " input").add("select#" + triggerId).on("change", function() {
            if ($(this).val() === triggerValue) {
                $("#" + panelId).show();
                showPanel("#" + panelId, clearOnHide);
                return $("#" + panelId).css('display', "block");
            } else {
                hidePanel(panelId, clearOnHide);
                return $("#" + panelId).hide();
            }
        });
    };
    
    window.showPanel = function(panelId, clearOnHide) {
        if(clearOnHide === "true") {
            // nothing to do
        } else {
            // TODO, enable fields, according to surrounding panels (if any)
        }
    }
    
    window.hidePanel = function(panelId, clearOnHide) {
        if(clearOnHide === "true") {
            $("#" + panelId).find('input').add('textarea').add('select').val('');
        } else {
            // TODO, disable fields
        }
    }

}).call(this);
