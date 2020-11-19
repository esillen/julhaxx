package se.teamkugel.julhaxx

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration("userInit")
class UsersConfiguration(userRepository: UserRepository,
                         completedChallengesRepository: CompletedChallengesRepository) {
    init {
        System.out.println("running userInit...")
        val cc1 = completedChallengesRepository.save(CompletedChallenge(
                day = 1,
                extraInfo = "Score: 10000"
        ))
        val cc2 = completedChallengesRepository.save(CompletedChallenge(
                day = 2,
                extraInfo = "Score: 2737"
        ))
        val cc3 = completedChallengesRepository.save(CompletedChallenge(
                day = 1,
                extraInfo = "Score: 37"
        ))
        val erik = userRepository.save(User("erik", "asdf", mutableListOf(cc1, cc2)))
        val fredde = userRepository.save(User("fredde", "asdf", mutableListOf(cc3)))
        val caro = userRepository.save(User("caro", "asdf", mutableListOf()))

        System.out.println("Num Saved users" + userRepository.findAll().count())
    }

}