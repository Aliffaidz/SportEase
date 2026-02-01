package app.model.request;

import app.exceptions.FixHour;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBookingField {

    @NotBlank(message = "nama harus di isi")
    private String name;

    @Size(max = 12,min = 12,message = "masukan format nomor yang benar")
    @NotBlank(message = "nomor handphone harus di isi")
    private String phoneNumber;

    @NotNull(message = "waktu mulai harus di isi")
    @FixHour
    private LocalTime start;

    @NotNull(message = "waktu selesai harus di isi")
    @FixHour
    private LocalTime ended;

    @NotNull(message = "tanggal booking harus di isi")
    private LocalDate date;

    @NotNull(message = "harga booking harus tersedia")
    private BigDecimal price;

}
