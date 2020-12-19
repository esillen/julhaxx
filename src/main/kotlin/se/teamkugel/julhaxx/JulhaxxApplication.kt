package se.teamkugel.julhaxx

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@ServletComponentScan
@SpringBootApplication
class JulhaxxApplication

fun main(args: Array<String>) {
	runApplication<JulhaxxApplication>(*args)
}
