function sendChallengeCode(code) {
    console.log("sending challenge code: " + code);
    $.post( "code?day=" + activeDay, code, function( data ) {
        console.log(window.alert("Yay du vann!!"));
    });

}