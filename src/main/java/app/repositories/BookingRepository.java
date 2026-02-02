package app.repositories;

import app.entities.Booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Integer>, JpaSpecificationExecutor<Booking> {

    @Query("SELECT b FROM Booking b JOIN b.field f WHERE b.date = :date AND f.id = :idField")
    List<Booking> getAvailableBookingSlots(@Param("date") LocalDate date,@Param("idField") Integer idField);

    @Query("SELECT b FROM Booking b WHERE b.date = :date")
    List<Booking> getAllBookingsByDate(@Param("date")LocalDate date);

    Page<Booking> findAllByDate(LocalDate date, Pageable pageable);




}
