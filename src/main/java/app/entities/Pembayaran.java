package app.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "data_pembayaran")
public class Pembayaran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_booking")
    private Integer idBooking;

    @ManyToOne
    @JoinColumn(name = "id_lapangan", referencedColumnName = "id")
    private Lapangan lapangan;

    @Column(name = "username",nullable = false)
    private String username;

    @Column(name = "no_handphone",nullable = false,length = 12)
    private String noHandphone;

    @Column(name = "waktu_booking",nullable = false)
    private LocalDate waktuBooking;

    @OneToOne(mappedBy = "idPembayaran")
    private GambarPembayaran gambarPembayaran;
}
