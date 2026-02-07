package app.services;

import app.entities.Booking;
import app.entities.Field;
import app.entities.PaymentMethod;
import app.entities.Provision;
import app.entities.enums.BookingStatus;
import app.entities.enums.PaymentStatus;
import app.model.dto.*;
import app.model.request.RequestBookingField;
import app.model.request.RequestEditBooking;
import app.model.request.RequestFilterBookings;
import app.repositories.BookingRepository;
import app.repositories.FieldRepository;
import app.utility.BookingUtility;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingUtility bookingUtility;
    private final FieldRepository fieldRepository;
    private final ImagesServiceImpl imagesService;
    private final ProvisionService provisionService;
    private final PaymentMethodService paymentMethodService;


    public BookingService(BookingRepository bookingRepository, BookingUtility bookingUtility, FieldRepository fieldRepository, ImagesServiceImpl imagesService, ProvisionService provisionService, PaymentMethodService paymentMethodService){
        this.bookingRepository = bookingRepository;
        this.bookingUtility = bookingUtility;
        this.fieldRepository = fieldRepository;
        this.imagesService = imagesService;
        this.provisionService = provisionService;
        this.paymentMethodService = paymentMethodService;
    }

    public Response<String> bookingField(RequestBookingField requestBookingField, List<MultipartFile> image , Integer idField){
        log.info("service booking bookingField");
        Field field = fieldRepository.findById(idField).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"field with id " + idField + " not found !!!")
        );
        try{
            boolean isOverlap = bookingRepository.isBookingOverlap(idField, requestBookingField.getDate(), requestBookingField.getStart(), requestBookingField.getEnded());
            if(bookingUtility.isValidDate(requestBookingField.getDate(),field) && bookingUtility.isValidTime(requestBookingField.getStart(),requestBookingField.getEnded(),field) && bookingUtility.isValidPrice(requestBookingField.getStart(),requestBookingField.getEnded(),requestBookingField.getPrice(),field) && !isOverlap){
                Booking booking = new Booking();
                booking.setUsername(requestBookingField.getName());
                booking.setMobilePhoneNumber(requestBookingField.getPhoneNumber());
                booking.setBookingStatus(BookingStatus.BOOKED);
                booking.setField(field);
                booking.setPrice(requestBookingField.getPrice());
                booking.setStartTime(requestBookingField.getStart());
                booking.setEndedTime(requestBookingField.getEnded());
                booking.setDate(requestBookingField.getDate());
                booking.setPaymentStatus(PaymentStatus.MENUNGGU_KONFIRMASI);
                List<String> img = imagesService.saveToLocal(image);
                booking.setImagePayment(img.getFirst());
                bookingRepository.save(booking);
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

    public Response<AvailableBookingSlots> getAvailableSlots(LocalDate date,Integer idField){
        if(date == null){
            date = LocalDate.now();
        }

        Field field = fieldRepository.getOperationalHour(idField).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"field with id " + idField + " not found !!! ")
        );

        List<Booking> bookingList = bookingRepository.getAvailableBookingSlots(date,idField);
        AvailableBookingSlots availableBookingSlots  = new AvailableBookingSlots();
        availableBookingSlots.setBookingSlotList(new ArrayList<>());

        LocalTime start = field.getOperationHours().getStartTime();
        LocalTime ended = field.getOperationHours().getEndedTime();

        while(start.isBefore(ended)){
            LocalTime slotEnd = start.plusHours(1L);
            boolean conflict = false;
            for (Booking b : bookingList){

                if(start.isBefore(b.getEndedTime()) && slotEnd.isAfter(b.getStartTime())){
                    conflict = true;
                    break;
                }
            }
            if(!conflict){
                BookingSlot bookingSlot = new BookingSlot();
                bookingSlot.setStart(start);
                bookingSlot.setEnded(slotEnd);
                if(slotEnd.isAfter(LocalTime.now())){
                    bookingSlot.setStatus(BookingStatus.AVAILABLE);
                }else{
                    bookingSlot.setStatus(BookingStatus.FINISHED);
                }
                availableBookingSlots.getBookingSlotList().add(bookingSlot);
            }else{
                BookingSlot bookingSlot = new BookingSlot();
                bookingSlot.setStart(start);
                bookingSlot.setEnded(slotEnd);
                if(slotEnd.isAfter(LocalTime.now())){
                    bookingSlot.setStatus(BookingStatus.BOOKED);
                }else{
                    bookingSlot.setStatus(BookingStatus.FINISHED);
                }
                availableBookingSlots.getBookingSlotList().add(bookingSlot);
            }
            start = slotEnd;

        }
        return Response.<AvailableBookingSlots>builder()
                .messages("success")
                .data(availableBookingSlots)
                .build();
    }

    // pr di paging ingat semalam
    public ResponsePagingData<List<BookingDto>> getAllBookings(RequestFilterBookings filterBookings){
        log.info("booking service getAllBookings");
        Pageable page = PageRequest.of(filterBookings.getPage(),10);
        Page<Booking> bookingPage;
        List<BookingDto> bookings;

        if(
                        filterBookings.getUsername() == null
                        && filterBookings.getFieldName() == null
                        && filterBookings.getDate() == null
                        && filterBookings.getStatus() == null
                        && filterBookings.getPayment() == null
        ){
            log.info("booking service getAllBookings query default");
            LocalDate date = LocalDate.now();
            bookings = bookingRepository.getAllBookingsByDate(date).stream().map(data -> new BookingDto(
                    data.getId(),
                    data.getUsername(),
                    data.getStartTime(),
                    data.getEndedTime(),
                    data.getDate(),
                    data.getPrice(),
                    data.getBookingStatus(),
                    data.getPaymentStatus(),
                    data.getImagePayment()
            )).toList();
            log.info("booking service getAllBookings query default with paging");
            Page<Booking> pageRequest = bookingRepository.findAllByDate(date,page);
            return ResponsePagingData.<List<BookingDto>>builder()
                    .messages("success")
                    .data(bookings)
                    .page(new PageResponse(pageRequest.getSize(),pageRequest.getNumber(),pageRequest.getTotalElements(),pageRequest.getTotalPages()))
                    .build();

        }

        Specification<Booking> querySpect =  (root, query, criteriaBuilder) -> {
            log.info("booking service getAllBookings query spect");
            List<Predicate> predicates = new ArrayList<>();

            if(filterBookings.getId() != null){
                predicates.add(criteriaBuilder.equal(root.get("id"),filterBookings.getId()));
            }

            if(filterBookings.getUsername() != null && !filterBookings.getUsername().isBlank()){
                predicates.add(criteriaBuilder.like(root.get("username"),filterBookings.getUsername()));
            }

            if(filterBookings.getDate() != null){
                predicates.add(criteriaBuilder.equal(root.get("date"),filterBookings.getDate()));
            }

            if(filterBookings.getStatus() != null){
                predicates.add(criteriaBuilder.equal(root.get("bookingStatus"),filterBookings.getStatus()));
            }

            if(filterBookings.getPayment() != null){
                predicates.add(criteriaBuilder.equal(root.get("paymentStatus"),filterBookings.getPayment()));
            }

            if(filterBookings.getFieldName() != null && !filterBookings.getFieldName().isBlank()){
                predicates.add(criteriaBuilder.equal(root.get("field").get("fieldName"),filterBookings.getFieldName()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        bookingPage = bookingRepository.findAll(querySpect,page);

        bookings = bookingPage.stream().map(data -> new BookingDto(
                data.getId(),
                data.getUsername(),
                data.getStartTime(),
                data.getEndedTime(),
                data.getDate(),
                data.getPrice(),
                data.getBookingStatus(),
                data.getPaymentStatus(),
                data.getImagePayment()
        )).toList();
        return ResponsePagingData.<List<BookingDto>>builder()
                .messages("success")
                .data(bookings)
                .page(new PageResponse(bookingPage.getSize(),bookingPage.getNumber(),bookingPage.getTotalElements(),bookingPage.getTotalPages()))
                .build();
    }

    public Response<BookingFormData> getBookingInformation(Integer idField){
        Field field = fieldRepository.findById(idField).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"field with id " + idField + " not found !!!")
        );

        BookingFormData data = new BookingFormData();

        data.setIdField(field.getId());
        data.setFieldName(field.getFieldName());
        data.setHourlyPrice(field.getHourlyPrice());
        data.setMaximumBookingTime(field.getMaximumBookingHours());
        data.setRangeBookingDate(field.getOperationDate());
        Response<List<Provision>> provisions = provisionService.getAllProvisions();
        Response<List<PaymentMethod>> paymentMethods = paymentMethodService.getAllPaymentMethods();
        data.setPaymentMethods(paymentMethods.getData());
        data.setProvisions(provisions.getData());
        LocalDate now = LocalDate.now();
        AvailableBookingSlots slots = getAvailableSlots(now,field.getId()).getData();
        slots.setDate(now);
        data.setAvailableBookingSlots(slots);
        return Response.<BookingFormData>builder()
                .messages("success")
                .data(data)
                .build();
    }

    public Response<String> changePaymentStatus(Integer id, PaymentStatus status){
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"booking tidak ditemukan")
        );

        if(status.equals(PaymentStatus.SUDAH_BAYAR)){
            booking.setPaymentStatus(PaymentStatus.SUDAH_BAYAR);
        }else{
            booking.setBookingStatus(BookingStatus.CANCEL);
        }
        return Response.<String>builder()
                .messages("success")
                .data("success change status " + status)
                .build();
    }

    public Response<String> changeBookingTime(Integer id, RequestEditBooking data){

        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"booking with id " + id + " not found !!!")
        );

        Field field = booking.getField();

        boolean validDate = true;
        boolean validTime = true;
        boolean validPrice = false;

        if(data.getDate() != null){
            validDate = bookingUtility.isValidDate(data.getDate(),field);
        } else if (data.getStart() != null && data.getEnded() != null) {
            validTime = bookingUtility.isValidTime(data.getStart(),data.getEnded(),field);
            validPrice = bookingUtility.isValidPrice(data.getStart(),data.getEnded(),data.getPrice(),field);
        }
        boolean bookingOverlap = bookingRepository.isBookingOverlap(field.getId(), data.getDate(), data.getStart(), data.getEnded());
        if(validDate && validTime && validPrice && !bookingOverlap){
            return Response.<String>builder()
                    .messages("success")
                    .data("success modify booking")
                    .build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"gagal memperbarui booking");
    }

    private BookingSummaryDashboard getChartCard(LocalDate start, LocalDate end, String fieldName){
        BookingSummaryDashboard summaryDashboard;
        if(fieldName != null && !fieldName.isBlank()){
            summaryDashboard = bookingRepository.getSummaryWithFieldName(start,end,fieldName);
        }
    }

/*    public DashboardChartWeek getChartWeek(LocalDate start,LocalDate end){


    }*/







}
