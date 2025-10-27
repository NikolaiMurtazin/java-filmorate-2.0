package ru.yandex.practicum.filmorate.contoller; // твой пакет

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User; // Убрали лишний import ValidationException

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
        // ЭТОТ ТЕСТ ОСТАЁТСЯ, он проверяет логику контроллера (генерацию ID)
    void add_whenAllDataIsValid_shouldAddUserSuccessfully() {
        User user = new User();
        user.setEmail("123@mail.ru");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(1995, 8, 1));

        User addeduser = userController.add(user);

        assertNotNull(addeduser, "Метод add не должен возвращать null");
        assertEquals(1, addeduser.getId(), "Пользователю должен быть присвоен ID 1");
        assertEquals("Name", addeduser.getName(), "Имя пользователя не совпадает");
    }

    @Test
        // ЭТОТ ТЕСТ ОСТАЁТСЯ, он проверяет логику контроллера (подстановку имени)
    void add_whenNameIsBlank_shouldUseLoginAsName() {
        User user = new User();
        user.setEmail("123@mail.ru");
        user.setLogin("Login");
        user.setName(""); // Пустое имя
        user.setBirthday(LocalDate.of(1995, 8, 1));

        User addeduser = userController.add(user);

        // Проверяем, что контроллер сам подставил логин в имя
        assertEquals("Login", addeduser.getName(), "Имя пользователя должно было взяться из логина");
    }

    // ----- ВСЕ ОСТАЛЬНЫЕ ТЕСТЫ (email, login, birthday) УДАЛЯЕМ ОТСЮДА -----
    // Они теперь в UserValidationTest.java
}