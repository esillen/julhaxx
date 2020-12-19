package se.teamkugel.julhaxx

import org.springframework.context.annotation.Configuration

@Configuration("userInit")
class UsersConfiguration(userRepository: UserRepository) {
    init {
        System.out.println("running userInit...")
        val usersBefore = userRepository.count()

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
        JulhaxxUser("erik-nisse", "#yolo#", mutableListOf(), "❤️"),
        JulhaxxUser("fredrik-nisse", "Surprise me", mutableListOf(), "❤️"),
        JulhaxxUser("mattias-nisse", "Hitta på nåt ba", mutableListOf(),"❤️"),
        JulhaxxUser("daniel-nisse", "eurytmi", mutableListOf(), "❤️"),
        JulhaxxUser("nisla-nisse", "#yolo#", mutableListOf(), "\uD83D\uDCBB")
)
// NOTE: NEVER EVER CHANGE THESE LISTS WITHOUT ALSO FIXING THE DB!!! DUPLICATE USERS = BAD
val realUsers = listOf(
        JulhaxxUser("caro", "pyskorfv", mutableListOf()),
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
        JulhaxxUser("lasse_sjoblom", "pudgeislove", mutableListOf()),
        JulhaxxUser("simon_velander", "vadeklockan", mutableListOf()),
        JulhaxxUser("gabriel_vilen", "g_s_a_v_a_g_e", mutableListOf()),
        JulhaxxUser("michel_chamoun", "a_d_y_e_n", mutableListOf()),
        JulhaxxUser("kalle_petersson", "konsultbossman", mutableListOf()),
        JulhaxxUser("victoria_catalan", "komp_etens_madame", mutableListOf()),
        JulhaxxUser("tobias_axelsson", "softahandleder", mutableListOf()),
        JulhaxxUser("ank", "spaceyfacey", mutableListOf()),
        JulhaxxUser("mysis", "lurbintheface", mutableListOf()),
        JulhaxxUser("doktor_bjorn", "filosofi", mutableListOf()),
        JulhaxxUser("mag_erik", "prosperity", mutableListOf()),
        JulhaxxUser("lyd_erik", "guilds", mutableListOf()),
        JulhaxxUser("rovbajn", "bengtskans", mutableListOf()),
        JulhaxxUser("jenny", "dinmamma", mutableListOf()),
        JulhaxxUser("udaya", "appleman", mutableListOf()),
        JulhaxxUser("lulu", "baguette", mutableListOf()),
        JulhaxxUser("rakan", "boulder", mutableListOf()),
        JulhaxxUser("emina", "pangpang", mutableListOf()),
        JulhaxxUser("thomas_dem", "adtoox", mutableListOf()),
        JulhaxxUser("danijela", "ekonom", mutableListOf()),
        JulhaxxUser("karl_lind", "rymdskepp", mutableListOf()),
        JulhaxxUser("DrRao", "Powerpuffpinglan", mutableListOf()),
        JulhaxxUser("Mange", "LuddeW", mutableListOf()),
        JulhaxxUser("Fredriko", "Paparazzi", mutableListOf()),
        JulhaxxUser("Evi", "Zeus", mutableListOf()),
        JulhaxxUser("Ariel", "TheMermaid", mutableListOf()),
        JulhaxxUser("Amaru", "CosineSimilarity", mutableListOf()),
        JulhaxxUser("Fehmi", "FriedIceCream", mutableListOf()),
        JulhaxxUser("Clara_David", "James Blunt", mutableListOf()),
        JulhaxxUser("PaulAtreides", "Dune", mutableListOf()),
        JulhaxxUser("MarIlda", "Bratwurst", mutableListOf()),
        JulhaxxUser("SleepingBeauty", "AnneFrank", mutableListOf()),
        JulhaxxUser("Plato", "WoxCox", mutableListOf()),
        JulhaxxUser("DoucheMartin", "SylvesterStallone", mutableListOf()),
        JulhaxxUser("Johannes_Karlsson", "TimeIsOfTheEssence", mutableListOf()),
        JulhaxxUser("Vendela_Asplund", "RedWedding", mutableListOf()),
        JulhaxxUser("Joel_wretborn", "Flipsquad", mutableListOf()),
        JulhaxxUser("Mr1Hand", "GabbeWabbe", mutableListOf()),
        JulhaxxUser("StrapsBoy", "WalleBalle", mutableListOf()),
        JulhaxxUser("Sandra_R", "ViveLaFrance", mutableListOf()),
        JulhaxxUser("Olivia_E", "Pakten", mutableListOf()),
        JulhaxxUser("Olivia_B", "TjockisFest", mutableListOf()),
        JulhaxxUser("PropellenRebellenEllen", "GretaThunberg", mutableListOf()),
        JulhaxxUser("Marie_Mats", "FamiljenCarlsson", mutableListOf()),
        JulhaxxUser("Hanna_Hampus", "Julfest", mutableListOf()),
        JulhaxxUser("Emelie_Karl", "CovidChristmaz", mutableListOf()),
        JulhaxxUser("David_Sanna", "Karlskoga", mutableListOf()),
        JulhaxxUser("Babette", "Hygge", mutableListOf()),
        JulhaxxUser("Jonte_Backis", "RagingRaccoons", mutableListOf()),
        JulhaxxUser("Love_Dahlin", "ItsAllLove", mutableListOf()),
        JulhaxxUser("Melker_Alma", "Cowboy", mutableListOf()),
        JulhaxxUser("Erik_L", "Sneriko", mutableListOf()),
        JulhaxxUser("Linus_F", "VikingBoy", mutableListOf()),
        JulhaxxUser("Joey", "RawFaggot", mutableListOf()),
        JulhaxxUser("Adrian_Z", "Adde", mutableListOf()),
        JulhaxxUser("Youseffi", "CovidMaestro", mutableListOf()),
        JulhaxxUser("Siriann", "Tipsy", mutableListOf()),
        JulhaxxUser("Anton_T", "RagingRaccoons", mutableListOf()),
        JulhaxxUser("Stephanie_L", "Steph", mutableListOf()),
        JulhaxxUser("Nelly_S", "Bait", mutableListOf()),
        JulhaxxUser("Per_Soile", "oasen", mutableListOf()),
        JulhaxxUser("Linnea_Niklas", "arribakiba", mutableListOf()),
        JulhaxxUser("Jennyis", "julkalender", mutableListOf()),
        JulhaxxUser("Viktor_W", "dasskapital", mutableListOf()),
        JulhaxxUser("Fredrik_R", "rullskridskor", mutableListOf()),
        JulhaxxUser("Ernst", "JulMedErnst", mutableListOf()),
        JulhaxxUser("JohanAnka", "FarbrorAnkre", mutableListOf()),
        JulhaxxUser("Sarah_Babo", "Netflix_n_DOW30K", mutableListOf()),
        JulhaxxUser("Chalisa", "storbod", mutableListOf()),
        JulhaxxUser("Jerker_Andrea", "bra_raderat", mutableListOf()),
        JulhaxxUser("Nina_Johan", "stadstunnelbana", mutableListOf()),
        JulhaxxUser("Samuel_M", "modigaresor", mutableListOf()),
        JulhaxxUser("Resi", "svensktysk", mutableListOf()),
        JulhaxxUser("Ellen_A", "ComputerMainBattleTank", mutableListOf()),
        JulhaxxUser("SarahPixel", "julspel", mutableListOf()),
        JulhaxxUser("Alp", "ThugHuelLife", mutableListOf()),
        JulhaxxUser("Erik_Z", "lappsyl", mutableListOf()),
        JulhaxxUser("Martin_Jennifer", "martifer", mutableListOf()),
        JulhaxxUser("Freisa", "secretninja", mutableListOf()),
        JulhaxxUser("Emil_Louise", "beeblebrox", mutableListOf()),
        JulhaxxUser("Fredrikat", "moppekonst", mutableListOf()),
        JulhaxxUser("Emman", "discobadrum", mutableListOf()),
        JulhaxxUser("Ake", "livskonst", mutableListOf()),
        JulhaxxUser("Noran", "guldskogar", mutableListOf()),
        JulhaxxUser("Karin_W", "klimatseger", mutableListOf()),
        JulhaxxUser("Fredrik_J", "frestelse", mutableListOf()),
        JulhaxxUser("Emelie_Joakim", "einzweipolizei", mutableListOf()),
        JulhaxxUser("Linnea_P", "flygplansrobot", mutableListOf()),
        JulhaxxUser("Fredrik_B", "bernsten", mutableListOf()),
        JulhaxxUser("Sofie_B", "ropen_skalla_teams_at_alla", mutableListOf()),
        JulhaxxUser("Gustav_S", "mememachine", mutableListOf()),
        JulhaxxUser("Mikkel", "DerMikkel", mutableListOf()),
        JulhaxxUser("Minna", "sushipoop", mutableListOf()),
        JulhaxxUser("Niktin", "poopview", mutableListOf()),
        JulhaxxUser("LudvigII", "hamnkaptenen", mutableListOf()),
        JulhaxxUser("Kerstin_Gunnar", "speldags", mutableListOf()),
        JulhaxxUser("Gustav_Elinor", "20m_i_tak", mutableListOf()),
        JulhaxxUser("Ingrid_J", "brummbrumm", mutableListOf()),
        JulhaxxUser("Ellioten", "gammelgubbe", mutableListOf()),
        JulhaxxUser("Nora_E", "kamel", mutableListOf()),
        JulhaxxUser("Nadia_D", "baskul", mutableListOf())
)