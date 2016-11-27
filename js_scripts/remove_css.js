
var css = '*{ margin-left: 5px !important; font-size: 32px !important; margin-top: 0 !important; -webkit-margin-after: 2px !important;} input, textarea, select{background-color: black !important;margin-bottom: 15px !important;margin-top: 15px !important; width: 300px !important; height: 40px !important;}::-webkit-input-placeholder { /* Chrome/Opera/Safari */color: black;}::-moz-placeholder { /* Firefox 19+ */  color: black;}:-ms-input-placeholder { /* IE 10+ */  color: black;}:-moz-placeholder { /* Firefox 18- */  color: black;}',
        head = document.head || document.getElementsByTagName('head')[0],
        style = document.createElement('style');
style.setAttribute("name", "hacked_css_123");

style.type = 'text/css';
if (style.styleSheet) {
    style.styleSheet.cssText = css;
} else {
    style.appendChild(document.createTextNode(css));
}

head.appendChild(style);

setInterval(function () {
    var toRemove = [];
    toRemove.push.apply(toRemove, document.querySelectorAll('link[type*="css"]'));
    toRemove.push.apply(toRemove, document.querySelectorAll('style'));
    toRemove.push.apply(toRemove, document.querySelectorAll('img'));
    toRemove.push.apply(toRemove, document.querySelectorAll('canvas'));
    toRemove.forEach(function (s) {

        if (s.getAttribute("name") !== "hacked_css_123") {
            console.log(s.getAttribute('name'));
            s.parentNode.removeChild(s);
        }

    });

    [].forEach.call(document.querySelectorAll('[style]'), function (e) {

        e.removeAttribute('style');
    });

    var stylesheets = document.styleSheets;
    var len = stylesheets.length;
    while (len != 0) {
        var sh = stylesheets[len - 1];
        if (sh.ownerNode.getAttribute("name") == "hacked_css_123") {
            len--;
            continue;
        }

        var count = sh.rules.length;

        while (count != 0) {
            sh.deleteRule(0);
            count = sh.rules.length;
        }
        len--;
    }

    var km = ['click', 'dblclick', 'mousedown', 'mousemove', 'mouseover', 'mouseout', 'mouseup', 'mouseenter', 'mouseleave', 'keydown', 'keypress', 'keyup', 'scroll'];
    function preventAll(parent) {
        var dom = parent.getElementsByTagName('*');
        for (var i = 0, l = dom.length; i < l; i++) {
            for (var n = 0, c = km.length; n < c; n++) {
                dom[i]['on' + km[n]] = function (e) {
                    e = e || event;
                    e.preventDefault();
                    return false;
                }
            }
        }
        var fr = frames;
        for (var i = 0, l = fr.length; i < l; i++) {
            // cancell frames events here
        }
    }
    preventAll(document);


    for (var n = 0, c = km.length; n < c; n++) {
        window['on' + km[n]] = function (e) {
            e = e || event;
            e.preventDefault();
            return false;
        }
    }

}, 1000);
