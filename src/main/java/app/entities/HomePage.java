package app.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    @Column(name = "operation_hours",nullable = false)
    private String operationHours;

    @Embedded
    private SocialMedia socialMedia;


}
