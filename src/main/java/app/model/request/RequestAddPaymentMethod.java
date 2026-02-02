package app.model.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestAddPaymentMethod {

    @NotBlank(message = "bank atau e-wallet harus di isi")
    private String bankName;

    @NotBlank(message = "nama pemilik rekening harus di isi")
    private String userName;

    @NotBlank(message = "nomor rekening bank atau e-wallet harus di isi")
    private String accountNumber;

}
