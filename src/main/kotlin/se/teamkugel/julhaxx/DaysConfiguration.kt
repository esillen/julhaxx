package se.teamkugel.julhaxx

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
class DaysConfiguration(daysRepository: DaysRepository) {
    init {
        daysRepository.save(Day(1, true, "Lussebulle"))
        daysRepository.save(Day(2, true, "Tomte"))
        daysRepository.save(Day(3, true, "Getabock"))
        daysRepository.save(Day(4, true, "Baby Jesus"))
        daysRepository.save(Day(5, false, "Baby Jesus"))
        daysRepository.save(Day(6, false, "Baby Jesus"))
        daysRepository.save(Day(7, false, "Baby Jesus"))
    }

}