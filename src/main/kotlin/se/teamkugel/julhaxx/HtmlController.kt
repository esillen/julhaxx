package se.teamkugel.julhaxx

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.time.LocalDateTime
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class HtmlController(val userRepository: UserRepository,
                     val daysRepository: DaysRepository,
                     val storyCompiler: StoryCompiler,
                     val persistedChatMessageRepository: PersistedChatMessageRepository,
                     val webSocketsController: WebSocketsController,  /*Uuuuh this is ugly. But how else to solve it?*/) : ErrorController {

    @GetMapping("/")
    fun index(model: Model): String {
        return game(model, null)
    }

    @GetMapping("/game")
    fun game(model: Model, @RequestParam day: Int?): String {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user == null) {
            return errorMessage(model, "Någonting är fel med din inloggning :/")
        } else {
            val activeDay = day ?: daysRepository.lastAvailableDay()?.number ?: 0 // To make /game ending up at current day
            if (activeDay == 0 || daysRepository.findByIdOrNull(activeDay)?.available == true) {
                model["title"] = "Dag $activeDay"
                model["user"] = user
                model["activeDay"] = activeDay
                model["story"] = storyCompiler.daysToStoryHtml[activeDay]!!
                model["emojis"] = EMOJIS
                model["chatHistory"] = WebSocketsController.savedMessagesQueue.map{ it.toChatMessage(userRepository)}.toTypedArray()
                addTopRowDays(model, activeDay)
                addJulhaxx(model);
                return "days/day$activeDay"
            } else {
                return errorMessage(model, "Den där dagen finns la inte :/")
            }
        }
    }

    fun DaysRepository.lastAvailableDay(): Day? {
        return this.findAll().sortedByDescending { it.number }.firstOrNull { it.available }
    }

    private fun updateDays() {
        val currentActiveDayNumber = Duration.between(START_DATE.atTime(0,1), LocalDateTime.now()).toDays() + 1
        val days = daysRepository.findAll()
        days.forEach { day ->
            if (day.number <= currentActiveDayNumber) {
                if (!day.available) {
                    day.available = true
                }
            } else {
                if (day.available) {
                    day.available = false
                }
            }
        }
        daysRepository.saveAll(days)
    }

    private fun addTopRowDays(model: Model, activeDay: Int = -1) {
        updateDays()
        model["topRowDays"] = daysRepository.findAll()
                .sortedBy { it.number }
                .map {
            TopRowDay(it.number,
                    it.title,
                    it.available,
                    it.number == activeDay)
        }
    }

    private fun addJulhaxx(model: Model) {
        val team = listOf("Erik S", "Fredrik C", "Mattias S", "Daniel S").shuffled()
        model["julhaxx"] = "${team[0]}, ${team[1]}, ${team[2]} och sist men inte minst ${team[3]}"
    }

    @GetMapping("/leaderboard")
    fun leaderboard(model: Model): String {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user == null) {
            return errorMessage(model, "Någonting är fel med din inloggning :/")
        } else {
            model["user"] = user
            val users = userRepository.findAll()
            model["users"] = users.map {
                LeaderboardUser(it.username, it.completedChallenges.size)
            }.sortedByDescending { it.numStars }
            model["title"] = "Topplista"
            addTopRowDays(model)
            addJulhaxx(model);
            return "leaderboard"
        }
    }

    val NUMBER_OF_GROUPS = 3
    @GetMapping("/manuals/{manualid}")
    fun manual(model: Model, @PathVariable manualid: String) : String {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        val userId = user?.id
        if (userId == null) {
            return errorMessage(model, "Någonting är fel med din inloggning :/")
        } else {
            model["group${userId % NUMBER_OF_GROUPS}"] = true
            return "manuals/$manualid"
        }
    }

    @GetMapping("/user/{inspectedUserUsername}")
    fun userProfile(model: Model, @PathVariable inspectedUserUsername: String): String {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        val inspectedUser = userRepository.findByUsername(inspectedUserUsername)
        if (user == null) {
            return errorMessage(model, "Något är fel med din inloggning :/")
        } else if (inspectedUser == null) {
            return errorMessage(model, "Den användaren verkar inte finnas???")
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
            addJulhaxx(model);
            return "user"
        }
    }

    @GetMapping("/longchat")
    fun longchat(model: Model) : String {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user == null) {
            return errorMessage(model, "Något är fel med din inloggning :/")
        } else {
            model["title"] = "Chatlogg"
            model["user"] = user!!
            addTopRowDays(model)
            addJulhaxx(model)
            // Not that performant but whatevz
            model["chatHistory"] = persistedChatMessageRepository.findAll().map { it.toChatMessageSimple() }
            return "longchat"
        }
    }


    @PostMapping("/completeChallenge")
    @ResponseBody
    fun completeChallenge(model: Model, @RequestParam day: Int, @RequestParam challengeNumber: Int, @RequestParam code: String) {
        val user = userRepository.findByUsernameForChallengeUpdate(SecurityContextHolder.getContext().authentication.name)
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
        } else if (!EMOJIS.contains(emoji)) {
            throw Exception("dålig emoji")
        } else {
            user.emoji = emoji
            userRepository.save(user)
        }
        response.sendRedirect("/user/${user.username}")
    }

    @GetMapping("/login")
    fun login(model: Model, @RequestParam error: Boolean?): String {
        model["title"] = "Logga in"
        model["error"] = error ?: false
        addJulhaxx(model);
        return "login"
    }

    private fun errorMessage(model: Model, errorMessage: String): String {
        model["title"] = "Fel fel fel"
        model["errorMessage"] = errorMessage
        addJulhaxx(model);
        return "error"
    }

    @RequestMapping("/error")
    fun error(model: Model, request: HttpServletRequest): String {
        model["title"] = "Fel fel fel"
        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
        if (status != null) {
            val statusCode = Integer.valueOf(status.toString())
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model["title"] = "Sidan kunde inte hittas"
                model["errorMessage"] = "Sidan kunde inte hittas"
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model["errorMessage"] = "Internt fel :("
            }
        }
        addJulhaxx(model);
        return "error"
    }

	@PostMapping("/fladder")
    @ResponseBody
    fun postFladderUpdate(model: Model, @RequestBody data: String) {
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        if (user == null) {
            throw Exception("user not found or something")
        } else {
        	System.out.println("Got fladder data: ${data}")
			val distance = data.substring("distance: ".length).toInt()
			if(distance >= 30) {
				completeChallenge(model, 6, 1, "fladderBasicDFHDHD")
			}
			if(distance >= 50) {
				completeChallenge(model, 6, 2, "fladderMediumDRYNY")
			}
			if(distance >= 100) {
				completeChallenge(model, 6, 3, "epicFladderShiteXXX")
			}
        }
    }

    override fun getErrorPath(): String {
        return "/error"
    }
}
