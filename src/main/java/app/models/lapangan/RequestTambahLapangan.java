package app.models.lapangan;

import app.entities.JamOprasional;
import app.entities.enums.JenisLapangan;
import app.entities.enums.StatusLapangan;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RequestTambahLapangan {

    @NotBlank(message = "nama lapangan tidak boleh kosong")
    private String nama;

    @NotNull(message = "jenis lapangan tidak boleh kosong")
    private JenisLapangan jenis;

    @NotNull(message = "jam operasional harus ada")
    private JamOprasional jamOprasional;

    @NotNull(message = "status lapangan tidak boleh kosong")
    private StatusLapangan status;

    @NotNull(message = "harga lapangan tidak boleh kosong")
    @Digits(integer = 10, fraction = 0, message = "Harga harus bilangan bulat")
    private BigDecimal harga;

    @NotBlank(message = "dekskripsi lapangan tidak boleh kososong")
    private String deskripsi;

    @NotBlank(message = "lokasi lapangan tidak boleh kososng")
    private String lokasi;

    @NotNull(message = "jam operasional tidak boleh kosong")
    private LocalDate tanggalOpersional;

    @NotNull(message = "durasi jam booking harus tidak boleh kososng")
    private  Integer maximalWaktuBooking;


}
