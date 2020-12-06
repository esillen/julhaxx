package se.teamkugel.julhaxx

private val day0ChallengeCodes = mapOf(1 to "goodstuff")
private val day1ChallengeCodes = mapOf(1 to "thatsreallygreat", 2 to "yayyoudidit", 3 to "finaltaskcompleted")
private val day2ChallengeCodes = mapOf(1 to "asdf", 2 to "lolol", 3 to "asdfasdf")
private val day3ChallengeCodes = mapOf(1 to "asdf", 2 to "lolol", 3 to "asdfasdf")
private val day4ChallengeCodes = mapOf(1 to "asdf", 2 to "lolol", 3 to "asdfasdf")
private val day5ChallengeCodes = mapOf(1 to "staroftheshow", 2 to "winconditionmet", 3 to "noresistanceplease")
private val day6ChallengeCodes = mapOf(1 to "asdf", 2 to "lolol", 3 to "asdfasdf")
private val day7ChallengeCodes = mapOf(1 to "asdf", 2 to "lolol", 3 to "asdfasdf")


private val challengeCodesByDay = mapOf(
        0 to day0ChallengeCodes,
        1 to day1ChallengeCodes,
        2 to day2ChallengeCodes,
        3 to day3ChallengeCodes,
        4 to day4ChallengeCodes,
        5 to day5ChallengeCodes,
        6 to day6ChallengeCodes,
        7 to day7ChallengeCodes
)

fun codeMatchesChallenge(dayNumber: Int, challengeNumber: Int, challengeCode: String) : Boolean {
    return challengeCodesByDay.get(dayNumber)?.get(challengeNumber) == challengeCode
}
