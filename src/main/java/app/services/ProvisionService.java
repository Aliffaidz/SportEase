package app.services;

import app.entities.Provision;
import app.model.dto.Response;
import app.repositories.ProvisionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProvisionService {

    private final ProvisionRepository provisionRepository;

    public ProvisionService(ProvisionRepository provisionRepository) {
        this.provisionRepository = provisionRepository;
    }

    public Response<String> addProvision(String prov){
        provisionRepository.save(new Provision(prov));
        return Response.<String>builder()
                .messages("success")
                .data("success add provision")
                .build();
    }

    public Response<List<Provision>> getAllProvisions(){
        return Response.<List<Provision>>builder()
                .messages("success")
                .data(provisionRepository.findAll())
                .build();
    }

    public Response<String> editProvision(String data,Integer id){
        Provision provision = provisionRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND," provision with id " + id + " not found !!!")
        );
        if(data != null && !data.isBlank()){
            provision.setProvision(data);
        }
        provisionRepository.save(provision);
        return Response.<String>builder()
                .messages("success")
                .data("success edit provision")
                .build();
    }

    public Response<String> deleteProvision(Integer id){
        Provision provision = provisionRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND," provision with id " + id + " not found !!!")
        );
        provisionRepository.delete(provision);
        return Response.<String>builder()
                .messages("success")
                .data("success delete provision")
                .build();
    }

}
