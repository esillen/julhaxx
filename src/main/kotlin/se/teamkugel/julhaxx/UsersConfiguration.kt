package se.teamkugel.julhaxx

import org.springframework.context.annotation.Configuration

@Configuration("userInit")
class UsersConfiguration(userRepository: UserRepository) {
    init {
        System.out.println("running userInit...")
        val cc1 = CompletedChallenge(
                day = 1,
                challengeNumber = 1,
                extraInfo = "Score: 10000"
        )
        val cc2 = CompletedChallenge(
                day = 2,
                challengeNumber = 1,
                extraInfo = "Score: 2737"
        )
        val cc3 = CompletedChallenge(
                day = 1,
                challengeNumber = 1,
                extraInfo = "Score: 37"
        )
        val erik = userRepository.saveIfUsernameDoesNotExist(JulhaxxUser("erik", "asdf",  mutableListOf(cc1, cc2)))
        val fredde = userRepository.saveIfUsernameDoesNotExist(JulhaxxUser("fredde", "asdf", mutableListOf(cc3)))
        val caro = userRepository.saveIfUsernameDoesNotExist(JulhaxxUser("caro", "asdf", mutableListOf()))
        val matte = userRepository.saveIfUsernameDoesNotExist(JulhaxxUser("matte", "asdf", mutableListOf()))

        System.out.println("Num Saved users" + userRepository.findAll().count())
    }


}

fun UserRepository.saveIfUsernameDoesNotExist(user: JulhaxxUser) {
    if (findByUsername(user.username) == null) {
        save(user)
    }
}
