package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.util.Constants.MIN_RELEASE_DATE;

public class ReleaseDateValidator implements ConstraintValidator<ValidReleaseDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }

        return date.isAfter(MIN_RELEASE_DATE) || date.isEqual(MIN_RELEASE_DATE);
    }
}
