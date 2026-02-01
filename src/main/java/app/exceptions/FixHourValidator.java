package app.exceptions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class FixHourValidator implements ConstraintValidator<FixHour, LocalTime> {


    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null){
            return true;
        }
        return value.getMinute() == 0 && value.getSecond() == 0;
    }
}
