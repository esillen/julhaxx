package se.teamkugel.julhaxx

import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.HtmlUtils.htmlEscape
import java.security.Principal
import java.util.*

@Controller
class WebSocketsController(val userRepository: UserRepository,
                           val websocketsTemplate: SimpMessagingTemplate) {

    @Transactional
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    @Throws(Exception::class)
    fun receiveChatMessage(receivedMessage: ReceivedChatMessage, principal: Principal): ChatMessage {
        val user = userRepository.findByUsername(principal.name)
        if (user == null) {
            throw Exception("Could not find user ${principal.name}")
        } else {
            val internalMessage = InternalChatMessage(user.username, htmlEscape(receivedMessage.content), ChatMessageType.FROM_USER)
            addMessageToQueue(internalMessage)
            val chatMessage = internalMessage.toChatMessage(userRepository)
            if (chatMessage != null) {
                return chatMessage
            } else {
                throw Exception("Could not convert message :/")
            }
        }
    }

    fun sendCompletedMessage(julhaxxUser: JulhaxxUser, day: Int, challengeNumber: Int) {
        val internalMessage = InternalChatMessage(julhaxxUser.username, "dag $day, stjärna nummer $challengeNumber", ChatMessageType.COMPLETED_CHALLENGE)
        addMessageToQueue(internalMessage)
        trySendMessage(internalMessage)
    }

    @Transactional
    @EventListener
    fun onLoggedIn(loggedInEvent: InteractiveAuthenticationSuccessEvent) {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user != null) {
            val internalMessage = InternalChatMessage(user.username, "", ChatMessageType.LOGIN)
            addMessageToQueue(internalMessage)
            trySendMessage(internalMessage)
        }
    }

    private fun trySendMessage(internalMessage: InternalChatMessage) {
        val chatMessage = internalMessage.toChatMessage(userRepository)
        if (chatMessage != null) {
            websocketsTemplate.convertAndSend("/topic/chat", chatMessage)
        }
    }

    private fun addMessageToQueue(message: InternalChatMessage) {
        savedMessagesQueue.add(message)
        if (savedMessagesQueue.size > CHAT_HISTORY) {
            savedMessagesQueue.poll()
        }
    }

    companion object {
        const val CHAT_HISTORY = 50
        val savedMessagesQueue: Queue<InternalChatMessage> = LinkedList()
    }
}