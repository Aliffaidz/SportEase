package app.services;

import app.entities.PaymentMethod;
import app.model.dto.Response;
import app.model.request.RequestAddPaymentMethod;
import app.model.request.RequestUpdatePaymentMethod;
import app.repositories.PaymentMethodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }


    public Response<String> addPaymentMethod(RequestAddPaymentMethod method){
        PaymentMethod paymentMethod = new PaymentMethod(
                method.getBankName(),
                method.getUserName(),
                method.getAccountNumber()
        );
        paymentMethodRepository.save(paymentMethod);
        return Response.<String>builder()
                .messages("success")
                .data("success add payment method " + method.getBankName())
                .build();
    }

    public Response<List<PaymentMethod>> getAllPaymentMethods(){
        return Response.<List<PaymentMethod>>builder()
                .messages("success")
                .data(paymentMethodRepository.findAll())
                .build();
    }

    public Response<String> editPaymentMethod(RequestUpdatePaymentMethod method,String id){
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"payment method with id " + id + " not found !!!")
        );

        if(method.getUsername() != null && !method.getUsername().isBlank()){
            paymentMethod.setUserName(method.getUsername());
        }

        if(method.getAccountNumber() != null && !method.getAccountNumber().isBlank()){
            paymentMethod.setAccountNumber(method.getAccountNumber());
        }

        paymentMethodRepository.save(paymentMethod);
        return Response.<String>builder()
                .messages("success")
                .data("success edit payment method " + id)
                .build();
    }

    public Response<String> deletePaymentMethod(String id){
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"payment method with id " + id + " not found !!!")
        );
        paymentMethodRepository.delete(paymentMethod);
        return Response.<String>builder()
                .messages("success")
                .data("success delete payment method " + id)
                .build();
    }

}
