package app.exceptions;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FixHourValidator.class)
public @interface FixHour {

    String message() default "Waktu harus tepat di awal jam (menit harus 00)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
