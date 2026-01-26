package app.controllers.client;

import app.dto.BookingTime;
import app.entities.Lapangan;
import app.entities.enums.BookingStatus;
import app.entities.enums.StatusBoking;
import app.entities.enums.StatusPembayaran;
import app.models.booking.RequestBookingLapangan;
import app.models.booking.RequestUpdateBooking;
import app.repositories.LapanganRepository;
import app.services.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@Slf4j
public class BookingController {

    private final BookingService bookingService;
    private final LapanganRepository lapanganRepository;
    private final LapanganService lapanganService;
    private final RekeningPembayaranService rekeningPembayaranService;
    private final KetentuanService ketentuanService;
    private final HomePageService homePageService;


    public BookingController(BookingService bookingService, LapanganRepository lapanganRepository, LapanganService lapanganService, RekeningPembayaranService rekeningPembayaranService, KetentuanService ketentuanService, HomePageService homePageService) {
        this.bookingService = bookingService;
        this.lapanganRepository = lapanganRepository;
        this.lapanganService = lapanganService;
        this.rekeningPembayaranService = rekeningPembayaranService;
        this.ketentuanService = ketentuanService;
        this.homePageService = homePageService;
    }


    @GetMapping("/jadwal-lapangan")
    public String getBookings(@RequestParam(value = "date",required = false)LocalDate date,Model model){
        model.addAttribute("bookingStatus", BookingStatus.values());
        model.addAttribute("homePageTitle",homePageService.get().getTitle());
        model.addAttribute("WhatsApp",homePageService.get().getContact());
        model.addAttribute("listBookingData",lapanganService.getField(date));
        log.info("nomor wa ============== " + homePageService.get().getContact());
        return "booking-page";
    }

    @GetMapping(path = "booking/{id}")
    public String getBooking(@PathVariable(name = "id") Integer idField, Model model) {
        log.info("Menampilkan form booking untuk lapangan ID: {}", idField);

        Lapangan lapangan = lapanganService.getCurrentFiled(idField);

        if (lapangan == null) {
            log.error("Lapangan dengan ID {} tidak ditemukan", idField);
            return "redirect:/jadwal-lapangan";
        }

        RequestBookingLapangan bookingForm = new RequestBookingLapangan();
        bookingForm.setIdField(idField);
        model.addAttribute("maxWaktuBooking",lapangan.getMaximalWaktuBooking());
        model.addAttribute("listKententuanBooking",ketentuanService.getAll());
        model.addAttribute("bookingForm", bookingForm);
        model.addAttribute("lapangan", lapangan);

        // Batas tanggal maksimum berdasarkan tanggalBeroperasi
        LocalDate maxBookingDate = lapangan.getTanggalBeroperasi();
        model.addAttribute("maxBookingDate", maxBookingDate);

        // Minimal tanggal booking adalah hari ini
        LocalDate minBookingDate = LocalDate.now();
        model.addAttribute("minBookingDate", minBookingDate);

        // Minimal waktu booking adalah 1 jam dari sekarang
        LocalDateTime minDateTime = LocalDateTime.now().plusHours(1);
        String formattedMinDateTime = minDateTime.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        );
        model.addAttribute("minDateTime", formattedMinDateTime);

        model.addAttribute("listRekeningPembayaran", rekeningPembayaranService.getListRekeningPembayaran());

        // Tambahkan pesan informasi tentang batas booking
        if (maxBookingDate != null) {
            String maxDateStr = maxBookingDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            model.addAttribute("maxDateMessage", "Booking dapat dilakukan hingga tanggal " + maxDateStr);

            // Cek apakah sudah melewati batas tanggal
            if (LocalDate.now().isAfter(maxBookingDate)) {
                model.addAttribute("bookingClosed", true);
                model.addAttribute("closedMessage",
                        "Maaf, booking untuk lapangan ini sudah ditutup. Batas booking adalah tanggal " + maxDateStr);
            }
        }

        log.info("Harga per jam lapangan {}: {}",
                lapangan.getNamaLapangan(),
                lapangan.getHargaLapanganPerjam());
        log.info("Batas tanggal booking: {}", maxBookingDate);

        return "boking/booking";
    }

    @PostMapping(path = "booking/{id}")
    public String postBooking(@PathVariable(name = "id") Integer idField,
                              @ModelAttribute("bookingForm") @Valid RequestBookingLapangan bookingLapangan,
                              @RequestPart(name = "imagePayment") MultipartFile image,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes)
    {
        log.info("Memproses booking untuk lapangan ID: {}", idField);

        Lapangan lapangan = lapanganService.getCurrentFiled(idField);
        if (lapangan == null) {
            redirectAttributes.addFlashAttribute("error", "Lapangan tidak ditemukan");
            return "redirect:/jadwal-lapangan";
        }

        if(bindingResult.hasErrors()){
            log.warn("Validasi gagal: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Validasi gagal. Silakan periksa data yang diinput."
            );
            return "redirect:/booking/" + idField;
        }

        try {
            bookingLapangan.setIdField(idField);
            // Service akan menghitung harga total berdasarkan durasi (per menit)
            bookingService.bookingField(bookingLapangan, image);
            redirectAttributes.addFlashAttribute("success",
                    "✅ Booking berhasil!,,tunggu beberapa saat Admin untuk mengecek booking");

            log.info("Booking berhasil untuk lapangan ID: {}", idField);

        } catch (Exception e) {
            log.error("Error saat booking: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error",
                    "❌ Gagal melakukan booking: " + e.getMessage());
            return "redirect:/booking/" + idField;
        }

        return "redirect:/jadwal-lapangan";
    }

    @GetMapping(path = "/admin/bookings")
    public String getBookingData(
            @RequestParam(name = "page",required = false) Integer page,
            @RequestParam(name = "field-name",required = false) String fieldName,
            @RequestParam(name = "id-booking",required = false) Integer idBooking,
            @RequestParam(name = "status-booking",required = false) StatusBoking statusBoking,
            @RequestParam(name = "status-pembayaran",required = false)StatusPembayaran statusPembayaran,
            @RequestParam(name = "date",required = false) LocalDate tanggal,
            @RequestParam(name = "username",required = false)String name,
            Model model
            ){
        if(page == null || page < 0) page = 0;
        model.addAttribute("statusBooking",StatusBoking.values());
        model.addAttribute("statusPembayaran",StatusPembayaran.values());
        model.addAttribute("fieldNames",lapanganService.getNameFields());
        model.addAttribute("dataPagingBooking",bookingService.getBookingList(page,fieldName,idBooking,statusBoking,tanggal,statusPembayaran,name));
        return "/admin/data_booking";
    }

    @PatchMapping(path = "/admin/bookings")
    public String updateBooking(@ModelAttribute("bookingUpdateForm") @Valid RequestUpdateBooking booking){
        bookingService.updateBooking(booking);
        return "redirect:/admin/bookings";
    }

    @DeleteMapping(path = "/admin/bookings")
    public String deleteBooking(
            @RequestParam(name = "booking-id")Integer idBooking,
            @RequestParam(name = "field-id")Integer idField)
    {
        bookingService.deleteBooking(idBooking,idField);
        return "redirect:/admin/bookings";
    }

    @GetMapping(path = "/detail-field/{id}")
    public String getDetailField(@PathVariable(value = "id") Integer idField,Model model){
        model.addAttribute("title",homePageService.get().getTitle());
        model.addAttribute("detailField",lapanganService.getDetailField(idField));
        return "/detail-field";
    }

    @GetMapping(path = "/avaliable-time")
    @ResponseBody
    public List<BookingTime> getAvaliableTime(@RequestParam(name = "id")Integer idField, @RequestParam(name = "date")LocalDate date){
        log.info("tanggal " + date.toString() + " id " + idField);
        return lapanganService.getBookingTimeField(idField, date);
    }
}