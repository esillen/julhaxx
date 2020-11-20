package se.teamkugel.julhaxx

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class CompletedChallenge(
        var day: Int,
        var extraInfo: String,
        var completedAt: LocalDateTime = LocalDateTime.now(),
        @Id @GeneratedValue var id: Long? = null)

@Entity
class User(
        var username: String,
        var password: String,
        @OneToMany(cascade = [(CascadeType.ALL)]) var completedChallenges: MutableList<CompletedChallenge>,
        @Id @GeneratedValue var id: Long? = null)

@Entity
class Day(
        @Id var number: Int,
        var available: Boolean,
        var challengeCode: String
)