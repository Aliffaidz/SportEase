package app.controllers.admin;

import app.services.LapanganService;
import app.services.PembayaranService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class PembayaranController {

    private PembayaranService pembayaranService;
    private LapanganService lapanganService;

    public PembayaranController(PembayaranService pembayaranService,LapanganService lapanganService){
        this.pembayaranService = pembayaranService;
        this.lapanganService = lapanganService;
    }

    @GetMapping(path = "/admin/data-pembayaran")
    public String getAll(
            @RequestParam(name = "page",required = false)Integer page,
            @RequestParam(name = "pembayaran",required = false)Integer pembayaran,
            @RequestParam(name = "field-name",required = false)String fieldName,
            @RequestParam(name = "booking",required = false) Integer idBooking,
            @RequestParam(name = "tanggal",required = false)LocalDate tanggal,
            @RequestParam(name = "username",required = false)String username,
            @RequestParam(name = "handphone",required = false)String handphone,
            Model model)
    {

        if(page == null){
            page = 0;
        }
        model.addAttribute("fieldNames",lapanganService.getNameFields());
        model.addAttribute("selectedField",fieldName);
        model.addAttribute("pagingDataPembayaran",pembayaranService.getAll(page,pembayaran,fieldName,idBooking,tanggal,username,handphone));
        return "admin/data-payment";
    }

    @DeleteMapping(path = "/admin/data-pembayaran/{id}")
    public String delete(@PathVariable(name = "id")Integer id){
        pembayaranService.deletePembayaran(id);
        return "redirect:/admin/data-pembayaran";
    }
}
