package app.services.interfaces;

import app.entities.ImageData;

import java.util.List;

public interface DeleteImageToLocal <T extends ImageData> {

    void deleteLocalImage(List<? extends ImageData> imagesPath);

}
