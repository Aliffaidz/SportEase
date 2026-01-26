package app.notification;

import app.entities.Booking;
import app.entities.EmailNotification;
import app.entities.Lapangan;
import app.repositories.EmailNotificationRepository;
import app.services.PembayaranService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Slf4j
@Component
public class SendEmailService {

    private JavaMailSender javaMailSender;

    private EmailNotificationRepository emailNotificationRepository;

    @Value("${application.image-locator-payment}")
    private String IMAGE_PATH_LOCATION_PAYMENT_VERIFICATION;

    private PembayaranService pembayaranService;

    public SendEmailService(JavaMailSender javaMailSender,EmailNotificationRepository emailNotificationRepository,PembayaranService pembayaranService){
        this.javaMailSender = javaMailSender;
        this.emailNotificationRepository = emailNotificationRepository;
        this.pembayaranService = pembayaranService;
    }

    @Async
    public void sendNotificationPaymentConfirm(Booking booking, MultipartFile imagePayment, Lapangan lapangan){
        String imageLocaltion = saveImagePaymentVerification(imagePayment);
        pembayaranService.addPembayaran(booking,imageLocaltion,lapangan);
        try{
            log.info("send email ");
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage,true,"UTF-8");

            helper.setFrom("aliffaid666@gmail.com");
            helper.setSubject("VERIFIKASI PEMBAYARAN");
            String htmlContent = buildHtmlContent(booking);
            helper.setText(htmlContent,true);

            log.info("location image " + imageLocaltion);
            File imagePaymentFile = new File(imageLocaltion);
            FileSystemResource fileSystemResource = new FileSystemResource(imagePaymentFile);

            helper.addInline("buktiPembayaran",fileSystemResource);

            emailNotificationRepository.findAll()
                            .forEach(email -> {
                                try {
                                    helper.setTo(email.getEmail());
                                } catch (MessagingException e) {
                                    throw new RuntimeException(e);
                                }
                                javaMailSender.send(mimeMailMessage);
                            });
        }catch (MessagingException | RuntimeException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"gagal mengirim pesan booking");
        }
    }

    @Transactional
    public void addEmail(@NotNull String email){
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setEmail(email);
        emailNotificationRepository.save(emailNotification);
    }

    @Transactional
    public void editEmail(String email,Integer id){
        EmailNotification emailNotification = emailNotificationRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"email yang ingin diedit tidak ditemukan")
        );
        emailNotification.setEmail(email);
        emailNotificationRepository.save(emailNotification);
    }

    @Transactional
    public void deleteEmail(Integer id){
        EmailNotification emailNotification = emailNotificationRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"email yang ingin dihapus tidak ditemukan")
        );
        emailNotificationRepository.delete(emailNotification);
    }

    @Transactional(readOnly = true)
    public List<EmailNotification> getAll(){
        return emailNotificationRepository.findAll();
    }

    private String buildHtmlContent(Booking booking) {
        return """
        <!DOCTYPE html>
        <html>
        <body style="font-family: Arial, sans-serif;">
            <h2>Booking Menunggu Verifikasi</h2>

            <p><b>Nama User:</b> %s</p>
            <p><b>No HP:</b> %s</p>
            <p><b>Lapangan:</b> %s</p>
            <p><b>Waktu:</b> %s - %s</p>
            <p><b>Harga:</b> Rp %s</p>

            <p><b>Status Booking:</b> %s</p>
            <p><b>Status Pembayaran:</b> %s</p>

            <h3>Bukti Pembayaran:</h3>
            <img src="cid:buktiPembayaran" 
                 style="max-width:100%%; border:1px solid #ccc"/>

            <p>Silakan login ke sistem untuk melakukan verifikasi.</p>
        </body>
        </html>
        """.formatted(
                booking.getUsername(),
                booking.getHandphone(),
                booking.getLapangan().getNamaLapangan(),
                booking.getWaktuMulai(),
                booking.getWaktuSelesai(),
                booking.getHarga(),
                booking.getStatusBooking(),
                booking.getStatusPembayaran()
        );
    }

    public String saveImagePaymentVerification(MultipartFile imagePayment) {
        if(imagePayment.isEmpty() || Objects.requireNonNull(imagePayment.getOriginalFilename()).lastIndexOf(".") < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"format gambar harus ber-extensi jpg,jepg dll");
        }

        int fileExtension = Objects.requireNonNull(imagePayment.getOriginalFilename()).lastIndexOf(".");
        String buatNamaGambar = UUID.randomUUID().toString() + imagePayment.getOriginalFilename().substring(fileExtension);

        Path pathName = Paths.get(IMAGE_PATH_LOCATION_PAYMENT_VERIFICATION);
        Path imageName = pathName.resolve(buatNamaGambar);
        createDirectory();
        try(OutputStream stream = Files.newOutputStream(imageName)){
            stream.write(imagePayment.getBytes());
            stream.flush();
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage());
        }
        return imageName.toString();
    }

    private void createDirectory(){
        Path sourceLocation = Paths.get(IMAGE_PATH_LOCATION_PAYMENT_VERIFICATION);
        if(!Files.exists(sourceLocation)){
            log.info("membuat directory");
            try{
                Files.createDirectories(sourceLocation);
            }catch (IOException e){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"gagal mengupload gambar");
            }
        }
    }

}
