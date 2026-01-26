package app.controllers.admin;

import app.services.KetentuanService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class KetentuanController {

    private KetentuanService ketentuanService;

    public KetentuanController(KetentuanService ketentuanService){
        this.ketentuanService = ketentuanService;
    }

    @GetMapping(path = "/admin/ketentuan")
    public String get(Model model){
        model.addAttribute("listKetentuan",ketentuanService.getAll());
        return "admin/ketentuan_booking";
    }

    @PostMapping(path = "/admin/ketentuan")
    public String add(@NotNull(message = "ketentuan tidak boleh kosong") @Valid String ketentuan){
        ketentuanService.add(ketentuan);
        return "redirect:/admin/ketentuan";
    }

    @PatchMapping(path = "/admin/ketentuan/{id}")
    public String update(@PathVariable(name = "id")Integer id,String ketentuan){
        ketentuanService.update(id,ketentuan);
        return "redirect:/admin/ketentuan";
    }

    @DeleteMapping(path = "/admin/ketentuan/{id}")
    public String  delete(@PathVariable(name = "id") Integer id){
        ketentuanService.delete(id);
        return "redirect:/admin/ketentuan";
    }

}
