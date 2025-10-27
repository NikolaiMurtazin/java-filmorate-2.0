package ru.yandex.practicum.filmorate.contoller; // или твой пакет, где лежит User

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void create_whenAllFieldsValid_shouldNotHaveViolations() {
        User user = new User();
        user.setEmail("123@mail.ru");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(1995, 8, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Для валидного пользователя не должно быть ошибок");
    }

    @Test
    void create_whenEmailIsBlank_shouldHaveViolation() {
        User user = new User();
        user.setEmail(" ");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(1995, 8, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Электронная почта не может быть пустой", violations.iterator().next().getMessage());
    }

    @Test
    void create_whenEmailIsInvalid_shouldHaveViolation() {
        User user = new User();
        user.setEmail("123mail.ru");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(1995, 8, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Электронная почта должна быть в корректном формате", violations.iterator().next().getMessage());
    }

    @Test
    void create_whenLoginIsBlank_shouldHaveViolation() {
        User user = new User();
        user.setEmail("123@mail.ru");
        user.setLogin(" ");
        user.setBirthday(LocalDate.of(1995, 8, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Логин не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void create_whenLoginHasSpaces_shouldHaveViolation() {
        User user = new User();
        user.setEmail("123@mail.ru");
        user.setLogin("my login");
        user.setBirthday(LocalDate.of(1995, 8, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Логин не должен содержать пробелы", violations.iterator().next().getMessage());
    }

    @Test
    void create_whenBirthdayInFuture_shouldHaveViolation() {
        User user = new User();
        user.setEmail("123@mail.ru");
        user.setLogin("Login");
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Дата рождения не может быть в будущем", violations.iterator().next().getMessage());
    }
}
