package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "gambar_pembayaran")
@NoArgsConstructor
@AllArgsConstructor
public class GambarPembayaran {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "id_pembayaran",referencedColumnName = "id")
    private Pembayaran idPembayaran;



}
