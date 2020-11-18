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
        val erik = userRepository.save(User("erik", "asdf"))
        val fredde = userRepository.save(User("fredde", "asdf"))
        completedChallengesRepository.save(CompletedChallenge(
                day = 1,
                extraInfo = "Score: 10000",
                user = erik
        ))
        completedChallengesRepository.save(CompletedChallenge(
                day = 2,
                extraInfo = "Score: 2737",
                user = erik
        ))
        completedChallengesRepository.save(CompletedChallenge(
                day = 1,
                extraInfo = "Score: 37",
                user = fredde
        ))
        System.out.println("Num Saved users" + userRepository.findAll().count())
    }

}