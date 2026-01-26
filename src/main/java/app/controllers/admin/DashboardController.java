package app.controllers.admin;

import app.dto.admin.AdminDashboardDto;
import app.entities.enums.StatusBoking;
import app.services.BookingService;
import app.services.LapanganService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private BookingService bookingService;
    private LapanganService lapanganService;

    public DashboardController(BookingService bookingService,LapanganService lapanganService){
        this.bookingService = bookingService;
        this.lapanganService = lapanganService;
    }

    @GetMapping("admin/dashboard")
    public String getDashboard(
            Model model,
            @RequestParam(value = "tanggal", required = false) LocalDate tanggal,
            @RequestParam(value = "status", required = false) StatusBoking statusBoking,
            @RequestParam(value = "field-name", required = false) String fieldName) {

        model.addAttribute("statusBooking", StatusBoking.values());
        model.addAttribute("lapanganNames", lapanganService.getNameFields());
        model.addAttribute("selectedTanggal", tanggal);
        model.addAttribute("selectedStatus", statusBoking);
        model.addAttribute("selectedLapangan", fieldName);

        AdminDashboardDto dashboardData = bookingService.getSummary(fieldName, tanggal, statusBoking);
        model.addAttribute("dashboardData", dashboardData);

        return "admin/dashboard";
    }





}
