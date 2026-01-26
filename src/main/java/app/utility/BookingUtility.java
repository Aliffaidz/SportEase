package app.utility;

import app.dto.BookingDto;
import app.entities.Booking;
import app.entities.Lapangan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
public class BookingUtility {

    public void isValidTime(LocalDate tanggal, LocalTime start, LocalTime ended, Lapangan lapangan){

        LocalTime waktuMulai = lapangan.getJamOprasional().getWaktuMulai();
        LocalTime waktuSelesai = lapangan.getJamOprasional().getWaktuSelesai();

        if(waktuMulai.isAfter(start)){
            log.info("waktu tidak valid, input sesuai waktu operasional lapangan");
            messages(HttpStatus.BAD_REQUEST,"waktu mulai tidak valid sesuaikan dengan jam operasioanl");
        }

        if(ended.isAfter(waktuSelesai)){
            log.info("waktu tidak valid, input sesuai waktu operasional lapangan");
            messages(HttpStatus.BAD_REQUEST,"waktu selesai tidak valid sesuaikan dengan jam operasioanl");
        }

        if(!start.isBefore(ended)){
            log.info("waktu tidak valid 1 " + start.toString() + " " + ended.toString());
            messages(HttpStatus.BAD_REQUEST,"waktu mulai tidak valid");
        }

        if(start.isBefore(LocalTime.now())){
            if(!tanggal.isAfter(LocalDate.now())){
                log.info("waktu tidak valid 1 " + start.toString() + " " + ended.toString());
                messages(HttpStatus.BAD_REQUEST,"waktu mulai tidak valid");
            }
        }

        long minutesDuration = Duration.between(start, ended).toMinutes();
        if (minutesDuration < 60 || minutesDuration % 60 != 0) {
            log.info("waktu tidak valid 2");
            messages(HttpStatus.BAD_REQUEST,"durasi booking minimal 1 jam");
        }


        if(!tanggal.isAfter(LocalDate.now())){
            Long range = Duration.between(LocalTime.now(),start).toMinutes();
            if(range < 60){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"waktu booking harus 1 jam sebelum waktu  mulai");
            }
        }

    }

    public void isValidDate(LocalDate start,LocalDate ended,Lapangan lapangan){
        if(!start.equals(ended)){
            log.info("tanggal valid 1");
            messages(HttpStatus.BAD_REQUEST,"tanggal booking tidak valid");
        }

        if(start.isAfter(lapangan.getTanggalBeroperasi())){
            log.info("tanggal valid 2");
            messages(HttpStatus.BAD_REQUEST,"tangal booking melewati tanggal operasional lapangan");
        }
    }

    public BigDecimal getPrice(LocalTime start,LocalTime ended,BigDecimal defaultPrice){
        long minutesDuration = Duration.between(start, ended).toMinutes();
        double hargaPermenit = defaultPrice.doubleValue() / 60;
        double finalPrice = hargaPermenit * minutesDuration;
        int totalHarga = (int) Math.round(finalPrice);
        return BigDecimal.valueOf(totalHarga);
    }

    public List<BookingDto> returnBookingDto(List<Booking> bookingList, Integer idField) {
        return bookingList.stream().map(booking
                        -> new BookingDto(
                        booking.getId(),
                        idField,
                        booking.getLapangan().getNamaLapangan(),
                        booking.getUsername(),
                        booking.getLapangan().getJenisLapangan(),
                        booking.getStatusBooking(),
                        booking.getWaktuMulai(),
                        booking.getWaktuSelesai()
                )
        ).toList();
    }

    public void messages(HttpStatus status, String messages){
        throw new ResponseStatusException(status,messages);
    }
}
