package se.teamkugel.julhaxx

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class WebSocketsController {

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    @Throws(Exception::class)
    fun receiveChatMessage(message: ChatMessage): ChatMessage {
        addMessageToQueue(message)
        return message
    }

    @MessageMapping("/login")
    @SendTo("/topic/chat")
    @Throws(Exception::class)
    fun receiveLoginMessage(message: ChatMessage): ChatMessage {
        addMessageToQueue(message)
        return ChatMessage(message.username, message.numStars, "Loggade just in!")
    }

    @SendTo("/topic/chat")
    @Throws(Exception::class)
    fun sendCompletedMessage(message: ChatMessage): ChatMessage {
        addMessageToQueue(message)
        return ChatMessage(message.username, message.numStars,"Loggade just in!")
    }

    fun addMessageToQueue(message: ChatMessage) {
        savedMessagesQueue.add(message)
        if (savedMessagesQueue.size > CHAT_HISTORY) {
            savedMessagesQueue.poll()
        }
    }

    companion object {
        const val CHAT_HISTORY = 50
        val savedMessagesQueue: Queue<ChatMessage> = LinkedList()
    }
}