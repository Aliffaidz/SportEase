package app.model.request;

import app.entities.SocialMedia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCustomHomePage {

    private String title;
    private String address;
    private String contact;
    private String operationalHours;
    private SocialMedia socialMedia;

}
