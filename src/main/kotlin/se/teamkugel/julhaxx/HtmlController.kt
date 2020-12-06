package se.teamkugel.julhaxx

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import javax.servlet.http.HttpServletResponse

@Controller
class HtmlController(val userRepository: UserRepository,
                     val daysRepository: DaysRepository,
                     val storyCompiler: StoryCompiler,
                     val webSocketsController: WebSocketsController /*Uuuuh this is ugly. But how else to solve it?*/) {

    @GetMapping("/")
    fun index(model: Model): String {
        return game(model, 0)
    }

    @GetMapping("/game")
    fun game(model: Model, @RequestParam day: Int?): String {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user == null) {
            return error(model)
        } else {
            val activeDay = day ?: 0 // To make /game ending up at day 0
            if (activeDay == 0 || daysRepository.findByIdOrNull(activeDay)?.available == true) {
                model["title"] = "Dag $activeDay"
                model["user"] = user
                model["activeDay"] = activeDay
                model["story"] = storyCompiler.daysToStoryHtml[activeDay]!!
                model["emojis"] = EMOJIS
                model["chatHistory"] = WebSocketsController.savedMessagesQueue.map{ it.toChatMessage(userRepository)}.toTypedArray()
                addTopRowDays(model, activeDay)
                return "days/day$activeDay"
            } else {
                return error(model)
            }
        }
    }

    private fun addTopRowDays(model: Model, activeDay: Int = -1) {
        model["topRowDays"] = daysRepository.findAll()
                .sortedBy { it.number }
                .map {
            TopRowDay(it.number, it.available, it.number==activeDay)
        }
    }

    @GetMapping("/leaderboard")
    fun leaderboard(model: Model): String {
        val users = userRepository.findAll()
        model["users"] = users.map {
            LeaderboardUser(it.username, it.completedChallenges.size)
        }.sortedByDescending { it.numStars }
        model["title"] = "Topplista"
        return "leaderboard"
    }

    val NUMBER_OF_GROUPS = 3
    @GetMapping("/manuals/{manualid}")
    fun manual(model: Model, @PathVariable manualid: String) : String {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        val userId = user?.id
        if (userId == null) {
            return error(model)
        } else {
            model["group${userId % NUMBER_OF_GROUPS}"] = true
            return "manuals/$manualid"
        }
    }

    @GetMapping("/user/{inspectedUserUsername}")
    fun userProfile(model: Model, @PathVariable inspectedUserUsername: String): String {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        val inspectedUser = userRepository.findByUsername(inspectedUserUsername)
        if (user == null || inspectedUser == null) {
            return error(model)
        } else {
            model["title"] = "${inspectedUserUsername}s profil"
            model["user"] = user
            model["inspectedUser"] = InspectedUser(inspectedUser.username,
                    inspectedUser.completedChallenges.size,
                    inspectedUser.emoji,
                    inspectedUser.getCompletedChallengesPerDay())
            if (user.username == inspectedUserUsername) {
                model["emojis"] = EMOJIS
                model["isCurrentUser"] = true
            }
            addTopRowDays(model)
            return "user"
        }
    }


    @PostMapping("/completeChallenge")
    @ResponseBody
    fun completeChallenge(model: Model, @RequestParam day: Int, @RequestParam challengeNumber: Int, @RequestParam code: String) {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user == null) {
            throw Exception("user not found or something")
        } else {
            if (codeMatchesChallenge(day, challengeNumber, code)) {
                if (user.hasCompletedChallenge(day, challengeNumber)) {
                    // Do nothing. User has already completed the challenge
                    // TODO: add support for updating high-score.
                } else {
                    user.completedChallenges.add(CompletedChallenge(day, challengeNumber))
                    userRepository.save(user)
                    webSocketsController.sendCompletedMessage(user, day, challengeNumber)
                }
            } else {
                System.out.println("User ${user.username} submitted bad challenge code. day: $day, challengeNumber: $challengeNumber, code:$code")
            }
        }
    }

    @PostMapping("/emoji")
    @ResponseBody
    fun selectEmoji(model: Model, @RequestParam emoji: String, response: HttpServletResponse) {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user == null) {
            throw Exception("user not found or something")
        } else {
            user.emoji = emoji // TODO: Make it impossible to hack?
            userRepository.save(user)
        }
        response.sendRedirect("/user/${user.username}")
    }

    @GetMapping("/login")
    fun login(model: Model): String {
        model["title"] = "Logga in"
        return "login"
    }

    @GetMapping("/error")
    fun error(model: Model): String {
        model["title"] = "Fel fel fel"
        return "error"
    }

}
