

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat', function (greeting) {
            showChatMessage(JSON.parse(greeting.body).content);
        });
    });
}

function sendChatMessage() {
    stompClient.send("/app/chat", {}, JSON.stringify({'username': username, 'content': $("#chat-message-input").val()}));
}

function showChatMessage(message) {
    $("#chat-messages").append('<tr><td class="message">' + message + "</td></tr>");
}

$(function () {
    connect();
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { sendChatMessage(); });
});