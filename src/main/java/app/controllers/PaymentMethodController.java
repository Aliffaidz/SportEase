package app.controllers;

import app.entities.PaymentMethod;
import app.model.dto.Response;
import app.model.request.RequestAddPaymentMethod;
import app.model.request.RequestUpdatePaymentMethod;
import app.services.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    //add
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> addPaymentMethod(@RequestBody @Valid RequestAddPaymentMethod method){
        return paymentMethodService.addPaymentMethod(method);
    }
    //getall

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<List<PaymentMethod>> getAllPaymentMethods(){
        return paymentMethodService.getAllPaymentMethods();
    }
    //update

    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> editPaymentMethod(@RequestBody RequestUpdatePaymentMethod method,@PathVariable(name = "id") String id){
        return paymentMethodService.editPaymentMethod(method,id);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> deletePaymentMethod(@PathVariable(name = "id") String id){
        return paymentMethodService.deletePaymentMethod(id);
    }
    //delete

}
