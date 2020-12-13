package se.teamkugel.julhaxx

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

interface UserRepository : CrudRepository<JulhaxxUser, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    fun findByUsername(username: String): JulhaxxUser?
}

interface CompletedChallengesRepository: CrudRepository<CompletedChallenge, Long> {
}

interface DaysRepository: CrudRepository<Day, Int> {
}