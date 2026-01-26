package app.dto;

import app.entities.enums.JenisLapangan;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class LapanganDtoDetail {

    private Integer id;
    private String fieldName;
    private JenisLapangan jenisLapangan;
    private BigDecimal price;
    private String location;
    private String description;
    private Integer maximalWaktuBooking;
    private List<String> imagesPath;

    public LapanganDtoDetail(Integer id, String fieldName, JenisLapangan jenisLapangan, BigDecimal price, String location, String description, Integer maximalWaktuBooking ,List<String> imagesPath) {
        this.id = id;
        this.fieldName = fieldName;
        this.jenisLapangan = jenisLapangan;
        this.price = price;
        this.location = location;
        this.description = description;
        this.maximalWaktuBooking = maximalWaktuBooking;
        this.imagesPath = imagesPath;
    }
}
