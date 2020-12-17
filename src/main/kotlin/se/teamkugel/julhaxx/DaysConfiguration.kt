package se.teamkugel.julhaxx

import org.springframework.context.annotation.Configuration

@Configuration
class DaysConfiguration (daysRepository: DaysRepository) {
    init {
        daysRepository.saveIfDoesNotExist(Day(1, "18 Dec",true))
        daysRepository.saveIfDoesNotExist(Day(2, "19 Dec",true))
        daysRepository.saveIfDoesNotExist(Day(3, "20 Dec",true))
        daysRepository.saveIfDoesNotExist(Day(4, "21 Dec", true))
        daysRepository.saveIfDoesNotExist(Day(5, "22 Dec", true))
        daysRepository.saveIfDoesNotExist(Day(6, "Dan före", true))
        daysRepository.saveIfDoesNotExist(Day(7, "Julafton", false))
    }
}

fun DaysRepository.saveIfDoesNotExist(day: Day) {
    if (this.findByNumber(day.number) == null) {
        this.save(day)
    }
}
