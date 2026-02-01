package app.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestAddFacility {

    @NotBlank(message = "nama fasilitas harus di isi")
    private String facilityName;
    @NotBlank(message = "deskripsi fasilitas harus di isi")
    private String description;

}

/*
{
"facilityName" : "Kursi",
"description" : "terdapat kursi tunggu"
}
 */