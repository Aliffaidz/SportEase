package app.controllers;

import app.model.dto.Response;
import app.model.request.RequestBookingField;
import app.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


}
