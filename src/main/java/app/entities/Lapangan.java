package app.entities;

import app.entities.enums.JenisLapangan;
import app.entities.enums.StatusLapangan;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity

public class Lapangan {

    public Lapangan(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String namaLapangan;

    @Column(nullable = false,precision =  10, scale = 0)
    private BigDecimal hargaLapanganPerjam;

    @Embedded
    private JamOprasional jamOprasional;

    @Column(name = "jenis_lapangan",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private JenisLapangan jenisLapangan;

    @Column(name = "status_lapangan",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusLapangan statusLapangan;

    @Column(columnDefinition = "VARCHAR (255) NOT NULL")
    private String lokasi;

    @Lob
    private String deskripsi;

    @Column(name = "tanggal_berpoerasi",nullable = false)
    private LocalDate tanggalBeroperasi;

    @Column(name = "maximal_waktu_booking",nullable = false)
    private Integer maximalWaktuBooking;

    @JsonIgnore
    @OneToMany(mappedBy = "lapangan",fetch = FetchType.EAGER)
    private List<Pembayaran> pembayaran;

    @JsonIgnore
    @OneToMany(mappedBy = "lapangan",fetch = FetchType.EAGER)
    private List<Booking> bookingList;

    @JsonIgnore
    @OneToMany(mappedBy = "lapanganGambar",fetch = FetchType.EAGER)
    private List<GambarLapangan> gambarLapangan;

}

