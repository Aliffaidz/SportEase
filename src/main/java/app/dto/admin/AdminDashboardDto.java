package app.dto.admin;

import app.entities.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDashboardDto {
    private SummaryBooking summaryDto;
    private List<Booking> listBookingHariIni;

}
