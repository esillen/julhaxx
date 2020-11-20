package se.teamkugel.julhaxx

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@Controller
class HtmlController(val userRepository: UserRepository, val daysRepository: DaysRepository) {

    @GetMapping("/")
    fun blog(model: Model): String {
        model["title"] = "Blog"
        return "blog"
    }

    @GetMapping("/home")
    fun home(model: Model): String {
        model["title"] = "Home"
        return "blog"
    }

    @GetMapping("/game")
    fun game(model: Model, @RequestParam day: String?): String {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user == null) {
            return error(model)
        } else {
            val activeDay = day ?: "0"
            model["title"] = "Game"
            model["username"] = user.username
            model["completion"] = user.completedChallenges
            model["activeDay"] = activeDay

            return "days/day$activeDay"
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
        model["title"] = "Login"
        return "login"
    }

    @GetMapping("/hello")
    fun hello(model: Model): String {
        model["title"] = "Hello"
        return "hello"
    }

    @GetMapping("/error")
    fun error(model: Model): String {
        model["title"] = "Error"
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
            }
            return "Success!!"

        } else {
            //TODO handle this case (wtf case actually)
            return "failure :/"
        }
    }

}
