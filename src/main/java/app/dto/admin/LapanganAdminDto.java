package app.dto.admin;

import app.entities.JamOprasional;
import app.entities.enums.JenisLapangan;
import app.entities.enums.StatusLapangan;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class LapanganAdminDto {

    private Integer id;
    private String name;
    private JenisLapangan jenisLapangan;
    private StatusLapangan statusLapangan;
    private BigDecimal hargaPerjam;
    private JamOprasional jamOprasional;
    private String lokasi;
    private String deskripsi;
    private LocalDate tanggalOperasional;
    private Integer maximalWaktuBooking;
    private List<String> gambar;


    public LapanganAdminDto(Integer id, String name, JenisLapangan jenisLapangan, StatusLapangan statusLapangan, BigDecimal hargaPerjam,JamOprasional jamOprasional, String lokasi, String deskripsi,LocalDate tanggalOperasional, Integer maximalWaktuBooking,List<String> gambar) {
        this.id = id;
        this.name = name;
        this.jenisLapangan = jenisLapangan;
        this.statusLapangan = statusLapangan;
        this.hargaPerjam = hargaPerjam;
        this.jamOprasional = jamOprasional;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.tanggalOperasional = tanggalOperasional;
        this.maximalWaktuBooking = maximalWaktuBooking;
        this.gambar = gambar;
    }





}
