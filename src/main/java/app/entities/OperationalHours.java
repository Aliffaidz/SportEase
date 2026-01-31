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
public class OperationalHours {



    @NotNull(message = "waktu mulai lapangan tidak boleh kosong")
    @Column(name = "start_time",nullable = false)
    private LocalTime startTime;

    @NotNull(message = "waktu selesai lapangan tidak boleh kososng")
    @Column(name = "ended_time",nullable = false)
    private LocalTime endedTime;

}
