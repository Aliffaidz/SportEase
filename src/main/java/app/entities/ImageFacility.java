package app.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gambar_fasilitas")
@Data
@NoArgsConstructor
public class ImageFacility extends ImageData {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_facility",referencedColumnName = "id")
    private Facility facility;

}
