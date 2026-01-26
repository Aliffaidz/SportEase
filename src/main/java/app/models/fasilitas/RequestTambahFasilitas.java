package app.models.fasilitas;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestTambahFasilitas {

    @NotBlank(message = "nama fasilitas tidak boleh kosong")
    private String name;

    @NotBlank(message = "desktipsi fasilitas tidak boleh kososong")
    private String deskripsi;

}
