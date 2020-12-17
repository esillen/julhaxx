package se.teamkugel.julhaxx

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

interface UserRepository : CrudRepository<JulhaxxUser, Long> {
    fun findByUsername(username: String): JulhaxxUser?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    @Query("SELECT u FROM JulhaxxUser u WHERE u.username = ?1")
    fun findByUsernameForChallengeUpdate(username: String): JulhaxxUser?
}

interface CompletedChallengesRepository: CrudRepository<CompletedChallenge, Long> {
}

interface DaysRepository: CrudRepository<Day, Int> {
    fun findByNumber(number: Int): Day?


}