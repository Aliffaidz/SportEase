package app.model.dto;
import app.entities.OperationalHours;
import app.entities.enums.FieldStatus;
import app.entities.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class AdminFieldDto {

    private Integer id;
    private String fieldName;
    private BigDecimal hourlyPrice;
    private OperationalHours operationHours;
    private FieldType fieldType;
    private FieldStatus fieldStatus;
    private String location;
    private String description;
    private LocalDate operationDate;
    private Integer maximumBookingHours;
    private List<String> images;


}
