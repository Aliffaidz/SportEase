package app.entities;

import app.dto.LapanganDtoDetail;
import app.dto.admin.FasilitasDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomePage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(name = "image_background")
    private String imageBackground;

    @Column(name = "address",nullable = false)
    private String address;

    @Column(name = "contact",nullable = false)
    private String contact;

    @Column(name = "jam_opersional",nullable = false)
    private String jamOperasional;

    @Embedded
    private SocialMedia socialMedia;


}
