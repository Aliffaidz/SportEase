package app.controllers;

import app.dto.BookingTime;
import app.dto.HomeDto;
import app.dto.LapanganDto;
import app.entities.Booking;
import app.models.booking.RequestUpdateBooking;
import app.services.BookingService;
import app.services.HomePageService;
import app.services.LapanganService;
import app.services.PembayaranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;

@RestController
public class ControllerTest {


    @Autowired
    private BookingService bookingService;
    @Autowired
    private LapanganService lapanganService;
    @Autowired
    private PembayaranService pembayaranService;

    @Autowired
    private HomePageService homePageService;






/*    @GetMapping(path = "api/payments")
    public PagedModel<PembayaranDto> getPembayaran(){
        return pembayaranService.getAll(0,null,null,null,null,null);
    }*/

    @GetMapping(path = "api/testing")
    public PagedModel<Booking> get(){
        return bookingService.getBookingList(0,null,null,null,null,null,null);
    }

    @GetMapping(path = "/api/field")
    public List<LapanganDto> getField(){
        return lapanganService.getField(LocalDate.now());
    }

    @GetMapping(path = "/api/times")
    public List<BookingTime> getBookingTime(){
        return lapanganService.getBookingTimeField(1,LocalDate.now());
    }

    @PatchMapping(path = "/api/update-booking")
    public String updateBooking(@RequestBody RequestUpdateBooking booking){
        bookingService.updateBooking(booking);
        return "berhasil update";
    }

    @DeleteMapping(path = "/api/delete-booking")
    public String deleteBooking(@RequestParam(value = "booking")Integer idBooking,@RequestParam(value = "field")Integer idField){
        bookingService.deleteBooking(idBooking,idField);
        return "berhasil hapus booking";
    }

    @GetMapping(path = "/api/home-test")
    public HomeDto getHomePage(){
        return homePageService.getHome();
    }
}
