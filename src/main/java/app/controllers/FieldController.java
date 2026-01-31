package app.controllers;

import app.model.request.RequestAddField;
import app.model.response.Response;
import app.services.FieldService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class FieldController {

    private final FieldService fieldService;

    public FieldController(FieldService fieldService){
        this.fieldService = fieldService;
    }

    @PostMapping(path = "field")
    public Response<String> createField(@RequestBody @Valid RequestAddField requestAddField, @RequestPart @Valid List<MultipartFile> images){
        return fieldService.createField(requestAddField,images);
    }

}
