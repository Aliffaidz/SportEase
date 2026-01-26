package app.dto;

import app.entities.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingTime {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endedTime;
    private BookingStatus bookingStatus;


}
