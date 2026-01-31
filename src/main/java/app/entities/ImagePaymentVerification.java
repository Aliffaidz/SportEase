package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "image_payment_verification")
@NoArgsConstructor
@AllArgsConstructor
public class ImagePaymentVerification extends ImageData {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "id_payment",referencedColumnName = "id")
    private Payment idPayment;

}
