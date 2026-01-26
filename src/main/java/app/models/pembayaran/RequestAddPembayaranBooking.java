package app.models.pembayaran;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddPembayaranBooking {

    private Integer idBooking;
    private Integer idLapangan;
    private String username;
    private String noHandphone;
    private LocalDateTime waktuBooking;
}
