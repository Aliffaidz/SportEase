package app.model.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RequestEditBooking {

    private LocalDate date;
    private LocalTime start;
    private LocalTime ended;
    private BigDecimal price;

}
