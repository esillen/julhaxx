package se.teamkugel.julhaxx

import org.springframework.context.annotation.Configuration

@Configuration("userInit")
class UsersConfiguration(userRepository: UserRepository) {
    init {
        System.out.println("running userInit...")
        val usersBefore = userRepository.count()
        //val cc1 = CompletedChallenge(
        //        day = 1,
        //        challengeNumber = 1,
        //        extraInfo = "Score: 10000"
        //)
        //val cc2 = CompletedChallenge(
        //        day = 2,
        //        challengeNumber = 1,
        //        extraInfo = "Score: 2737"
        //)
        //val cc3 = CompletedChallenge(
        //        day = 1,
        //        challengeNumber = 1,
        //        extraInfo = "Score: 37"
        //)
        //val erik = userRepository.saveIfUsernameDoesNotExist(JulhaxxUser("erik", "asdf",  mutableListOf(cc1, cc2)))
        //val fredde = userRepository.saveIfUsernameDoesNotExist(JulhaxxUser("fredde", "asdf", mutableListOf(cc3)))
        //val caro = userRepository.saveIfUsernameDoesNotExist(JulhaxxUser("caro", "asdf", mutableListOf()))
        //val matte = userRepository.saveIfUsernameDoesNotExist(JulhaxxUser("matte", "asdf", mutableListOf()))
        //userRepository.saveIfUsernameDoesNotExist(JulhaxxUser("daniel", "asdf", mutableListOf()))

        fakeUsers.forEach { userRepository.saveIfUsernameDoesNotExist(it) }
        realUsers.forEach { userRepository.saveIfUsernameDoesNotExist(it) }

        val usersAfter = userRepository.count()
        println("Num new users" + (usersAfter - usersBefore))
        println("Total num users" + usersAfter)
    }

}

fun UserRepository.saveIfUsernameDoesNotExist(user: JulhaxxUser) {
    if (findByUsername(user.username) == null) {
        save(user)
    }
}

// NOTE: NEVER EVER CHANGE THESE LISTS WITHOUT ALSO FIXING THE DB!!! DUPLICATE USERS = BAD
val fakeUsers = listOf(
        JulhaxxUser("erik-nisse", "#yolo#", mutableListOf())
)
// NOTE: NEVER EVER CHANGE THESE LISTS WITHOUT ALSO FIXING THE DB!!! DUPLICATE USERS = BAD
val realUsers = listOf(
        JulhaxxUser("agixel", "phdochdink", mutableListOf()),
        JulhaxxUser("evedario", "levelmario", mutableListOf()),
        JulhaxxUser("morrisaga", "knorrisdaga", mutableListOf()),
        JulhaxxUser("familjenkarlsson", "supergrannar", mutableListOf()),
        JulhaxxUser("handre", "alejandre", mutableListOf()),
        JulhaxxUser("josselassen", "mumseglassen", mutableListOf()),
        JulhaxxUser("henriksenia", "tysksvenska", mutableListOf()),
        JulhaxxUser("tomokaxel", "crewjapan", mutableListOf()),
        JulhaxxUser("davidso", "storbritterna", mutableListOf()),
        JulhaxxUser("antoncarro", "klaetteraporna", mutableListOf()),
        JulhaxxUser("jesperanna", "ringfitadventures", mutableListOf()),
        JulhaxxUser("naomicke", "karolinskasjukhuset", mutableListOf()),
        JulhaxxUser("griparna", "selmoden", mutableListOf()),
        JulhaxxUser("erik_deng", "woodenflute", mutableListOf()),
        JulhaxxUser("valentijn", "beardeddutch", mutableListOf()),
        JulhaxxUser("henrike_alex", "uppsalapluppsala", mutableListOf()),
        JulhaxxUser("inno", "math_is_ez", mutableListOf()),
        JulhaxxUser("joelubna", "flickflackradioattack", mutableListOf()),
        JulhaxxUser("asa_kleve", "flywaychampion", mutableListOf()),
        JulhaxxUser("simon_velander", "vadeklockan", mutableListOf()),
        JulhaxxUser("gabriel_vilen", "g_s_a_v_a_g_e", mutableListOf()),
        JulhaxxUser("michel_chamoun", "a_d_y_e_n", mutableListOf()),
        JulhaxxUser("kalle_petersson", "konsultbossman", mutableListOf()),
        JulhaxxUser("victoria_catalan", "komp_etens_madame", mutableListOf()),
        JulhaxxUser("tobias_axelsson", "softahandleder", mutableListOf())
)