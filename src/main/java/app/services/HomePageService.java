package app.services;

import app.dto.HomeDto;
import app.dto.admin.FasilitasDto;
import app.entities.HomePage;
import app.entities.Lapangan;
import app.entities.SocialMedia;
import app.models.home_page.RequestCustomHomePage;
import app.models.home_page.RequestModifyHomePage;
import app.repositories.HomePageRepository;
import app.repositories.LapanganRepository;
import app.utility.SimpanGambarUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class HomePageService {

    private HomePageRepository homePageRepository;
    private SimpanGambarUtil simpanGambarUtil;
    private LapanganRepository lapanganRepository;
    private FasilitasService fasilitasService;

    public HomePageService(HomePageRepository homePageRepository,SimpanGambarUtil simpanGambarUtil,LapanganRepository lapanganRepository,FasilitasService fasilitasService){
        this.homePageRepository = homePageRepository;
        this.simpanGambarUtil = simpanGambarUtil;
        this.lapanganRepository = lapanganRepository;
        this.fasilitasService = fasilitasService;
    }

    public void add(RequestCustomHomePage modifyHomePage, MultipartFile image){

        HomePage homePage = new HomePage();

        homePage.setTitle(modifyHomePage.getTitle());
        homePage.setAddress(modifyHomePage.getAddress());
        homePage.setContact(modifyHomePage.getContact());
        homePage.setJamOperasional(modifyHomePage.getJamOperasional());
        SocialMedia socialMedia = new SocialMedia();
        if(Objects.nonNull(modifyHomePage.getFacebook())){
            socialMedia.setFacebook(modifyHomePage.getFacebook());
        }

        if(Objects.nonNull(modifyHomePage.getTiktok())){
            socialMedia.setTiktok(modifyHomePage.getTiktok());
        }

        if(Objects.nonNull(modifyHomePage.getInstagram())){
            socialMedia.setInstagram(modifyHomePage.getInstagram());
        }
        homePage.setSocialMedia(socialMedia);
        String backGroundImage = simpanGambarUtil.saveBackGroundImage(image);
        homePage.setImageBackground(backGroundImage);

        log.info("buat home page baru ===============================");
        homePageRepository.save(homePage);
    }

    public HomePage get(){
        List<HomePage> homePages = homePageRepository.findAll();
        if(homePages.isEmpty()){
            return null;
        }
        return homePages.getFirst();
    }

    public HomeDto getHome(){
        List<HomePage> homePage = homePageRepository.findAll();
        List<FasilitasDto> fasilitasDtoList = fasilitasService.ambilSemuaFasilitas();

        HomeDto homeDto = new HomeDto();
        homeDto.setFasilitasDtoList(fasilitasDtoList);

        if(homePage.isEmpty()){
            homeDto.setHomePage(null);
        }else {
            homeDto.setHomePage(homePage.getFirst());
        }

        if(fasilitasDtoList.isEmpty()){
            homeDto.setFasilitasDtoList(null);
        }else {
            homeDto.setFasilitasDtoList(fasilitasDtoList);
        }
        return homeDto;
    }

    public void update(Integer id,RequestModifyHomePage modifyHomePage, MultipartFile image){
        log.info("edit home page =============================");
        HomePage homepage = homePageRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"tidak ditemukan")
        );

        if(Objects.nonNull(modifyHomePage.getTitle())){
            homepage.setTitle(modifyHomePage.getTitle());
        }

        if(Objects.nonNull(modifyHomePage.getAddress())){
            homepage.setAddress(modifyHomePage.getAddress());
        }
        if(Objects.nonNull(modifyHomePage.getContact())){
            homepage.setContact(modifyHomePage.getContact());
        }

        if(Objects.nonNull(modifyHomePage.getJamOperasional())){
            homepage.setJamOperasional(modifyHomePage.getJamOperasional());
        }

        if(Objects.nonNull(modifyHomePage.getFacebook())){
            homepage.getSocialMedia().setFacebook(modifyHomePage.getFacebook());
        }

        if(Objects.nonNull(modifyHomePage.getInstagram())){
            homepage.getSocialMedia().setInstagram(modifyHomePage.getInstagram());
        }

        if(Objects.nonNull(modifyHomePage.getTiktok())){
            homepage.getSocialMedia().setTiktok(modifyHomePage.getTiktok());
        }

        if(Objects.nonNull(modifyHomePage.getImageBackground()) && image != null){
                String backGroundImage = simpanGambarUtil.saveBackGroundImage(image);
                log.info("selsai simpan gambar di local====================");
                homepage.setImageBackground(backGroundImage);
                log.info("selsai hapus gambar di local====================");
        }
        log.info("edit simpan perubahan home page =============================");
        homePageRepository.save(homepage);
        fileDeleteAsync(modifyHomePage.getImageBackground());
    }

    @Async
    public void fileDeleteAsync(String path) {
        try {
            Thread.sleep(300);
            Files.deleteIfExists(Path.of(path));
        } catch (Exception ignored) {}
    }



}
