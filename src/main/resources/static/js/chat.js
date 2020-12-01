

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat', function (messageDataJSON) {
            var messageData = JSON.parse(messageDataJSON.body)
            showChatMessage(messageData);
        });
    });
}

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
    $("#chat-messages-container").append('<div class="chat-message-from-user">' + messageData.username + "(" + messageData.numStars + "⭐): " + messageData.content + "</div>");
}

function showLoginMessage(messageData) {
    $("#chat-messages-container").append('<div class="chat-message-login">' + messageData.username + "(" + messageData.numStars + "⭐) loggade in! </div>");
}

function showCompletedChallengeMessage(messageData) {
    $("#chat-messages-container").append('<div class="chat-message-completed-challenge">' + messageData.username + "(" + messageData.numStars + "⭐) klarade just " + messageData.content + "!👏  🎅🎅🎅</div>");
}

$(function () {
    connect();
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#chat-send" ).click(function() {
        sendChatMessage();
        $("#chat-message-input").val("");
    });
    chatHistory.forEach(messageData => showChatMessage(messageData));
});

$("#chat-message-input").focus();