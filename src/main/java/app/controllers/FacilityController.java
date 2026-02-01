package app.controllers;

import app.entities.ImageFacility;
import app.model.dto.FacilityDto;
import app.model.dto.Response;
import app.model.request.RequestAddFacility;
import app.model.request.RequestUpdateFacility;
import app.services.FacilityService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping(path = "facilities")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Response<String> addFacility(@RequestPart(name = "data") @Valid RequestAddFacility requestAddFacility,
                                        @RequestPart(name = "images") List<MultipartFile> images){
        return facilityService.addFacility(requestAddFacility,images);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<FacilityDto>> getAllFacilities(){
        return facilityService.getAllFacility();
    }

    @PatchMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Response<String> updateFacility(@RequestPart(name = "data") RequestUpdateFacility requestUpdateFacility,
                                           @RequestPart(name = "images") List<MultipartFile> images,
                                           @PathVariable(name = "id")Integer id)
    {
        return facilityService.updateFacility(requestUpdateFacility,images,id);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> deleteFacility(@PathVariable(name = "id")Integer id){
        return facilityService.deleteFacility(id);
    }

}
