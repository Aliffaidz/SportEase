package app.controllers.admin;

import app.models.pembayaran.RequestAddPaymentMethod;
import app.services.RekeningPembayaranService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RekeningController {

    private RekeningPembayaranService rekeningPembayaranService;

    public RekeningController(RekeningPembayaranService rekeningPembayaranService){
        this.rekeningPembayaranService = rekeningPembayaranService;
    }


    @PostMapping(path = "/admin/metode-pembayaran")
    public String addPaymentMethod(RequestAddPaymentMethod requestAddPaymentMethod){
        rekeningPembayaranService.addPaymentMethod(requestAddPaymentMethod);
        return "redirect:/admin/metode-pembayaran";
    }

    @GetMapping(path = "admin/metode-pembayaran")
    public String getAll(Model model){
        model.addAttribute("listPaymentMethod",rekeningPembayaranService.getListRekeningPembayaran());
        return "admin/rekening";
    }

    @PatchMapping(path = "/admin/metode-pembayaran/{id}")
    public String updatePaymentMethod(@ModelAttribute RequestAddPaymentMethod updated, @PathVariable(name = "id")String id){
        rekeningPembayaranService.editPaymentMethod(updated,id);
        return "redirect:/admin/metode-pembayaran";
    }

    @GetMapping(path = "/admin/metode-pembayaran/{id}")
    public String deletePaymentMethod(@PathVariable(name = "id")String id){
        rekeningPembayaranService.deletePaymentMethod(id);
        return "redirect:/admin/metode-pembayaran";
    }



}
