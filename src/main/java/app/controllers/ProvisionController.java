package app.controllers;

import app.entities.Provision;
import app.model.dto.Response;
import app.services.ProvisionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "provisions")
public class ProvisionController {

    private final ProvisionService provisionService;

    public ProvisionController(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> addProvision(@RequestParam(name = "provision")String prov){
        return provisionService.addProvision(prov);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<Provision>> getAllProvisions(){
        return provisionService.getAllProvisions();
    }

    @PutMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> editProvision(@PathVariable(name = "id") Integer id,@RequestParam(name = "edit") String prov ){
        return provisionService.editProvision(prov,id);
    }

    @DeleteMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> deleteProvision(@PathVariable(name = "id") Integer id){
        return provisionService.deleteProvision(id);
    }
}
