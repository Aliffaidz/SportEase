package app.dto;

import app.dto.admin.FasilitasDto;
import app.entities.HomePage;
import app.entities.Lapangan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeDto {

    private HomePage homePage;
    private List<FasilitasDto> fasilitasDtoList;
}
