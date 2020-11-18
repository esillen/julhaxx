package se.teamkugel.julhaxx

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class WSController {

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    @Throws(Exception::class)
    fun greeting(message: ChatMessage): ChatMessage {
        Thread.sleep(1000) // simulated delay
        return ChatMessage(message.username, "from server" + message.content)
    }
}