package app.services.interfaces;

import app.entities.ImageData;

import java.util.List;

public interface SaveImageToDatabase <T extends ImageData> {

    List<T> saveDb(List<String> images,Object object);
}
