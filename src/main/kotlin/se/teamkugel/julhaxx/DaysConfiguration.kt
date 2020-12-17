package se.teamkugel.julhaxx

import org.springframework.context.annotation.Configuration

@Configuration
class DaysConfiguration (daysRepository: DaysRepository) {
    init {
        daysRepository.save(Day(1, "18 Dec",false))
        daysRepository.save(Day(2, "19 Dec",false))
        daysRepository.save(Day(3, "20 Dec",false))
        daysRepository.save(Day(4, "21 Dec", false))
        daysRepository.save(Day(5, "22 Dec", false))
        daysRepository.save(Day(6, "Dan före", false))
        daysRepository.save(Day(7, "Julafton", false))
    }
}


// No longer used. code updates day availability automatically
fun DaysRepository.saveIfDoesNotExist(day: Day) {
    if (this.findByNumber(day.number) == null) {
        this.save(day)
    }
}
