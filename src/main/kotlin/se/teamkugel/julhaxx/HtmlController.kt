package se.teamkugel.julhaxx

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@Controller
class HtmlController(val userRepository: UserRepository,
                     val daysRepository: DaysRepository,
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
            model["title"] = "Dag $activeDay"
            model["username"] = user.username
            model["completion"] = user.completedChallenges
            model["numStars"] = user.completedChallenges.size
            model["activeDay"] = activeDay
            model["chatHistory"] = WebSocketsController.savedMessagesQueue.toTypedArray()
            model["topRowDays"] = daysRepository.findAll().map {
                TopRowDay(it.number, it.available, it.number==activeDay)
            }
            return "days/day$activeDay"
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

    @GetMapping("/user/{username}")
    fun userProfile(model: Model, @PathVariable username: String): String {
        val user = userRepository.findByUsername(username)
        if (user == null) {
            return error(model)
        } else {
            model["title"] = "${username}s profil"
            model["userData"] = user
            return "user"
        }
    }


    @PostMapping("/completeChallenge")
    fun completeChallenge(model: Model, @RequestParam day: String?/*TODO not optional*/, @RequestParam code: String?/*TODO not optional*/) {
        // TODO: optionals haxx instead of this mess
        val dayNumber = try {
          day?.toInt() ?: -1
        } catch (e: Exception) {
            -1
        }
        val dayData = daysRepository.findByIdOrNull(dayNumber)
        if (dayData != null && code != null) {
            if (dayData.challengeCode.equals(code)) {
                // Correct

            } else {
                // Wrong
            }
        } else {
            // Something went wrong (bad input, no match in db etc etc..)
        }
    }

    @GetMapping("/login")
    fun login(model: Model): String {
        model["title"] = "Logga in"
        return "login"
    }

    @GetMapping("/error")
    fun error(model: Model): String {
        model["title"] = "Något gick fel :("
        return "error"
    }

    @PostMapping("/code")
    @ResponseBody
    fun verifyCode(@RequestParam day: String, @RequestBody codePayload: String) : String {
        // TODO: verify code
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user != null) {
            val alreadyCompletedChallenge = user.completedChallenges.find { it.day.toString().equals(day) }
            if (alreadyCompletedChallenge != null) {
                // TODO: Update or whatever...
            } else {
                user.completedChallenges.add(CompletedChallenge(day.toInt(), "extra info"))
                userRepository.save(user)
                webSocketsController.sendCompletedMessage(user, day)
            }
            return "Success!!"

        } else {
            //TODO handle this case (wtf case actually)
            return "failure :/"
        }
    }

}
