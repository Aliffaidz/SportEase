package app.entities;

import app.entities.enums.StatusBoking;
import app.entities.enums.StatusPembayaran;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Booking {

    public Booking(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String username;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String handphone;

    @Column(name = "waktu_mulai",nullable = false)
    private LocalDateTime waktuMulai;

    @Column(name = "waktu_selesai",nullable = false)
    private LocalDateTime waktuSelesai;

    @Column(nullable = false)
    private BigDecimal harga;

    @Column(name = "status_booking",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusBoking statusBooking;

    @Column(name = "status_pembayaran",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusPembayaran statusPembayaran;

    @ManyToOne
    @JoinColumn(name = "id_lapangan",referencedColumnName = "id")
    private Lapangan lapangan;

}
