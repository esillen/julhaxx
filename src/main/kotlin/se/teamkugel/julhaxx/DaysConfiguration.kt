package se.teamkugel.julhaxx

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
class DaysConfiguration(daysRepository: DaysRepository) {
    init {
        daysRepository.save(Day(1, "Lussebulle"))
        daysRepository.save(Day(2, "Tomte"))
        daysRepository.save(Day(3, "Getabock"))
        daysRepository.save(Day(4, "Baby Jesus"))
    }

}