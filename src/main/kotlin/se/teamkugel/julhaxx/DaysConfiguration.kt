package se.teamkugel.julhaxx

import org.springframework.context.annotation.Configuration

@Configuration
class DaysConfiguration (daysRepository: DaysRepository) {
    init {
        daysRepository.save(Day(1, true))
        daysRepository.save(Day(2, true))
        daysRepository.save(Day(3, true))
        daysRepository.save(Day(4, true))
        daysRepository.save(Day(5, true))
        daysRepository.save(Day(6, true))
        daysRepository.save(Day(7, false))
    }

}
