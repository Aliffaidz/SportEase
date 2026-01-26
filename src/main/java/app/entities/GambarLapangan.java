package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "gambar_lapangan")
@Data
@AllArgsConstructor
public class GambarLapangan {

    public GambarLapangan(){}

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_lapangan",referencedColumnName = "id")
    private Lapangan lapanganGambar;

}
