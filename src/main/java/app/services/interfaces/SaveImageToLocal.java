package app.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SaveImageToLocal {

    List<String> saveToLocal(List<MultipartFile> images);
}
