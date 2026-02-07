package app.controllers;

import app.entities.HomePage;
import app.model.dto.Response;
import app.model.request.RequestCustomHomePage;
import app.model.request.RequestEditHomePage;
import app.services.HomePageService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/home")
public class HomePageController {

    private final HomePageService homePageService;

    public HomePageController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> modifyHomePage(@RequestPart(name = "data") @Valid RequestCustomHomePage homePage, @RequestPart(name = "image") List<MultipartFile> image){
        return homePageService.customHomePage(homePage,image);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<HomePage> getAll(){
        return homePageService.get();
    }

    @PatchMapping(path = "/{id}/edit")
    public Response<String> modifyHomePage(
            @PathVariable(name = "/id") Integer id,
            @RequestPart(name = "data") RequestEditHomePage homePage,
            @RequestPart(name = "image") List<MultipartFile> images)
    {
        return homePageService.modifyCustomHomePage(homePage,images,id);
    }
}
