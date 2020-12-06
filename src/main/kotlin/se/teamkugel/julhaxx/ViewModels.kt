package se.teamkugel.julhaxx

data class TopRowDay(val number: Int, val available: Boolean, val current: Boolean)
data class LeaderboardUser(val username: String, val numStars: Int)
data class InspectedUser(val username: String, val numStars: Int, val emoji: String, val completionPerDay: List<DayCompletion>)


data class DayCompletion(val dayNumber: Int, val numCollectedStars: Int, val collectedStars: List<Boolean>)