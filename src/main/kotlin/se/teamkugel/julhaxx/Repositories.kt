package se.teamkugel.julhaxx

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
}

interface CompletedChallengesRepository: CrudRepository<CompletedChallenge, Long> {
}

interface DaysRepository: CrudRepository<Day, Int> {
}