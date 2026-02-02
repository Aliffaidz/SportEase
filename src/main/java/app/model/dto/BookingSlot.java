package app.model.dto;

import app.entities.enums.BookingStatus;
import lombok.Data;

import java.time.LocalTime;

@Data
public class BookingSlot {

    private LocalTime start;
    private LocalTime ended;
    private BookingStatus status;

}
