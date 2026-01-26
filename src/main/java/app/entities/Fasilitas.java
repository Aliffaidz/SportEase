package app.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fasilitas")
@Data
@NoArgsConstructor
public class Fasilitas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Lob
    private String deskripsi;

    @OneToMany(mappedBy = "fasilitas",fetch = FetchType.EAGER)
    private List<GambarFasilitas> gambarFasilitas;

    @Column(name = "waktu_ditambahkan",columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime waktuDitambahkan;


}
