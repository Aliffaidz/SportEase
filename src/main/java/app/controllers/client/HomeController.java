package app.controllers.client;

import app.models.home_page.RequestCustomHomePage;
import app.models.home_page.RequestModifyHomePage;
import app.services.BookingService;
import app.services.HomePageService;
import app.services.LapanganService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class HomeController {

    private final LapanganService lapanganService;
    private final BookingService bookingService;
    private final HomePageService homePageService;

    public HomeController(LapanganService lapanganService, BookingService bookingService,HomePageService homePageService){
        this.lapanganService = lapanganService;
        this.bookingService = bookingService;
        this.homePageService = homePageService;
    }


    @GetMapping(path = "/admin/site-config")
    public String get(Model model){
        model.addAttribute("dataHomePage",homePageService.get());
        return "admin/home-config"; // ini code html yang perlu kamu buat
    }

    @PostMapping(path = "/admin/site-config/tambah")
    public String add(@ModelAttribute @Valid RequestCustomHomePage requestCustomHomePage, MultipartFile image, BindingResult bindingResult){
        homePageService.add(requestCustomHomePage,image);
        if(bindingResult.hasErrors()){
            return "redirect:/admin/site-config/tambah";
        }
        return "redirect:/admin/site-config";
    }

    @PatchMapping(path = "/admin/site-config/{id}")
    public String update(@PathVariable(name = "id") Integer id, RequestModifyHomePage requestModifyHomePage,MultipartFile image){
        homePageService.update(id,requestModifyHomePage,image);
        return "redirect:/admin/site-config";
    }

    @GetMapping(path = "/home")
    public String getHomePage(Model model){
        model.addAttribute("data",homePageService.getHome());
        return "home-page";
    }









        


}
