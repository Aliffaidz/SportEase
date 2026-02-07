package app.model.dto;

import java.math.BigDecimal;

public record BookingSummaryDashboard(
         Integer bookings,
         BigDecimal revenue,
         Integer cancel,
         Integer alreadyPay,
         Integer waitingForConfirmation
){
}
