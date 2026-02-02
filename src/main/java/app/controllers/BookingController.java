package app.controllers;

import app.model.dto.AvailableBookingSlots;
import app.model.dto.BookingDto;
import app.model.dto.Response;
import app.model.dto.ResponsePagingData;
import app.model.request.RequestBookingField;
import app.model.request.RequestFilterBookings;
import app.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(
            path = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> bookingField(
            @RequestPart(name = "data") @Valid  RequestBookingField requestBookingField,
            @RequestPart(name = "image") List<MultipartFile> image,
            @PathVariable(name = "id") Integer idField
    )
    {
        return bookingService.bookingField(requestBookingField,image,idField);
    }

    @GetMapping(
            path = "/{id}/booking-slots",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<AvailableBookingSlots> getAvailableBookingSlots(
            @PathVariable(name = "id") Integer idField,
            @RequestParam(name = "date") LocalDate date
            )
    {
        return bookingService.getAvailableSlots(date,idField);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponsePagingData<List<BookingDto>> getBookingsByFilters(@ModelAttribute RequestFilterBookings filterBookings){
        if(filterBookings.getId() == null){
            filterBookings.setPage(0);
        }
        return bookingService.getAllBookings(filterBookings);
    }


}
