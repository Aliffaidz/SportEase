package app.utility;

import app.entities.Field;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Component
public class BookingUtility {

    public boolean isValidTime(LocalTime start,LocalTime ended, Field field){
        if(start.isBefore(field.getOperationHours().getStartTime()) || ended.isAfter(field.getOperationHours().getEndedTime()) || start.equals(ended)) {
            return false;
        }
        long minuteDuration = Duration.between(start,ended).toMinutes();
        if(minuteDuration < 60 || minuteDuration % 60 != 0){
            return false;
        }
        return !start.isAfter(ended);
    }
    public boolean isValidDate(LocalDate date,Field field){
        return !date.isAfter(field.getOperationDate()) && !date.isBefore(LocalDate.now());
    }
    public boolean isValidPrice(LocalTime start, LocalTime ended, BigDecimal price, Field field) {
        long durationHours = java.time.temporal.ChronoUnit.HOURS.between(start, ended);
        if (durationHours <= 0) {
            return false;
        }
        BigDecimal pricePerHour = field.getHourlyPrice();
        BigDecimal expectedPrice = pricePerHour.multiply(BigDecimal.valueOf(durationHours));
        return price != null && price.compareTo(expectedPrice) == 0;
    }
    

}
