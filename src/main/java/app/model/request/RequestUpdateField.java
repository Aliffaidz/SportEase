package app.model.request;

import app.entities.enums.FieldStatus;
import app.entities.enums.FieldType;
import app.exceptions.NotAfter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
@NoArgsConstructor
public class RequestUpdateField {

    private String fieldName;
    private BigDecimal hourlyPrice;
    private LocalTime startTime;
    @NotAfter(value = "23:00",message = "Maximal atur waktu selesai jam 11")
    private LocalTime endedTime;
    private FieldType fieldType;
    private FieldStatus fieldStatus;
    private String location;
    private String description;
    private Integer maximumBookingHours;
    private LocalDate operationDate;


}
