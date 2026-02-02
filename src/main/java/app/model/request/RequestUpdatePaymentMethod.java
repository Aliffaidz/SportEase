package app.model.request;

import lombok.Data;

@Data
public class RequestUpdatePaymentMethod {

    private String username;
    private String accountNumber;

}
