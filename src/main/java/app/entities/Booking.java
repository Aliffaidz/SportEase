package app.entities;

import app.entities.enums.BookingStatus;
import app.entities.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Booking {

    public Booking(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String username;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String mobilePhoneNumber;

    @Column(name = "start_time",nullable = false)
    private LocalDateTime startTime;

    @Column(name = "ended_time",nullable = false)
    private LocalDateTime endedTime;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "booking_status",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column(name = "payment_status",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "id_field",referencedColumnName = "id")
    private Field field;

}
