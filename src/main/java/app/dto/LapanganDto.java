package app.dto;

import app.entities.JamOprasional;
import app.entities.enums.JenisLapangan;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class LapanganDto {

    private Integer id;
    private String fieldName;
    private JenisLapangan jenisLapangan;
    private BigDecimal price;
    private String image;
    private JamOprasional jamOprasional;
    private LocalDate tanggalBeroperasi;
    private Integer maximalWaktuBooking;
    private List<BookingTime> bookingTimes;

    public LapanganDto(Integer id, String fieldName, JenisLapangan jenisLapangan, BigDecimal price, String image,JamOprasional jamOprasional) {
        this.id = id;
        this.fieldName = fieldName;
        this.jenisLapangan = jenisLapangan;
        this.price = price;
        this.image = image;
        this.jamOprasional = jamOprasional;
    }

}
