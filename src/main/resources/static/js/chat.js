

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat', function (messageDataJSON) {
            var messageData = JSON.parse(messageDataJSON.body)
            showChatMessage(messageData.username, messageData.numStars, messageData.content);
        });
    });
}

function sendChatMessage() {
    stompClient.send("/app/chat", {}, JSON.stringify({'username': username, 'numStars': numStars, 'content': $("#chat-message-input").val()}));
}

function showChatMessage(username, numStars, message) {
    $("#chat-messages-container").append('<div class="message">' + username + "(" + numStars + "⭐): " + message + "</div>");
}

$(function () {
    connect();
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#chat-send" ).click(function() {
        sendChatMessage();
        $("chat-message-input").val("");
    });
});

$("chat-message-input").focus();