package se.teamkugel.julhaxx

import org.springframework.context.annotation.Configuration

@Configuration
class DaysConfiguration (daysRepository: DaysRepository) {
    init {
        daysRepository.saveIfDoesNotExist(Day(1, true))
        daysRepository.saveIfDoesNotExist(Day(2, true))
        daysRepository.saveIfDoesNotExist(Day(3, true))
        daysRepository.saveIfDoesNotExist(Day(4, true))
        daysRepository.saveIfDoesNotExist(Day(5, true))
        daysRepository.saveIfDoesNotExist(Day(6, true))
        daysRepository.saveIfDoesNotExist(Day(7, false))
    }
}

fun DaysRepository.saveIfDoesNotExist(day: Day) {
    if (this.findByNumber(day.number) == null) {
        this.save(day)
    }
}
