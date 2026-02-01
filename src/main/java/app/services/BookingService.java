package app.services;

import app.entities.Booking;
import app.entities.Field;
import app.entities.enums.BookingStatus;
import app.entities.enums.PaymentStatus;
import app.model.dto.Response;
import app.model.request.RequestBookingField;
import app.repositories.BookingRepository;
import app.repositories.FieldRepository;
import app.utility.BookingUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Slf4j
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingUtility bookingUtility;
    private final FieldRepository fieldRepository;
    private final PaymentService paymentService;

    public BookingService(BookingRepository bookingRepository, BookingUtility bookingUtility, FieldRepository fieldRepository, PaymentService paymentService){
        this.bookingRepository = bookingRepository;
        this.bookingUtility = bookingUtility;
        this.fieldRepository = fieldRepository;
        this.paymentService = paymentService;
    }

    public Response<String> bookingField(RequestBookingField requestBookingField, List<MultipartFile> image , Integer idField){
        log.info("service booking bookingField");
        Field field = fieldRepository.findById(idField).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"field with id " + idField + " not found !!!")
        );
        try{
            if(bookingUtility.isValidDate(requestBookingField.getDate(),field) && bookingUtility.isValidTime(requestBookingField.getStart(),requestBookingField.getEnded(),field) && bookingUtility.isValidPrice(requestBookingField.getStart(),requestBookingField.getEnded(),requestBookingField.getPrice(),field)){
                Booking booking = new Booking();
                booking.setUsername(requestBookingField.getName());
                booking.setMobilePhoneNumber(requestBookingField.getPhoneNumber());
                booking.setBookingStatus(BookingStatus.BOOKED);
                booking.setField(field);
                booking.setPrice(requestBookingField.getPrice());
                booking.setStartTime(requestBookingField.getStart());
                booking.setEndedTime(requestBookingField.getEnded());
                booking.setDate(requestBookingField.getDate());
                booking.setPaymentStatus(PaymentStatus.BELUM_BAYAR);
                bookingRepository.save(booking);
                paymentService.addPaymentVerification(booking,image);
                log.info("service booking response");
                return Response.<String>builder()
                        .messages("success")
                        .data("success booking")
                        .build();
            }
        }catch (ResponseStatusException e){
            throw new ResponseStatusException(e.getStatusCode(),e.getMessage());
        }
        return Response.<String>builder()
                .messages("failed")
                .data("failed booking")
                .build();
    }



    //available time

    // modify booking - change time,change status,change payment status
    // delete
}
