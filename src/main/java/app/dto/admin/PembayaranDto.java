package app.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PembayaranDto {

    private Integer id;
    private Integer idBooking;
    private String fieldName;
    private String username;
    private String noHandphone;
    private LocalDate tanggalBooking;
    private String gambar;


    public PembayaranDto(Integer id, Integer idBooking, String fieldName,String username, String noHandphone, LocalDate tanggalBooking, String gambar) {
        this.id = id;
        this.idBooking = idBooking;
        this.fieldName = fieldName;
        this.username = username;
        this.noHandphone = noHandphone;
        this.tanggalBooking = tanggalBooking;
        this.gambar = gambar;
    }
}
