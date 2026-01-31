package app.model.request;
import app.entities.enums.FieldStatus;
import app.entities.enums.FieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record RequestAddField(

        @NotBlank(message = "nama lapangan tidak boleh kosong")
        String fieldName,

        @NotNull(message = "harga perjam lapangan tidak boleh kosong")
        BigDecimal hourlyPrice,

        @NotNull(message = "waktu operasional mulai lapangan tidak boleh kosong")
        LocalTime startTime,

        @NotNull(message = "waktu operasional selesai lapangan tidak boleh kosong")
        LocalTime endedTime,

        @NotNull(message = "tipe lapangan tidak boleh kosong")
        FieldType fieldType,

        @NotNull(message = "status lapangan tidak boleh kosong")
        FieldStatus fieldStatus,

        @NotBlank(message = "lokasi lapanga tidak boleh kosong")
        String location,

        @NotBlank(message = "deskripsi lapangan tidak boleh kosong")
        String description,

        @NotNull(message = "maximal jam booking tidak boleh kosong")
        Integer maximumBookingHours,

        @NotNull(message = "rentang tanggal booking tidak boleh kosong")
        LocalDate operationDate
) {
}
