package se.teamkugel.julhaxx

import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Controller
class WebSocketsController(val userRepository: UserRepository,
                           val websocketsTemplate: SimpMessagingTemplate) {

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    @Throws(Exception::class)
    fun receiveChatMessage(message: ChatMessage): ChatMessage {
        addMessageToQueue(message)
        return message
    }

    fun sendCompletedMessage(user: User, day: String) {
        val message = ChatMessage(user.username, user.completedChallenges.size, "Klarade just dag $day!")
        addMessageToQueue(message)
        websocketsTemplate.convertAndSend("/topic/chat", message)
    }

    @Transactional
    @EventListener
    fun onLoggedIn(loggedInEvent: InteractiveAuthenticationSuccessEvent) {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user != null) {
            val message = ChatMessage(loggedInEvent.authentication.name, user.completedChallenges.size, "Loggade just in!")
            addMessageToQueue(message)
            //websocketsTemplate.convertAndSend("/topic/chat", message)
        }
    }

    private fun addMessageToQueue(message: ChatMessage) {
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