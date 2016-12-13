var googleAnalytics = googleAnalytics || {};
googleAnalytics.init = function (data) {
    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date();
        a = s.createElement(o),
            m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
    })(window, document, 'script', data.url, 'ga');

    ga('create', data.agentid, {
        'siteSpeedSampleRate': '1'
    });
    ga('send', 'pageview');
    ga('set', 'anonymizeIp', true);
    ga('set', 'nonInteraction', true);
}

googleAnalytics.bindRadioButtons = function () {
    $("input:radio").click(function () {
        var cat = location.pathname;
        var action = $(this).attr("name");
        var label = $(this).next("span").html().trim();
        var v = "";
        trackEvent(cat, action, label, v);
    });
}

googleAnalytics.sendError = function (label) {
    console.log("GA sending error for label:" + label);
    var cat = location.pathname;
    var action = "Error";
    var v = "";
    trackEvent(cat, action, label, v);
}

googleAnalytics.trackTiming = function (category, variable, timemeasured) {
    if (0 < timemeasured && timemeasured < 36000000) {
        ga('send', 'timing', {
            'timingCategory': category,
            'timingVar': variable,
            'timingValue': timemeasured
        });
    }
}

googleAnalytics.sendTrackEvent = function (category, action, label) {
    if( category == ""){
        category=location.pathname;
    }
    var v = "";
    trackEvent(category, action, label, v);
}

function trackEvent(category, action, label, value) {
    $(document).ready(function () {
        ga('send', 'event', getConfigurationObject(category, action, label, value));
    });
}

function getConfigurationObject(category, action, label, value) {
    var configurationObject = {};
    if (typeof category != 'undefined') configurationObject.eventCategory = category;
    if (typeof action != 'undefined') configurationObject.eventAction = action;
    if (typeof label != 'undefined') configurationObject.eventLabel = label;
    if (typeof value != 'undefined') configurationObject.eventValue = value;
    return configurationObject;
}


