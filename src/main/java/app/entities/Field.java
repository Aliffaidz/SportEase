package app.entities;

import app.entities.enums.FieldType;
import app.entities.enums.FieldStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity

public class Field {

    public Field(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String fieldName;

    @Column(nullable = false,precision =  10)
    private BigDecimal hourlyPrice;

    @Embedded
    private OperationalHours operationHours;

    @Column(name = "field_type",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private FieldType fieldType;

    @Column(name = "status_field",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private FieldStatus fieldStatus;

    @Column(columnDefinition = "VARCHAR (255) NOT NULL")
    private String location;

    @Lob
    private String description;

    @Column(name = "operation_date",nullable = false)
    private LocalDate operationDate;

    @Column(name = "maximum_booking_hours",nullable = false)
    private Integer maximumBookingHours;

    @JsonIgnore
    @OneToMany(mappedBy = "field",fetch = FetchType.EAGER)
    private List<Payment> payments;

    @JsonIgnore
    @OneToMany(mappedBy = "field",fetch = FetchType.EAGER)
    private List<Booking> bookings;

    @JsonIgnore
    @OneToMany(mappedBy = "field",fetch = FetchType.EAGER)
    private List<ImagesField> imagesFieldList;

}

