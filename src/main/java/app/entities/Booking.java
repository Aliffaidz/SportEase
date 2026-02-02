package app.entities;

import app.entities.enums.BookingStatus;
import app.entities.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private LocalTime startTime;

    @Column(name = "ended_time",nullable = false)
    private LocalTime endedTime;

    @Column(name = "date",nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "image_payment_verification")
    private String imagePayment;

    @Column(name = "booking_status",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column(name = "payment_status",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_field",referencedColumnName = "id")
    private Field field;




}
