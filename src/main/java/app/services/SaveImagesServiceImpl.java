package app.services;
import app.entities.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SaveImagesServiceImpl implements SaveImageToLocal, SaveImageToDatabase<ImageData> {

    @Value("${application.image-locator}")
    private String IMAGE_FIELD_LOCATION;

    @Override
    public List<String> saveToLocal(List<MultipartFile> images){
        isValidImage(images);
        List<String> imageLocation = new ArrayList<>();
        for (MultipartFile current : images){
            String generateImageName = UUID.randomUUID().toString() + current.getOriginalFilename().lastIndexOf(".");
            Path location = Path.of(IMAGE_FIELD_LOCATION);
            Path imageName = location.resolve(generateImageName);
            imageLocation.add(imageName.toString());
            try{
                try(OutputStream outputStream = Files.newOutputStream(imageName)){
                    outputStream.write(current.getBytes());
                    outputStream.flush();
                }
            }catch (IOException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"gambar mungkin rusak");
            }
        }
        return imageLocation;
    }

    @Override
    public List<ImageData> saveDb(List<String> images, Object object){
        List<ImageData> imageDataList = new ArrayList<>();
        for (String imageName : images){
            if(object instanceof Field){
                ImagesField imageField = new ImagesField();
                imageField.setId(imageName);
                imageField.setField((Field) object);
                imageDataList.add(imageField);
            } else if (object instanceof Facility) {
                ImageFacility imageFacility = new ImageFacility();
                imageFacility.setId(imageName);
                imageFacility.setFacility((Facility) object);
                imageDataList.add(imageFacility);
            } else if (object instanceof Payment) {
                ImagePaymentVerification imagePaymentVerification = new ImagePaymentVerification();
                imagePaymentVerification.setId(imageName);
                imagePaymentVerification.setIdPayment((Payment) object);
                imageDataList.add(imagePaymentVerification);
            }
        }
        return imageDataList;
    }

    private void isValidImage(List<MultipartFile> image){
        for (MultipartFile current : image){
            int img = Objects.requireNonNull(current.getOriginalFilename()).lastIndexOf(".");

            if(img < 0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"gambar yang diupload tidak valid");
            }
        }
    }


}
