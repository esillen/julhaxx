package se.teamkugel.julhaxx

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<JulhaxxUser, Long> {
    fun findByUsername(username: String): JulhaxxUser?
}

interface CompletedChallengesRepository: CrudRepository<CompletedChallenge, Long> {
}

interface DaysRepository: CrudRepository<Day, Int> {
}