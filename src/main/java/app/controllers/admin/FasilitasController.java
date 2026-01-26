package app.controllers.admin;

import app.dto.admin.FasilitasDto;
import app.models.fasilitas.RequestTambahFasilitas;
import app.models.fasilitas.RequestEditFasilitas;
import app.services.FasilitasService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller

public class FasilitasController {

    private final FasilitasService fasilitasService;

    public FasilitasController(FasilitasService fasilitasService){
        this.fasilitasService = fasilitasService;
    }

    @GetMapping(path = "/admin/fasilitas")
    public String showDataFasilitas(Model model) {
        List<FasilitasDto> fasilitasList = fasilitasService.ambilSemuaFasilitas();
        model.addAttribute("fasilitasList", fasilitasList);
        return "admin/data_fasilitas";
    }

    @PostMapping(path = "/admin/fasilitas/tambah")
    public String addFacility(
            @ModelAttribute @Valid RequestTambahFasilitas fasilitas,
            @RequestParam(name = "gambar") List<MultipartFile> gambar)
    {
        fasilitasService.tambahFasilitas(fasilitas, gambar);
        return "redirect:/admin/fasilitas";
    }

    @PostMapping(path = "/admin/fasilitas/edit")
    public String editFacility(
            @ModelAttribute RequestEditFasilitas facility,
            @RequestParam(name = "gambar1") MultipartFile gambar1,
            @RequestParam(name = "gambar2") MultipartFile gambar2,
            @RequestParam(name = "gambar3") MultipartFile gambar3
    )
    {
        List<MultipartFile> images = new ArrayList<>();
        if(gambar1 != null && !gambar1.isEmpty()) images.add(gambar1);
        if(gambar2 != null && !gambar2.isEmpty()) images.add(gambar2);
        if(gambar3 != null && !gambar3.isEmpty()) images.add(gambar3);

        fasilitasService.modifyFacility(facility,images);
        return "redirect:/admin/fasilitas";
    }

    @GetMapping(path = "/admin/fasilitas/hapus")
    public String deleteFacility(@RequestParam(name = "id") Integer id){
        log.info("ID HAPUS " + id);
        fasilitasService.hapusFasilitas(id);
        return "redirect:/admin/fasilitas";
    }



}
