package app.repositories;

import app.dto.admin.SummaryBooking;
import app.entities.Booking;
import app.entities.enums.StatusBoking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {

    @Query("SELECT COUNT (b) > 0 FROM Booking b JOIN b.lapangan l WHERE b.username = :username AND CAST(b.waktuMulai AS DATE) = :tanggal AND l.id = :idField")
    boolean cariBerdasarkanUserNameDanIdLapangan(@Param("username") String username,@Param("tanggal")LocalDate tanggal,@Param("idField")Integer idField);

    @Query("""
    SELECT b 
    FROM Booking b 
    JOIN b.lapangan l 
    WHERE l.id = :idField
      AND (b.statusBooking = :status OR b.statusBooking = :secondStatus)
      AND CAST(b.waktuMulai AS DATE) = :tanggal
    ORDER BY 
      CAST(b.waktuMulai AS DATE) ASC,
      b.waktuMulai ASC
""")
    List<Booking> cariBerdasarkanIdLapanganStatusDanTanggal(
            @Param("idField") Integer idField,
            @Param("status") StatusBoking statusBoking,
            @Param("secondStatus") StatusBoking statusSecond,
            @Param("tanggal") LocalDate tanggal
    );

    @Query("SELECT CASE WHEN COUNT (b) > 0 THEN true ELSE false END FROM Booking b WHERE b.lapangan.id = :fieldId AND (:start < b.waktuSelesai AND :end > b.waktuMulai)")
    boolean validasiWaktuBooking(@Param("fieldId")Integer fieldId, @Param("start")LocalDateTime waktuMulai, @Param("end") LocalDateTime waktuSelesai);


    @Query("SELECT new app.dto.admin.SummaryBooking(" +
            "COUNT(b), " +
            "CAST(SUM(CASE WHEN b.statusBooking = 'MENUNGGU' THEN 1 ELSE 0 END) AS Long), " +
            "CAST(SUM(CASE WHEN b.statusBooking = 'SELESAI' THEN 1 ELSE 0 END) AS Long), " +
            "CAST(SUM(CASE WHEN b.statusPembayaran = 'SUDAH_BAYAR' THEN 1 ELSE 0 END) AS Long), " +
            "CAST(SUM(CASE WHEN b.statusPembayaran = 'BELUM_BAYAR' THEN 1 ELSE 0 END) AS Long), " +
            "CAST(SUM(CASE WHEN b.statusPembayaran = 'SUDAH_BAYAR' THEN b.harga ELSE 0 END) AS Long))" +
            "FROM Booking b WHERE CAST(b.waktuMulai AS DATE) = :tanggal")
    SummaryBooking ambilDataSummary(@Param("tanggal") LocalDate localDate);

    @Query("SELECT b FROM Booking b JOIN b.lapangan l WHERE CAST(b.waktuMulai AS DATE) = :tanggal AND b.statusBooking = :status AND l.namaLapangan = :fieldName")
    List<Booking> ambilBookingHariIni(@Param("tanggal") LocalDate tanggal,@Param("status")StatusBoking statusBoking,@Param("fieldName")String fieldName);

    @Query("SELECT b FROM Booking b WHERE CAST(b.waktuMulai AS DATE) = :tanggal AND b.statusBooking = :status")
    List<Booking> ambilBookingHariIniAll(@Param("tanggal") LocalDate tanggal,@Param("status")StatusBoking statusBoking);

    @Query("SELECT b from Booking b JOIN b.lapangan l WHERE b.id = :idBooking AND l.id = :idField")
    Optional<Booking> findFirstByIdAndLapangan(@Param("idBooking") Integer idBooking,@Param("idField") Integer idField);

    @Query("SELECT b FROM Booking b JOIN b.lapangan l WHERE CAST(b.waktuMulai AS DATE) = :date AND l.id = :idField OR b.statusBooking = 'BATAL'")
    List<Booking> findAllByDateAndField(@Param("date")LocalDate date,@Param("idField") Integer idField);

    @Query("SELECT b FROM Booking b WHERE b.statusBooking = 'MENUNGGU' AND b.waktuSelesai <  :date ")
    List<Booking> ambilSemuaBookingHariIni(@Param("date")LocalDateTime tanggal);


}
