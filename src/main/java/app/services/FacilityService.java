package app.services;

import app.entities.Facility;
import app.entities.ImageFacility;
import app.model.dto.FacilityDto;
import app.model.dto.Response;
import app.model.request.RequestAddFacility;
import app.model.request.RequestUpdateFacility;
import app.repositories.FacilityRepository;
import app.repositories.ImageFacilityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
@Slf4j
@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final ImagesServiceImpl saveImagesService;
    private final ImageFacilityRepository imageFacilityRepository;

    public FacilityService(FacilityRepository facilityRepository, ImagesServiceImpl saveImagesService, ImageFacilityRepository imageFacilityRepository){
        this.facilityRepository = facilityRepository;
        this.saveImagesService = saveImagesService;
        this.imageFacilityRepository = imageFacilityRepository;
    }

    public Response<String> addFacility(RequestAddFacility requestAddFacility, List<MultipartFile> images){
        log.info("service addFacility");
        Facility facility = new Facility();

        facility.setName(requestAddFacility.getFacilityName());
        facility.setDescription(requestAddFacility.getDescription());
        facility.setTimeAdded(LocalDateTime.now());

        facilityRepository.save(facility);
        List<String> savedImageLocal = saveImagesService.saveToLocal(images);
        List<ImageFacility> imageDataFacility = saveImagesService.saveDb(savedImageLocal, facility).stream()
                .map(img -> (ImageFacility) img).toList();
        imageFacilityRepository.saveAll(imageDataFacility);
        log.info("service addFacility response");
        return Response.<String>builder()
                .messages("success")
                .data("success add facility " + requestAddFacility.getFacilityName())
                .build();
    }

    public Response<List<FacilityDto>> getAllFacility(){
        List<Facility> facilities = facilityRepository.getAllFacilityWithImage();
        List<FacilityDto> facilityDtoList = facilities.stream()
                .map(data -> new FacilityDto(
                        data.getId(),
                        data.getName(),
                        data.getDescription(),
                        data.getImageFacilities().stream().map(ImageFacility::getId).toList())

                ).toList();
        log.info("service getAllFacility response");
        return Response.<List<FacilityDto>>builder()
                .messages("success")
                .data(facilityDtoList)
                .build();
    }

    public Response<String> updateFacility(RequestUpdateFacility requestUpdateFacility,List<MultipartFile> images,Integer id){
        log.info("service updateFacility ");
        Facility facility = facilityRepository.getFacilityById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"facility with id " + id +  " not found !!!")
        );

        if(Objects.nonNull(requestUpdateFacility.getFacilityName())){
            facility.setName(requestUpdateFacility.getFacilityName());
        }

        if(Objects.nonNull(requestUpdateFacility.getDescription())){
            facility.setDescription(requestUpdateFacility.getDescription());
        }

        facilityRepository.save(facility);

        if(!images.isEmpty()){
            List<String> savedLocalImage = saveImagesService.saveToLocal(images);
            List<ImageFacility> imageDataFacility = saveImagesService.saveDb(savedLocalImage, facility).stream()
                    .map(img -> (ImageFacility) img).toList();
            imageFacilityRepository.saveAll(imageDataFacility);
        }
        log.info("service facilityUpdate response");
        return Response.<String>builder()
                .messages("success")
                .data("success update facility " + requestUpdateFacility.getFacilityName())
                .build();
    }

    public Response<String> deleteFacility(Integer id){
        log.info("service deleteFacility ");
        Facility facility = facilityRepository.getFacilityById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"facility with id " + id +  " not found !!!")
        );

        List<ImageFacility> facilityImages = imageFacilityRepository.findAllByFacility(facility);
        imageFacilityRepository.deleteAll(facilityImages);
        facilityRepository.delete(facility);
        log.info("service deleteFacility response");
        return Response.<String>builder()
                .messages("success")
                .data("success delete facility")
                .build();
    }


}
