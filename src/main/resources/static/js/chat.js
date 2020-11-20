

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat', function (messageDataJSON) {
            var messageData = JSON.parse(messageDataJSON.body)
            showChatMessage(messageData.username, messageData.content);
        });
    });
}

function sendChatMessage() {
    stompClient.send("/app/chat", {}, JSON.stringify({'username': username, 'content': $("#chat-message-input").val()}));
}

function showChatMessage(username, message) {
    $("#chat-messages").append('<tr><td class="message">' + username + ": " + message + "</td></tr>");
}

$(function () {
    connect();
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { sendChatMessage(); });
});

$("chat-message-input").focus();