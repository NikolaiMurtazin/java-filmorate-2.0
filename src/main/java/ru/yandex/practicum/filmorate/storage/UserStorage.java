package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    /**
     * Возвращает список всех пользователей.
     */
    List<User> getAll();

    /**
     * Находит пользователя по его ID.
     */
    User getById(int id);

    /**
     * Создаёт нового пользователя.
     */
    User add(User user);

    /**
     * Обновляет существующего пользователя.
     */
    User update(User user);
}
