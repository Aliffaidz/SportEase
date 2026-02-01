package app.services;

import app.entities.Booking;
import app.entities.ImagePaymentVerification;
import app.entities.Payment;
import app.repositories.ImagePaymentRepository;
import app.repositories.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ImagePaymentRepository imagePaymentRepository;
    private final ImagesServiceImpl imagesService;

    public PaymentService(PaymentRepository paymentRepository, ImagePaymentRepository imagePaymentRepository, ImagesServiceImpl imagesService) {
        this.paymentRepository = paymentRepository;
        this.imagePaymentRepository = imagePaymentRepository;
        this.imagesService = imagesService;
    }

    public void addPaymentVerification(Booking booking, List<MultipartFile> image){
        log.info("service payment addPaymentVerification");
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setUsername(booking.getUsername());
        payment.setMobilePhoneNumber(booking.getMobilePhoneNumber());
        payment.setBookingDate(LocalDate.now());
        paymentRepository.save(payment);
        List<String> savedLocalImage = imagesService.saveToLocal(image);
        List<ImagePaymentVerification> imagePaymentVerification = imagesService.saveDb(savedLocalImage, payment).stream()
                .map(img -> (ImagePaymentVerification) img).toList();
        imagePaymentRepository.saveAll(imagePaymentVerification);
    }
}
