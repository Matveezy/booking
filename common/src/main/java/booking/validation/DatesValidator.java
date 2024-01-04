package booking.validation;

import booking.dto.DateInOutDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DatesValidator implements ConstraintValidator<Dates, DateInOutDto> {

    @Override
    public boolean isValid(DateInOutDto dateInOutDto, ConstraintValidatorContext constraintValidatorContext) {
        return !dateInOutDto.in().isAfter(dateInOutDto.out());
    }
}
