package app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class JamOprasional {

    @NotNull(message = "waktu mulai lapangan tidak boleh kosong")
    @Column(name = "waktu_mulai",nullable = false)
    private LocalTime waktuMulai;

    @NotNull(message = "waktu selesai lapangan tidak boleh kososng")
    @Column(name = "waktu_selesai",nullable = false)
    private LocalTime waktuSelesai;

}
