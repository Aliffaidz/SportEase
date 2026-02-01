package app.exceptions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class NotAfterValidator implements ConstraintValidator<NotAfter, LocalTime> {
    private LocalTime limitTime;

    @Override
    public void initialize(NotAfter constraintAnnotation) {
        // Mengambil nilai "23:00" dari anotasi dan mengubahnya jadi LocalTime
        this.limitTime = LocalTime.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
        if (value == null) return true; // Biarkan @NotNull yang menangani jika kosong
        // Return true jika waktu user TIDAK melewati limitTime
        return !value.isAfter(limitTime);
    }
}