package app.models.lapangan;
import app.entities.JamOprasional;
import app.entities.enums.JenisLapangan;
import app.entities.enums.StatusLapangan;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class RequestEditLapangan {

    @Size(min = 3)
    private String nama;

    private JenisLapangan jenis;

    private StatusLapangan status;

    private BigDecimal harga;

    private String deskripsi;

    private String lokasi;

    private JamOprasional jamOprasional;

    private LocalDate tanggalOpersional;

    private List<String> gambarList;

    private  Integer maximalWaktuBooking;

}
