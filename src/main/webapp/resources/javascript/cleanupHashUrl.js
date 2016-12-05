$(document).ready(function() {

    // if there is a # by itself at then end of the URL, trim it off.
    // to do this we have to rewrite history (weird, but true)
    // and obviously it only works for html5 browsers, so not most IE browsers
    if(window.location.href.endsWith('#') && window.location.hash == '') {
        history.pushState("", document.title, window.location.pathname);
    }
});
