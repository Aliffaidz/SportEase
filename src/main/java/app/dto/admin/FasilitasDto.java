package app.dto.admin;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class FasilitasDto {

    private Integer id;
    private String title;
    private String description;
    private List<String> gambarFasilitas;

    public FasilitasDto(Integer id,String title, String description, List<String> gambarFasilitas) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.gambarFasilitas = gambarFasilitas;
    }


}
