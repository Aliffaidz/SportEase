package app.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "payment_data")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_booking")
    private Integer idBooking;

    @ManyToOne
    @JoinColumn(name = "id_field", referencedColumnName = "id")
    private Field field;

    @Column(name = "username",nullable = false)
    private String username;

    @Column(name = "mobile_phone_number",nullable = false,length = 12)
    private String mobilePhoneNumber;

    @Column(name = "booking_date",nullable = false)
    private LocalDate bookingDate;

    @OneToOne(mappedBy = "idPayment")
    private ImagePaymentVerification imagePaymentVerification;
}
