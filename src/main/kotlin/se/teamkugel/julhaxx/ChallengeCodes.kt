package se.teamkugel.julhaxx

private val day1ChallengeCodes = mapOf(1 to "thatsreallygreat", 2 to "yayyoudidit", 3 to "finaltaskcompleted")
private val day2ChallengeCodes = mapOf(1 to "coronalol", 2 to "coronanomore", 3 to "verynomore")
private val day3ChallengeCodes = mapOf(1 to "zomgvaccinemade", 2 to "verykewldude", 3 to "omfgdatvasgrejt")
private val day4ChallengeCodes = mapOf(1 to "main", 2 to "froggieboi", 3 to "exceptionx")
private val day5ChallengeCodes = mapOf(1 to "staroftheshow", 2 to "winconditionmet", 3 to "noresistanceplease")
private val day6ChallengeCodes = mapOf(1 to "fladderBasicDFHDHD", 2 to "fladderMediumDRYNY", 3 to "epicFladderShiteXXX")
private val day7ChallengeCodes = mapOf(1 to "asdf", 2 to "lolol", 3 to "asdfasdf")


private val challengeCodesByDay = mapOf(
        1 to day1ChallengeCodes,
        2 to day2ChallengeCodes,
        3 to day3ChallengeCodes,
        4 to day4ChallengeCodes,
        5 to day5ChallengeCodes,
        6 to day6ChallengeCodes,
        7 to day7ChallengeCodes
)

val numChallengesPerDay = challengeCodesByDay.map { (key, value) -> key to value.size}.toMap()

fun codeMatchesChallenge(dayNumber: Int, challengeNumber: Int, challengeCode: String) : Boolean {
    return challengeCodesByDay.get(dayNumber)?.get(challengeNumber) == challengeCode
}
