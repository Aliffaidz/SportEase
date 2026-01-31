package app.model.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class RequestAddImage {

    @NotNull(message = "gambar 1 tidak boleh kosong")
    private MultipartFile image1;
    @NotNull(message = "gambar 2 tidak boleh kosong")
    private MultipartFile image2;
    @NotNull(message = "gambar 3 tidak boleh kosong")
    private MultipartFile image3;

}
