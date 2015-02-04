playSound = function(soundUrl) {

    if (checkHtml5AudioSupport()) {
        // Because of Mozilla Firefox bug we should to generate random parameter of request the order to guarantee invocation of servlet.
        // See more: https://bugzilla.mozilla.org/show_bug.cgi?id=1129121
        var sound = new Audio(soundUrl + "?" + Math.random());
        sound.type = 'audio/wav';
        sound.autobuffer = false;

        sound.play();
    } else {
        var div = document.createElement('div');
        div.innerHTML = "<embed src='" + soundUrl + "' autoplay='true' hidden='true' volume='100' type='audio/wav'/>";
        document.body.appendChild(div);
    }
};

checkHtml5AudioSupport = function() {
    var browserCompatibilityCheck = document.createElement('audio');
    return (!!(browserCompatibilityCheck.canPlayType) && !!(browserCompatibilityCheck.canPlayType("audio/wav")));
};