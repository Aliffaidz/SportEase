package app.models.fasilitas;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RequestEditFasilitas {

    private Integer id;
    private String title;
    private String description;
    private List<String> gambarList;

}
