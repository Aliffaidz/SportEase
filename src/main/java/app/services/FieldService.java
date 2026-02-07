package app.services;

import java.util.List;
import java.util.Objects;

import app.entities.Field;
import app.entities.ImagesField;
import app.entities.OperationalHours;
import app.model.dto.AdminFieldDto;
import app.model.dto.FieldDetailDto;
import app.model.request.RequestAddField;
import app.model.request.RequestUpdateField;
import app.model.dto.Response;
import app.repositories.FieldRepository;
import app.repositories.ImageFieldRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class FieldService {

    private final FieldRepository fieldRepository;
    private final ImagesServiceImpl saveImagesService;
    private final ImageFieldRepository imageFieldRepository;

    public FieldService(FieldRepository fieldRepository, ImagesServiceImpl saveImagesService, ImageFieldRepository imageFieldRepository){
        this.fieldRepository = fieldRepository;
        this.saveImagesService = saveImagesService;
        this.imageFieldRepository = imageFieldRepository;
    }

    public Response<String> createField(RequestAddField requestAddField, List<MultipartFile> images){
        log.info("service createField ");
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
        List<ImagesField> imagesFieldslist = saveImagesService.saveDb(savedImageLocal, field).stream()
                .map(data -> (ImagesField) data).toList();
        imageFieldRepository.saveAll(imagesFieldslist);
        log.info("service createField response ");
        return Response.<String>builder()
                .messages("success")
                .data("success adding field " + requestAddField.fieldName())
                .build();
    }

    public Response<List<AdminFieldDto>> getAllFields(){
        log.info("service getAllFields ");
        List<Field> fields = fieldRepository.findAllFieldsWithImages();

        List<AdminFieldDto> responseData = fields.stream()
                .map(data -> new AdminFieldDto(
                        data.getId(),
                        data.getFieldName(),
                        data.getHourlyPrice(),
                        data.getOperationHours(),
                        data.getFieldType(),
                        data.getFieldStatus(),
                        data.getLocation(),
                        data.getDescription(),
                        data.getOperationDate(),
                        data.getMaximumBookingHours(),
                        data.getImagesFieldList().stream().map(ImagesField::getId).toList()
                )).toList();

        return Response.<List<AdminFieldDto>>builder()
                .messages("success")
                .data(responseData)
                .build();
    }

    public Response<String> updateField(RequestUpdateField requestUpdateField, List<MultipartFile> images ,Integer id){
        log.info("service updatedField");
        Field field = fieldRepository.getFieldById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"field with id " + id + " not found !!! ")
        );

        if(Objects.nonNull(requestUpdateField.getFieldName())){
            field.setFieldName(requestUpdateField.getFieldName());
        }
        if(Objects.nonNull(requestUpdateField.getHourlyPrice())){
            field.setHourlyPrice(requestUpdateField.getHourlyPrice());
        }
        if(Objects.nonNull(requestUpdateField.getStartTime())){
            field.getOperationHours().setStartTime(requestUpdateField.getStartTime());
        }
        if(Objects.nonNull(requestUpdateField.getEndedTime())){
            field.getOperationHours().setEndedTime(requestUpdateField.getEndedTime());
        }

        if(Objects.nonNull(requestUpdateField.getFieldType())){
            field.setFieldType(requestUpdateField.getFieldType());
        }
        if(Objects.nonNull(requestUpdateField.getFieldStatus())){
            field.setFieldStatus(requestUpdateField.getFieldStatus());
        }
        if(Objects.nonNull(requestUpdateField.getLocation())){
            field.setLocation(requestUpdateField.getLocation());
        }
        if(Objects.nonNull(requestUpdateField.getDescription())){
            field.setDescription(requestUpdateField.getDescription());
        }
        if(Objects.nonNull(requestUpdateField.getMaximumBookingHours())){
            field.setMaximumBookingHours(requestUpdateField.getMaximumBookingHours());
        }
        if(Objects.nonNull(requestUpdateField.getOperationDate())){
            field.setOperationDate(requestUpdateField.getOperationDate());
        }
        fieldRepository.save(field);
        if(!images.isEmpty()){
            log.info("service updateField change image");
            List<String> savedImageLocal = saveImagesService.saveToLocal(images);
            List<ImagesField> imagesFieldslist = saveImagesService.saveDb(savedImageLocal, field).stream()
                    .map(data -> (ImagesField) data).toList();
            imageFieldRepository.saveAll(imagesFieldslist);
        }
        log.info("service updateField response");
        return  Response.<String>builder()
                .messages("success")
                .data("success updated field " + requestUpdateField.getFieldName())
                .build();
    }

    public Response<String> deleteField(Integer id){
        log.info("service deleteField ");
        Field field = fieldRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"field with id " + id + " not found !!!")
        );

        List<ImagesField> imagesFields = imageFieldRepository.findAllByField(field);
        saveImagesService.deleteLocalImage(imagesFields);
        imageFieldRepository.deleteAll(imagesFields);
        fieldRepository.delete(field);
        log.info("service deleteField response");
        return Response.<String>builder()
                .messages("success")
                .data("success delete field " + id)
                .build();
    }

    public Response<FieldDetailDto> getDetailField(Integer idField){

        Field field = fieldRepository.findById(idField).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"field with id " + idField + " not found !!!")
        );

        FieldDetailDto detailField = new FieldDetailDto();

        detailField.setId(field.getId());
        detailField.setFieldName(field.getFieldName());
        detailField.setHourlyPrice(field.getHourlyPrice());
        detailField.setFieldType(field.getFieldType());
        detailField.setDescription(field.getDescription());
        detailField.setLocation(field.getLocation());
        detailField.setMaximumBookingHours(field.getMaximumBookingHours());
        detailField.setOperationDate(field.getOperationDate());
        detailField.setOperationalHours(field.getOperationHours());
        return Response.<FieldDetailDto>builder()
                .messages("success")
                .data(detailField)
                .build();
    }
}
