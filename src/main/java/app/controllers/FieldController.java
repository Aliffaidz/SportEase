package app.controllers;

import app.model.dto.AdminFieldDto;
import app.model.dto.FieldDetailDto;
import app.model.request.RequestAddField;
import app.model.request.RequestUpdateField;
import app.model.dto.Response;
import app.services.FieldService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.util.List;
@Slf4j
@RestController
@RequestMapping(path = "fields")
public class FieldController {

    private final FieldService fieldService;

    public FieldController(FieldService fieldService){
        this.fieldService = fieldService;
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Response<String> createField(@RequestPart("data") @Valid RequestAddField requestAddField, @RequestPart("images") List<MultipartFile> images){
        log.info("controller createField ");
        return fieldService.createField(requestAddField, images);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<AdminFieldDto>> getAllFields(){
        return fieldService.getAllFields();
    }


    @PatchMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Response<String> updateField(
            @RequestPart(name = "data") @Valid RequestUpdateField requestUpdateField,
            @RequestPart(name = "images") List<MultipartFile> images,
            @PathVariable(name = "id") @NotNull Integer id)
    {
        log.info("controller updateField");
        return fieldService.updateField(requestUpdateField,images,id);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> deleteField(@PathVariable(name = "id") Integer id){
        log.info("controller deleteField");
        return fieldService.deleteField(id);
    }

    @GetMapping(path = "/{id}/detail")
    public Response<FieldDetailDto> getDetailField(@PathVariable(name = "id") Integer id){
        return fieldService.getDetailField(id);
    }
}
