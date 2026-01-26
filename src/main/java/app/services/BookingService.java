package app.services;

import app.dto.BookingTime;
import app.dto.admin.AdminDashboardDto;
import app.dto.admin.SummaryBooking;
import app.entities.enums.StatusLapangan;
import app.entities.enums.StatusPembayaran;
import app.models.booking.RequestUpdateBooking;
import app.notification.SendEmailService;
import app.utility.BookingUtility;
import app.web_socket.SendUpdatedDataBooking;
import app.entities.Booking;
import app.entities.Lapangan;
import app.entities.enums.StatusBoking;
import app.models.booking.RequestBookingLapangan;
import app.repositories.BookingRepository;
import app.repositories.LapanganRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Service
public class BookingService {

    private LapanganRepository lapanganRepository;
    private BookingRepository bookingRepository;
    private SendUpdatedDataBooking sendUpdatedDataBooking;
    private BookingUtility bookingUtility;
    private LapanganService lapanganService;
    private final Integer  FIX_SIZE = 10;
    private SendEmailService sendEmailService;


    public BookingService(LapanganRepository lapanganRepository, BookingRepository bookingRepository, SendUpdatedDataBooking sendUpdatedDataBooking,BookingUtility bookingUtility, LapanganService lapanganService,SendEmailService sendEmailService) {
        this.lapanganRepository = lapanganRepository;
        this.bookingRepository = bookingRepository;
        this.sendUpdatedDataBooking = sendUpdatedDataBooking;
        this.bookingUtility = bookingUtility;
        this.lapanganService = lapanganService;
        this.sendEmailService = sendEmailService;

    }

    @Transactional
    public void bookingField(RequestBookingLapangan booking , MultipartFile imagePaymentVerfication) {
        Lapangan lapangan = lapanganRepository.findById(booking.getIdField()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "lapangan tidak ditermukan")
        );

        if(lapangan.getStatusLapangan().equals(StatusLapangan.TUTUP)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lapangan tidak tersedia");
        }

        BigDecimal price = isValidBookingTimeAndPrice(
                booking.getStartTime(),
                booking.getEndedTime(),
                booking.getIdField(),
                lapangan
        );

        Booking bookingData = new Booking();

        log.info("START CREATE DATA BOOKING");
        bookingData.setStatusBooking(StatusBoking.MENUNGGU);
        bookingData.setUsername(booking.getUsername());
        bookingData.setHandphone(booking.getHandPhoneNumber());
        bookingData.setHarga(price);
        bookingData.setWaktuMulai(booking.getStartTime());
        bookingData.setWaktuSelesai(booking.getEndedTime());
        bookingData.setStatusPembayaran(StatusPembayaran.BELUM_BAYAR);
        bookingData.setLapangan(lapangan);

        log.info("START SAVE BOOKING");
        bookingRepository.save(bookingData);

        sendEmailService.sendNotificationPaymentConfirm(bookingData,imagePaymentVerfication,lapangan);

        List<BookingTime> bookingTimeList = lapanganService.getBookingTimeField(lapangan.getId(),booking.getStartTime().toLocalDate());
        sendUpdatedDataBooking.sendUpdatedDataBooking(bookingTimeList);
    }

    @Transactional(readOnly = true)
    public PagedModel<Booking> getBookingList(Integer page, String fieldName,Integer idBooking, StatusBoking statusBoking, LocalDate tanggal, StatusPembayaran statusPembayaran, String username){
        Sort  sort = Sort.by(Sort.Order.asc("waktuMulai"));
        Pageable pageable = PageRequest.of(page,FIX_SIZE);
        Page<Booking> bookingPage;


        if(fieldName == null & statusBoking == null & tanggal == null & statusPembayaran == null & username == null){
            log.info("equery null");
            bookingPage = bookingRepository.findAll(pageable);
            return new PagedModel<>(bookingPage);
        }

        Specification<Booking> specification = (root, query, criteriaBuilder) -> {
            log.info("query spec");
            List<Predicate> predicates = new ArrayList<>();
            if(fieldName != null && !fieldName.isBlank()){
                    log.info("field name " + fieldName);
                    predicates.add(criteriaBuilder.equal(root.get("lapangan").get("namaLapangan"),fieldName));
            }
            if(idBooking != null){
                predicates.add(criteriaBuilder.equal(root.get("id"),idBooking));
            }
            if(username != null && !username.isBlank()){
                log.info("username " + username);
                predicates.add(criteriaBuilder.equal(root.get("username"),username));
            }
            if(statusBoking != null){
                log.info("status booking " + statusBoking);
                predicates.add(criteriaBuilder.equal(root.get("statusBooking"),statusBoking ));
            }
            if(statusPembayaran != null){
                log.info("status pembayaran " + statusPembayaran);
                predicates.add(criteriaBuilder.equal(root.get("statusPembayaran"),statusPembayaran));
            }

            if (tanggal != null) {
                log.info("tanggal " + tanggal);
                Expression<LocalDate> dateExpression = criteriaBuilder.function("DATE", LocalDate.class, root.get("waktuMulai"));
                predicates.add(criteriaBuilder.equal(dateExpression, tanggal));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        bookingPage = bookingRepository.findAll(specification,pageable);
        return new PagedModel<>(bookingPage);
    }

    public void updateBooking(RequestUpdateBooking updateBooking){
        Booking booking = bookingRepository.findFirstByIdAndLapangan(updateBooking.getIdBooking(),updateBooking.getIdField()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"booking dengan id " + updateBooking.getIdBooking() + " tidak ditemukan")
        );

        Lapangan lapangan = lapanganRepository.findById(updateBooking.getIdField()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"lapangan tidak ditemukan")
        );

        LocalDateTime mulai = null;
        LocalDateTime selesai = null;
        BigDecimal harga;

        if(updateBooking.getWaktuMulai().equals(booking.getWaktuMulai()) && updateBooking.getWaktuSelesai().equals(booking.getWaktuSelesai())){

        }else{
            if(!updateBooking.getWaktuMulai().equals(booking.getWaktuMulai())){
                mulai = updateBooking.getWaktuMulai();
            }
            if(!updateBooking.getWaktuSelesai().equals(booking.getWaktuSelesai())){
                selesai = updateBooking.getWaktuSelesai();
            }

            if(mulai != null && selesai != null){
                harga = isValidBookingTimeAndPrice(
                        mulai,
                        selesai,
                        updateBooking.getIdField(),
                        lapangan
                );
                booking.setWaktuMulai(mulai);
                booking.setWaktuSelesai(selesai);
                booking.setHarga(harga);
            }

            if(mulai != null){
                harga = isValidBookingTimeAndPrice(
                        mulai,
                        booking.getWaktuSelesai(),
                        updateBooking.getIdField(),
                        lapangan
                );
                booking.setWaktuMulai(mulai);
                booking.setHarga(harga);
            }

            if(selesai != null){
                harga = isValidBookingTimeAndPrice(
                        booking.getWaktuMulai(),
                        selesai,
                        updateBooking.getIdField(),
                        lapangan
                );
                booking.setWaktuSelesai(selesai);
                booking.setHarga(harga);
            }

        }

        if(updateBooking.getUsername() != null) booking.setUsername(updateBooking.getUsername());
        if(updateBooking.getHandPhone() != null) booking.setHandphone(updateBooking.getHandPhone());
        if(updateBooking.getStatusBoking() != null) booking.setStatusBooking(updateBooking.getStatusBoking());
        if(updateBooking.getStatusPembayaran() != null) booking.setStatusPembayaran(updateBooking.getStatusPembayaran());

        bookingRepository.save(booking);

/*        List<Booking> bookingList = bookingRepository.cariBerdasarkanIdLapanganStatusDanTanggal(updateBooking.getIdField(), StatusBoking.MENUNGGU,StatusBoking.SEDANG_BERLANGSUNG,LocalDate.now());
        sendUpdatedDataBooking.sendUpdatedDataBooking(bookingUtility.returnBookingDto(bookingList, updateBooking.getIdField()));*/
    }

    public void deleteBooking(Integer IdBooking,Integer idField){
        log.info("delete booking " + IdBooking + " " + idField);
        Booking booking = bookingRepository.findFirstByIdAndLapangan(IdBooking,idField).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"booking dengan id " + IdBooking+ " tidak ditemukan")
        );
        bookingRepository.delete(booking);
    }

    public AdminDashboardDto getSummary(String fieldName, LocalDate tanggal,StatusBoking statusBoking){

        if(tanggal == null){
            tanggal = LocalDate.now();
        }

        SummaryBooking summaryBooking = bookingRepository.ambilDataSummary(tanggal);
        long totalLapanganAktif = lapanganRepository.totalLapanganAktif(StatusLapangan.TERSEDIA);
        summaryBooking.setTotalLapangaAktif(totalLapanganAktif);
        List<Booking> totalBooking;

        if(statusBoking == null){
            statusBoking = StatusBoking.MENUNGGU;
        }

        if(fieldName != null && !fieldName.isBlank()){
            log.info("booking dengan nama lapangan " + fieldName);
            totalBooking = bookingRepository.ambilBookingHariIni(tanggal,statusBoking,fieldName);
        }else {
            log.info("booking tanpa nama lapangan ");
            log.info("tangggal " + tanggal.toString() + "status " + statusBoking.toString() );
            totalBooking = bookingRepository.ambilBookingHariIniAll(tanggal,statusBoking);
        }
        return AdminDashboardDto.builder()
                .summaryDto(summaryBooking)
                .listBookingHariIni(totalBooking)
                .build();
    }

    @Transactional
    @Scheduled(fixedRate = 60 * 5 * 1000)
    protected void updateStatusBooking(){
        log.info("refaresh booking");
        List<Booking> bookingList = bookingRepository.ambilSemuaBookingHariIni(LocalDateTime.now());

        if(bookingList.isEmpty()){

        }else {
            List<Booking> list = bookingList.stream()
                    .filter(
                    b -> b.getWaktuSelesai().toLocalTime().isBefore(LocalTime.now())
                    ).map(booking -> {
                        booking.setStatusBooking(StatusBoking.SELESAI);
                        return booking;
                    }).toList();
            bookingRepository.saveAll(list);
        }
    }

    private BigDecimal isValidBookingTimeAndPrice(LocalDateTime startTime, LocalDateTime endedTime,Integer idField,Lapangan lapangan){

        bookingUtility.isValidDate(startTime.toLocalDate(),endedTime.toLocalDate(),lapangan);
        bookingUtility.isValidTime(startTime.toLocalDate(),startTime.toLocalTime(),endedTime.toLocalTime(),lapangan);

        if (bookingRepository.validasiWaktuBooking(idField, startTime, endedTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "waktu booking bertabrakan");
        }

        return bookingUtility.getPrice(startTime.toLocalTime(),endedTime.toLocalTime(),lapangan.getHargaLapanganPerjam());
    }
}




