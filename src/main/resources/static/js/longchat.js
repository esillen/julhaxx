function showChatMessage(messageData){
    if (messageData.type === "FROM_USER"){
        showUserMessage(messageData);
    } else if (messageData.type == "LOGIN"){
        showLoginMessage(messageData);
    } else if (messageData.type == "COMPLETED_CHALLENGE"){
        showCompletedChallengeMessage(messageData);
    }
}

function sendChatMessage() {
    stompClient.send("/app/chat", {}, JSON.stringify({'content': $("#chat-message-input").val()}));
}

function showUserMessage(messageData) {
    $("#chat-messages-container").append('<div class="chat-message-from-user">' + messageData.emoji + " " + messageData.username + "(" + messageData.numStars + "⭐): " + messageData.content + "</div>");
}

function showLoginMessage(messageData) {
    $("#chat-messages-container").append('<div class="chat-message-login">' + messageData.emoji + " " + messageData.username + "(" + messageData.numStars + "⭐) loggade in! </div>");
}

function showCompletedChallengeMessage(messageData) {
    $("#chat-messages-container").append('<div class="chat-message-completed-challenge">' + messageData.emoji + " " + messageData.username + "(" + messageData.numStars + "⭐) klarade just " + messageData.content + "!👏  🎅🎅🎅</div>");
}

$(function () {
    chatHistory.forEach(messageData => showChatMessage(messageData));
});
