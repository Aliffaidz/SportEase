package app.exceptions;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotAfterValidator.class)
public @interface NotAfter {

    String value(); // Jam batas, misal "23:00"
    String message() default "Waktu tidak boleh lebih dari {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
