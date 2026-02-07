package app.repositories;

import app.entities.Booking;

import app.model.dto.BookingSummaryDashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Integer>, JpaSpecificationExecutor<Booking> {

    @Query("SELECT b FROM Booking b JOIN b.field f WHERE b.date = :date AND f.id = :idField")
    List<Booking> getAvailableBookingSlots(@Param("date") LocalDate date,@Param("idField") Integer idField);

    @Query("SELECT b FROM Booking b WHERE b.date = :date OR b.bookingStatus = 'CANCEL'")
    List<Booking> getAllBookingsByDate(@Param("date")LocalDate date);

    Page<Booking> findAllByDate(LocalDate date, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.field.id = :fieldId " +
            "AND b.date = :date " +
            "AND (:startTime < b.endedTime AND :endedTime > b.startTime)")
    boolean isBookingOverlap(@Param("fieldId") Integer fieldId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endedTime") LocalTime endedTime);


    // query all fields
    @Query("""
    SELECT new app.model.dto.BookingSummaryDashboard(
        CAST(COUNT(b) AS int),
        COALESCE(SUM(CASE WHEN b.paymentStatus = 'SUDAH_BAYAR' THEN b.price ELSE 0 END), 0),
        CAST(SUM(CASE WHEN b.paymentStatus = 'CANCEL' THEN 1 ELSE 0 END) AS int),
        CAST(SUM(CASE WHEN b.paymentStatus = 'SUDAH_BAYAR' THEN 1 ELSE 0 END) AS int),
        CAST(SUM(CASE WHEN b.paymentStatus = 'MENUNGGU_KONFIRMASI' THEN 1 ELSE 0 END) AS int)
    )
    FROM Booking b
    WHERE b.date BETWEEN :startDate AND :endDate""")
    BookingSummaryDashboard getSummaryAllField(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // query field
    @Query("""
    SELECT new app.model.dto.BookingSummaryDashboard(
        CAST(COUNT(b) AS int),
        COALESCE(SUM(CASE WHEN b.paymentStatus = 'SUDAH_BAYAR' THEN b.price ELSE 0 END), 0),
        CAST(SUM(CASE WHEN b.paymentStatus = 'CANCEL' THEN 1 ELSE 0 END) AS int),
        CAST(SUM(CASE WHEN b.paymentStatus = 'SUDAH_BAYAR' THEN 1 ELSE 0 END) AS int),
        CAST(SUM(CASE WHEN b.paymentStatus = 'MENUNGGU_KONFIRMASI' THEN 1 ELSE 0 END) AS int)
    )
    FROM Booking b JOIN b.field l
    WHERE b.date BETWEEN :startDate AND :endDate AND l.fieldName = :fieldName""")
    BookingSummaryDashboard getSummaryWithFieldName(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,@Param("fieldName") String fieldName);

    // query all fields with current day default
    @Query("""
        SELECT new app.model.dto.BookingSummaryDashboard(
            CAST(COUNT(b) AS int),
            COALESCE(SUM(CASE WHEN b.paymentStatus = 'SUDAH_BAYAR' THEN b.price ELSE 0 END), 0),
            CAST(SUM(CASE WHEN b.bookingStatus = 'CANCEL' THEN 1 ELSE 0 END) AS int),
            CAST(SUM(CASE WHEN b.paymentStatus = 'SUDAH_BAYAR' THEN 1 ELSE 0 END) AS int),
            CAST(SUM(CASE WHEN b.paymentStatus = 'MENUNGGU_KONFIRMASI' THEN 1 ELSE 0 END) AS int)
        )
        FROM Booking b
        WHERE b.date = :startDate""")
    BookingSummaryDashboard getTodaySummaryAllField(@Param("startDate") LocalDate startDate);

    // query current field
    @Query("""
        SELECT new app.model.dto.BookingSummaryDashboard(
            CAST(COUNT(b) AS int),
            COALESCE(SUM(CASE WHEN b.paymentStatus = 'SUDAH_BAYAR' THEN b.price ELSE 0 END), 0),
            CAST(SUM(CASE WHEN b.bookingStatus = 'CANCEL' THEN 1 ELSE 0 END) AS int),
            CAST(SUM(CASE WHEN b.paymentStatus = 'SUDAH_BAYAR' THEN 1 ELSE 0 END) AS int),
            CAST(SUM(CASE WHEN b.paymentStatus = 'MENUNGGU_KONFIRMASI' THEN 1 ELSE 0 END) AS int)
        )
        FROM Booking b JOIN b.field l
        WHERE b.date = :startDate AND l.fieldName = :fieldName""")
    BookingSummaryDashboard getTodaySummaryField(@Param("startDate") LocalDate startDate, @Param("fieldName") String fieldName);









}
