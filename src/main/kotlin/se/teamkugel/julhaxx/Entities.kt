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
}

@Entity
class Day(
        @Id var number: Int,
        var available: Boolean
)
