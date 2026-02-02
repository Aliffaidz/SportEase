package app.model.dto;

import app.entities.enums.BookingStatus;
import app.entities.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Data
public class BookingDto {

    private Integer id;
    private String username;
    private LocalTime startTime;
    private LocalTime endedTime;
    private LocalDate date;
    private BigDecimal price;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private String image;


}
