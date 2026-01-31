package app.services;

import java.util.List;
import app.entities.Field;
import app.entities.ImageData;
import app.entities.ImagesField;
import app.entities.OperationalHours;
import app.model.request.RequestAddField;
import app.model.response.Response;
import app.repositories.FieldRepository;
import app.repositories.ImageFieldRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FieldService {

    private final FieldRepository fieldRepository;
    private final SaveImagesServiceImpl saveImagesService;
    private final ImageFieldRepository imageFieldRepository;

    public FieldService(FieldRepository fieldRepository, SaveImagesServiceImpl saveImagesService, ImageFieldRepository imageFieldRepository){
        this.fieldRepository = fieldRepository;
        this.saveImagesService = saveImagesService;
        this.imageFieldRepository = imageFieldRepository;
    }

    public Response<String> createField(RequestAddField requestAddField, List<MultipartFile> images){

        Field field = new Field();

        field.setFieldName(requestAddField.fieldName());
        field.setHourlyPrice(requestAddField.hourlyPrice());
        field.setOperationHours(new OperationalHours(requestAddField.startTime(),requestAddField.endedTime()));
        field.setFieldType(requestAddField.fieldType());
        field.setFieldStatus(requestAddField.fieldStatus());
        field.setLocation(requestAddField.location());
        field.setDescription(requestAddField.description());
        field.setOperationDate(requestAddField.operationDate());
        field.setMaximumBookingHours(requestAddField.maximumBookingHours());

        fieldRepository.save(field);

        List<String> savedImageLocal = saveImagesService.saveToLocal(images);
        saveImagesService.saveDb(savedImageLocal,field)
                .forEach(imageData -> {
                    imageFieldRepository.save( (ImagesField) imageData);
                });
        return Response.<String>builder()
                .messages("succes")
                .data(null)
                .build();
    }



}
