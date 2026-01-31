package app.services;

import app.entities.ImageData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SaveImageToDatabase <T extends ImageData> {

    List<T> saveDb(List<String> images,Object object);
}
