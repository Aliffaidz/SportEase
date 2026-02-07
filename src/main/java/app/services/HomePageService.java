package app.services;

import app.entities.HomePage;
import app.model.dto.Response;
import app.model.request.RequestCustomHomePage;
import app.model.request.RequestEditHomePage;
import app.repositories.HomePageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HomePageService {

    private final HomePageRepository homePageRepository;
    private final ImagesServiceImpl imagesService;

    public HomePageService(HomePageRepository homePageRepository, ImagesServiceImpl imagesService) {
        this.homePageRepository = homePageRepository;
        this.imagesService = imagesService;
    }

    public Response<String> customHomePage(RequestCustomHomePage customHomePage, List<MultipartFile> image){

        HomePage homePage = new HomePage();
        homePage.setTitle(customHomePage.getTitle());
        homePage.setOperationHours(customHomePage.getOperationalHours());
        homePage.setAddress(customHomePage.getAddress());
        homePage.setContact(customHomePage.getContact());

        List<String> imageBackGround = imagesService.saveToLocal(image);

        homePage.setImageBackground(imageBackGround.getFirst());
        homePage.setSocialMedia(customHomePage.getSocialMedia());

        homePageRepository.save(homePage);
        return Response.<String>builder()
                .messages("success")
                .data("success custom home page")
                .build();
    }

    public Response<HomePage> get(){
        HomePage homePage = homePageRepository.findAll().getFirst();
        return Response.<HomePage>builder()
                .messages("success")
                .data(homePage)
                .build();
    }

    public Response<String> modifyCustomHomePage(RequestEditHomePage homePage,List<MultipartFile> image,Integer id){

        HomePage page = homePageRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"not found")
        );

        if(homePage.getTitle() != null){
            page.setTitle(homePage.getTitle());
        }

        if(homePage.getAddress() != null){
            page.setAddress(homePage.getAddress());
        }

        if(homePage.getContact() != null){
            page.setContact(homePage.getContact());
        }

        if(homePage.getOperationalHours() != null){
            page.setOperationHours(homePage.getOperationalHours());
        }

        if(homePage.getFacebook() != null){
            page.getSocialMedia().setFacebook(homePage.getFacebook());
        }

        if(homePage.getInstagram() != null){
            page.getSocialMedia().setInstagram(homePage.getInstagram());
        }

        if(homePage.getTiktok() != null){
            page.getSocialMedia().setTiktok(homePage.getTiktok());
        }

        if(!image.isEmpty()){
            List<String> newImage = imagesService.saveToLocal(image);
            page.setImageBackground(newImage.getFirst());
        }

        homePageRepository.save(page);
        return Response.<String>builder()
                .messages("success")
                .data("success modify home page")
                .build();
    }


}
