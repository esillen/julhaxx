package se.teamkugel.julhaxx

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HtmlController(val userRepository: UserRepository) {

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
    fun game(model: Model): String {
        model["title"] = "Game"
        val user = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
        model["username"] = user?.username ?: ""
        return "game"
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

}
