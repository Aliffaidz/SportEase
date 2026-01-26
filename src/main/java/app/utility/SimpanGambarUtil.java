package app.utility;

import app.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class SimpanGambarUtil {

    @Value("${application.image-locator}")
    private String IMAGE_PATH_LOCATION;

    @Value("${application.image-locator-payment}")
    private String IMAGE_PATH_LOCATION_PAYMENT_VERIFICATION;


    public List<GambarLapangan> saveImages(List<MultipartFile> image, Lapangan lapangan) throws IOException {

        for(MultipartFile currentGambar : image){
            if(currentGambar.isEmpty() || Objects.requireNonNull(currentGambar.getOriginalFilename()).lastIndexOf(".") < 0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"format gambar harus ber-extensi jpg,jepg dll");
            }
        }

        Path sourceLocation = Paths.get(IMAGE_PATH_LOCATION);
        createDirectory(IMAGE_PATH_LOCATION);

        List<GambarLapangan> fileName = new ArrayList<>();


        for (MultipartFile currentImage : image){
            int fileExtension = Objects.requireNonNull(currentImage.getOriginalFilename()).lastIndexOf(".");
            String buatNamaGambar = UUID.randomUUID().toString() + currentImage.getOriginalFilename().substring(fileExtension);

            Path pathName = sourceLocation.resolve(buatNamaGambar);
            log.info("PATH NAME GAMBAR " + pathName.toString());
            try(OutputStream stream = Files.newOutputStream(pathName)){
                stream.write(currentImage.getBytes());
                stream.flush();
            }
            fileName.add(new GambarLapangan(pathName.toString(),lapangan));
        }
        return fileName;
    }

    public List<GambarFasilitas> saveImages(List<MultipartFile> gambar, Fasilitas fasilitas) throws IOException {

        for(MultipartFile currentGambar : gambar){
            if(currentGambar.isEmpty() || Objects.requireNonNull(currentGambar.getOriginalFilename()).lastIndexOf(".") < 0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"format gambar harus ber-extensi jpg,jepg dll");
            }
        }

        Path sourceLocation = Paths.get(IMAGE_PATH_LOCATION);
        createDirectory(IMAGE_PATH_LOCATION);

        log.info("selesai membuat directory");
        List<GambarFasilitas> fileName = new ArrayList<>();


        for (MultipartFile gambarL : gambar){
            int fileExtension = Objects.requireNonNull(gambarL.getOriginalFilename()).lastIndexOf(".");
            String buatNamaGambar = UUID.randomUUID().toString() + gambarL.getOriginalFilename().substring(fileExtension);

            Path pathName = sourceLocation.resolve(buatNamaGambar);
            log.info("PATH NAME GAMBAR " + pathName.toString() );
            try(OutputStream stream = Files.newOutputStream(pathName)){
                stream.write(gambarL.getBytes());
                stream.flush();
            }
            fileName.add(new GambarFasilitas(pathName.toString(),fasilitas));
        }
        return fileName;
    }

    public void deleteImagesFieldFootball(List<GambarLapangan> imageFieldList){
        imageFieldList.forEach(image -> {
            String path = image.getId();
            try {
                Files.deleteIfExists(Path.of(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void hapusGambarFasilitas(List<GambarFasilitas> imageFieldList) throws IOException {
        imageFieldList.forEach(image -> {
            String path = image.getId();
            try {
                Files.deleteIfExists(Path.of(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void createDirectory(String imagePath) throws IOException {
        Path sourceLocation = Paths.get(imagePath);
        if(!Files.exists(sourceLocation)){
            log.info("membuat directory");
            Files.createDirectories(sourceLocation);
        }
    }
    public GambarPembayaran saveImagesPembayaran(MultipartFile image,Pembayaran pembayaran) throws IOException {

        if(image.isEmpty() || Objects.requireNonNull(image.getOriginalFilename()).lastIndexOf(".") < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"format gambar harus ber-extensi jpg,jepg dll");
        }

        Path sourceLocation = Paths.get(IMAGE_PATH_LOCATION_PAYMENT_VERIFICATION);
        createDirectory(IMAGE_PATH_LOCATION_PAYMENT_VERIFICATION);

        int fileExtension = Objects.requireNonNull(image.getOriginalFilename()).lastIndexOf(".");
        String buatNamaGambar = UUID.randomUUID().toString() + image.getOriginalFilename().substring(fileExtension);

        Path pathName = sourceLocation.resolve(buatNamaGambar);
        log.info("PATH NAME GAMBAR " + pathName.toString());
        try(OutputStream stream = Files.newOutputStream(pathName)) {
            stream.write(image.getBytes());
            stream.flush();
        }
        return new GambarPembayaran(pathName.toString(),pembayaran);
    }


    public String saveBackGroundImage(MultipartFile image) {
        String imageName;
        if(image.isEmpty() || Objects.requireNonNull(image.getOriginalFilename()).lastIndexOf(".") < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"format gambar harus ber-extensi jpg,jepg dll");
        }
        Path sourceLocation = Paths.get(IMAGE_PATH_LOCATION);
        try{
            createDirectory(IMAGE_PATH_LOCATION);
            int fileExtension = Objects.requireNonNull(image.getOriginalFilename()).lastIndexOf(".");
            String buatNamaGambar = UUID.randomUUID() + image.getOriginalFilename().substring(fileExtension);

            Path pathName = sourceLocation.resolve(buatNamaGambar);
            imageName = pathName.toString();
            log.info("PATH NAME GAMBAR " + pathName);
            try(OutputStream stream = Files.newOutputStream(pathName)){
                stream.write(image.getBytes());
                stream.flush();
            }
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"gagal menyimpan gambar background");
        }
        return imageName;
    }
}
