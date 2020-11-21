package se.teamkugel.julhaxx

enum class ChatMessageType{FROM_USER, LOGIN, COMPLETED_CHALLENGE}

data class ChatMessage(val username: String, val numStars: Int, val content: String, val type: ChatMessageType)

data class ReceivedChatMessage(val username: String, val numStars: Int, val content: String)

