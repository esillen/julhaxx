package se.teamkugel.julhaxx

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class WebSocketsController {

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    @Throws(Exception::class)
    fun receiveChatMessage(message: ChatMessage): ChatMessage {
        return ChatMessage(message.username, message.content)
    }

    @MessageMapping("/login")
    @SendTo("/topic/chat")
    @Throws(Exception::class)
    fun receiveLoginMessage(message: ChatMessage): ChatMessage {
        return ChatMessage(message.username, "Loggade just in!")
    }

    @SendTo("/topic/chat")
    @Throws(Exception::class)
    fun sendCompletedMessage(message: ChatMessage): ChatMessage {
        return ChatMessage(message.username, "Loggade just in!")
    }

    companion object {
        // TODO: save messages


    }
}