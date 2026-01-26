package app.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gambar_fasilitas")
@Data
@NoArgsConstructor
public class GambarFasilitas {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_fasilitas",referencedColumnName = "id")
    private Fasilitas fasilitas;

    public GambarFasilitas(String id, Fasilitas fasilitas) {
        this.id = id;
        this.fasilitas = fasilitas;
    }
}
