package app.models.home_page;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCustomHomePage {

    @NotNull(message = "title tidak boleh kososng")
    private String title;

    @NotNull(message = "alamat tidak boleh kosong")
    private String address;

    @NotNull(message = "nomor contatc tidak boleh kosong")
    private String contact;

    @NotNull(message = "jam opersional gor tidak boleh kosong")
    private String jamOperasional;

    private String facebook;

    private String instagram;

    private String tiktok;


}
