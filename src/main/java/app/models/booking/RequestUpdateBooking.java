package app.models.booking;

import app.entities.enums.StatusBoking;
import app.entities.enums.StatusPembayaran;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestUpdateBooking {

    @NotNull(message = "id booking tidak boleh kososng")
    private Integer idBooking;
    @NotNull(message = "id lapangan tidak boleh kosong")
    private Integer idField;

    private String username;
    private String handPhone;
    private LocalDateTime waktuMulai;
    private LocalDateTime waktuSelesai;
    private StatusBoking statusBoking;
    private StatusPembayaran statusPembayaran;

}
