package app.model.request;

import app.entities.SocialMedia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestEditHomePage {

    private String title;
    private String address;
    private String contact;
    private String operationalHours;
    private String facebook;
    private String instagram;
    private String tiktok;
}
