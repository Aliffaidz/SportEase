package app.controllers.admin;

import app.entities.enums.JenisLapangan;
import app.entities.enums.StatusLapangan;
import app.models.lapangan.RequestEditLapangan;
import app.models.lapangan.RequestTambahLapangan;
import app.services.LapanganService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LapanganController {

    private LapanganService lapanganService;

    public LapanganController(LapanganService lapanganService){
        this.lapanganService = lapanganService;
    }


    @GetMapping(path = "/admin/lapangan")
    public String showPanelAdmin(Model model) {
        model.addAttribute("lapanganRequest", new RequestTambahLapangan());
        model.addAttribute("jenisList", JenisLapangan.values());
        model.addAttribute("statusList", StatusLapangan.values());
        model.addAttribute("lapanganList", lapanganService.getAll());
        model.addAttribute("editRequest", new RequestEditLapangan());
        return "admin/panel_admin";
    }


    @PostMapping(path = "/admin/lapangan/tambah", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addFutsalField(
            @ModelAttribute @Valid RequestTambahLapangan request,
            @RequestParam("gambar") List<MultipartFile> gambar
    ){
        lapanganService.addFutsalFutsal(request, gambar);
        return "redirect:/admin/lapangan";
    }

    @PostMapping("/admin/lapangan/edit")
    public String modifyFutsalField(
            @RequestParam(name = "id") Integer id,
            @ModelAttribute RequestEditLapangan request,
            @RequestParam(value = "gambar1",required = false) MultipartFile gambar1,
            @RequestParam(value = "gambar2",required = false) MultipartFile gambar2,
            @RequestParam(value = "gambar3",required = false) MultipartFile gambar3)

    {
        List<MultipartFile> gambar = new ArrayList<>();
        if (gambar1 != null && !gambar1.isEmpty()) gambar.add(gambar1);
        if (gambar2 != null && !gambar2.isEmpty()) gambar.add(gambar2);
        if (gambar3 != null && !gambar3.isEmpty()) gambar.add(gambar3);
        lapanganService.modifyFutsalField(id,request,gambar);
        return "redirect:/admin/lapangan";
    }

    @GetMapping(path = "/admin/lapangan/hapus")
    public String deleteFutsalField(@RequestParam(name = "id") Integer id){
        lapanganService.deleteField(id);
        return "redirect:/admin/lapangan";
    }







}
