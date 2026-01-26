package app.models.home_page;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestModifyHomePage {

    private String title;

    private String imageBackground;

    private String address;

    private String contact;

    private String jamOperasional;

    private String facebook;

    private String instagram;

    private String tiktok;


}


