package se.teamkugel.julhaxx

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class CompletedChallenge(
        var day: Int,
        var challengeNumber: Int,
        var extraInfo: String = "",
        var completedAt: LocalDateTime = LocalDateTime.now(),
        @Id @GeneratedValue var id: Long? = null
)

@Entity
class JulhaxxUser(
        var username: String,
        var password: String,
        @OneToMany(cascade = [(CascadeType.ALL)]) var completedChallenges: MutableList<CompletedChallenge>,
        var emoji: String = "👶",
        @Id @GeneratedValue var id: Long? = null
) {
    fun hasCompletedChallenge(dayNumber: Int, challengeNumber: Int) : Boolean {
        return (completedChallenges.any { it.day == dayNumber && it.challengeNumber == challengeNumber })
    }

    //TODO: probably very inefficient
    fun getCompletedChallengesPerDay(): List<DayCompletion>{
        val listOfCollectedStars = numChallengesPerDay.toList()
                .sortedBy { it.first }
                .map{(0 until it.second).map { false }.toMutableList()}

        for (challenge in completedChallenges) {
            listOfCollectedStars[challenge.day - 1][challenge.challengeNumber-1] = true
        }
        return listOfCollectedStars.mapIndexed { index, boolList ->
            DayCompletion(index+1, boolList.count { it == true }, boolList)
        }
    }

}

@Entity
class Day(
        @Id var number: Int,
        var title: String,
        var available: Boolean
)

@Entity
class PersistedChatMessage(
        var username: String,
        var content: String,
        var type: ChatMessageType,
        var completedAt: LocalDateTime = LocalDateTime.now(),
        @Id @GeneratedValue var id: Long? = null
) {
    fun toChatMessage(userRepository: UserRepository): ChatMessage? {
        val user = userRepository.findByUsername(username)
        if (user != null) {
            return ChatMessage(user.username, user.emoji, user.completedChallenges.size, content, type)
        } else {
            return null
        }
    }

    fun toChatMessageSimple() : ChatMessage {
        return ChatMessage(username, "", 0, content, type)
    }

}