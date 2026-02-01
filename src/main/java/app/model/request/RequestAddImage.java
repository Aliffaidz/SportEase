package app.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAddImage {

    @NotNull(message = "gambar 1 tidak boleh kosong")
    private MultipartFile image1;
    @NotNull(message = "gambar 2 tidak boleh kosong")
    private MultipartFile image2;
    @NotNull(message = "gambar 3 tidak boleh kosong")
    private MultipartFile image3;

}
