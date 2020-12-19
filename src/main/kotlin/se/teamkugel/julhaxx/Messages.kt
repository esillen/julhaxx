package se.teamkugel.julhaxx

enum class ChatMessageType{FROM_USER, LOGIN, COMPLETED_CHALLENGE}

data class ChatMessage(val username: String, val emoji: String, val numStars: Int, val content: String, val type: ChatMessageType)
data class InternalChatMessage(val username: String, val content: String, val type: ChatMessageType) {
    fun toChatMessage(userRepository: UserRepository): ChatMessage? {
        val user = userRepository.findByUsername(username)
        if (user != null) {
            return ChatMessage(user.username, user.emoji, user.completedChallenges.size, content, type)
        } else {
            return null
        }
    }

    fun toPersistedChatMessage() : PersistedChatMessage {
        return PersistedChatMessage(username, content, type)
    }

}

data class ReceivedChatMessage(val content: String)

