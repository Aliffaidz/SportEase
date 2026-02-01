package app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDto {

    private Integer id;
    private String facilityName;
    private String description;
    private List<String> images;

}
