function sendChallengeCode(challengeNumber, code) {
    //console.log("sending challenge code: " + code);
    var data = {"day" : activeDay, "challengeNumber" : challengeNumber, "code": code};
    $.post( "completeChallenge", data, function( data ) {
        //console.log(window.alert("Yay du vann!!"));
    });
}

