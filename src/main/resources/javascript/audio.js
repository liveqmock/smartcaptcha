playSound = function(soundUrl) {

    if (checkHtml5AudioSupport()) {
        var sound = new Audio(soundUrl);
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