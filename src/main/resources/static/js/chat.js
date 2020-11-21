

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

function sendChatMessage() {
    stompClient.send("/app/chat", {}, JSON.stringify({'username': username, 'numStars': numStars, 'content': $("#chat-message-input").val()}));
}

function showChatMessage(messageData) {
    $("#chat-messages-container").append('<div class="chat-message">' + messageData.username + "(" + messageData.numStars + "⭐): " + messageData.content + "</div>");
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
    chatHistory.forEach(messageData => showChatMessage(messageData));
});

$("chat-message-input").focus();