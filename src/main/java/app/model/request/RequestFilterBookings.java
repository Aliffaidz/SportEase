package app.model.request;

import app.entities.enums.BookingStatus;
import app.entities.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RequestFilterBookings {

    private Integer id;
    private String username;
    private LocalDate date;
    private BookingStatus status;
    private PaymentStatus payment;
    private String fieldName;
    private Integer page;

}
