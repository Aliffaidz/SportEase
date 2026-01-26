package app.models.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RequestBookingLapangan {


    @NotBlank(message = "username harus tersedia")
    private String username;

    @NotBlank(message = "nomorHandphone harus tersedia")
    @Size(min = 12,message = "nomor handphone harus sesuai")
    private String handPhoneNumber;

    @NotNull(message = "waktu booking tidak boleh kosong")
    private LocalDateTime startTime;

    @NotNull(message = "wakti booking berakhir tidak boleh kososng")
    private LocalDateTime endedTime;

    @NotNull(message = "id lapangan harus tersedia")
    private Integer idField;

}
