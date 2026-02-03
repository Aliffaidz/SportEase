package app.model.dto;

import app.entities.PaymentMethod;
import app.entities.Provision;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class BookingFormData {

    private Integer idField;
    private String fieldName;
    private BigDecimal hourlyPrice;
    private LocalDate rangeBookingDate;
    private Integer maximumBookingTime;
    private AvailableBookingSlots availableBookingSlots;
    private List<PaymentMethod> paymentMethods;
    private List<Provision> provisions;

}
