package app.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AvailableBookingSlots {
    private LocalDate date;
    private List<BookingSlot> bookingSlotList;
}
