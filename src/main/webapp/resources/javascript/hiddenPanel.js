(function() {
    // TODO: might need to clear details when it hides the inputs

    window.initPanelState = function(panelId, triggerId, triggerValue, triggerFunction, clearOnHide) {
        var show = false;        
        if(triggerFunction) {
            show = window[triggerFunction](triggerId);
        } else {
            show = inputValueEqualsTriggerValue(triggerId, triggerValue);
        }
        
        if(show === true) {
            showPanel(panelId, clearOnHide);
        } else {
            hidePanel(panelId, clearOnHide);
        }
    };
    
    window.inputValueEqualsTriggerValue = function(triggerId, triggerValue) {
        var show = false;
        $("#" + triggerId + " input").not('input[type="hidden"]').add("select#" + triggerId).each(function(){
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
        
        return show;
    }

    window.initPanelEvents = function(panelId, triggerId, triggerValue, triggerFunction, clearOnHide) {
        return $("#" + triggerId + " input").not('input[type="hidden"]').add("select#" + triggerId).on("change", function() {
            var show = true;
            if(triggerFunction) {
                show = window[triggerFunction](triggerId);
            } else {
                show = inputValueEqualsTriggerValue(triggerId, triggerValue)
            }
            
            if (show) {
                showPanel(panelId, clearOnHide);
                return $("#" + panelId).css('display', "block");
            } else {
                hidePanel(panelId, clearOnHide);
                return $("#" + panelId).hide();
            }
        });
    };
    
    window.showPanel = function(panelId, clearOnHide) {
        $("#" + panelId).show();
        if(clearOnHide === "true") {
            // nothing to do
        } else {
            // TODO, enable fields, according to surrounding panels (if any)
        }
    }
    
    window.hidePanel = function(panelId, clearOnHide) {
        $("#" + panelId).hide();
        if(clearOnHide === "true") {
            var panel = $("#" + panelId);
            panel.find('input')
                 .not('input[type="hidden"]')
                 .not('input[type="radio"]')
                 .not('input[type="checkbox"]').val('');
            
            panel.find('textarea').val('');
            panel.find('select').val('');
            
            panel.find('input[type="radio"]').removeAttr('checked');
            panel.find('input[type="checkbox"]').removeAttr('checked');
            panel.find("[data-tag-type^='hiddenPanel']").hide();
            panel.find("label.block-label.selected").removeClass("selected");
                            
        } else {
            // TODO, disable fields
        }
    }

}).call(this);
