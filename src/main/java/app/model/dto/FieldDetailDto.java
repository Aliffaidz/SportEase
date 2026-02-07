package app.model.dto;

import app.entities.OperationalHours;
import app.entities.enums.FieldStatus;
import app.entities.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldDetailDto {

    private Integer id;
    private String fieldName;
    private BigDecimal hourlyPrice;
    private OperationalHours operationalHours;
    private FieldType fieldType;
    private String location;
    private String description;
    private LocalDate operationDate;
    private Integer maximumBookingHours;
}
