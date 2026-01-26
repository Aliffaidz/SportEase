package app.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryBooking {

    private Long totalBookingHariIni;
    private Long totalBookingStatusMenunggu;
    private Long totalBookingStatusSelesai;
    private Long totalBookingStatusSudahBayar;
    private Long totalBookingStatusBelumBayar;
    private Long totalPendapatanHariIni;
    private Long totalLapangaAktif;

    public SummaryBooking(Long totalBookingHariIni, Long totalBookingStatusMenunggu,Long totalBookingStatusSelesai, Long totalBookingStatusSudahBayar, Long totalBookingStatusBelumBayar, Long totalPendapatanHariIni) {
        this.totalBookingHariIni = totalBookingHariIni;
        this.totalBookingStatusMenunggu = totalBookingStatusMenunggu;
        this.totalBookingStatusSelesai = totalBookingStatusSelesai;
        this.totalBookingStatusSudahBayar = totalBookingStatusSudahBayar;
        this.totalBookingStatusBelumBayar = totalBookingStatusBelumBayar;
        this.totalPendapatanHariIni = totalPendapatanHariIni;
    }
}
