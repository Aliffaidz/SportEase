package app.dto;

import app.entities.enums.JenisLapangan;
import app.entities.enums.StatusBoking;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingDto {
    private Integer id;
    private Integer idField;
    private String namaLapangan;
    private String usernameBooking;
    private JenisLapangan jenisLapangan;
    private StatusBoking statusBoking;
    private LocalDateTime waktuMulai;
    private LocalDateTime waktuSelesi;


    public BookingDto(Integer id, Integer idField, String namaLapangan, String usernameBooking, JenisLapangan jenisLapangan, StatusBoking statusBoking, LocalDateTime waktuMulai, LocalDateTime waktuSelesi) {
        this.id = id;
        this.idField = idField;
        this.namaLapangan = namaLapangan;
        this.usernameBooking = usernameBooking;
        this.jenisLapangan = jenisLapangan;
        this.statusBoking = statusBoking;
        this.waktuMulai = waktuMulai;
        this.waktuSelesi = waktuSelesi;
    }

    public BookingDto(String namaLapangan, String usernameBooking, JenisLapangan jenisLapangan, StatusBoking statusBoking, LocalDateTime waktuMulai, LocalDateTime waktuSelesi) {
        this.namaLapangan = namaLapangan;
        this.usernameBooking = usernameBooking;
        this.jenisLapangan = jenisLapangan;
        this.statusBoking = statusBoking;
        this.waktuMulai = waktuMulai;
        this.waktuSelesi = waktuSelesi;
    }

    public BookingDto(Integer idField, String namaLapangan, String usernameBooking, JenisLapangan jenisLapangan, StatusBoking statusBoking, LocalDateTime waktuMulai, LocalDateTime waktuSelesi) {
        this.idField = idField;
        this.namaLapangan = namaLapangan;
        this.usernameBooking = usernameBooking;
        this.jenisLapangan = jenisLapangan;
        this.statusBoking = statusBoking;
        this.waktuMulai = waktuMulai;
        this.waktuSelesi = waktuSelesi;
    }
}

