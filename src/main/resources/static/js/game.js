function sendChallengeCode(challengeNumber, code) {
    console.log("sending challenge code: " + code);
    $.post( "completeChallenge", activeDay, challengeNumber, code, function( data ) {
        //console.log(window.alert("Yay du vann!!"));
    });
}

