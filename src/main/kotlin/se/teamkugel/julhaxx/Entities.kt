package se.teamkugel.julhaxx

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class CompletedChallenge(
        var day: Int,
        var extraInfo: String,
        @ManyToOne var user: User,
        var completedAt: LocalDateTime = LocalDateTime.now(),
        @Id @GeneratedValue var id: Long? = null)

@Entity
class User(
        var username: String,
        var password: String,
        var completion: Int = 0,
        @Id @GeneratedValue var id: Long? = null)

