package app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "rekening_pembayaran")
@AllArgsConstructor
public class PaymentMethod {

    @Id
    private String bankName;

    @Column(name = "username",nullable = false)
    private String userName;

    @Column(name = "account_Number",nullable = false)
    private String accountNumber;

}
