package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    private final Set<String> emails = new HashSet<>();
    private final Set<String> logins = new HashSet<>();

    @Override
    public List<User> getAll() {
        log.debug("Запрошен список всех пользователей. Текущее количество: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(int id) {
        log.debug("Запрошен пользователь с ID: {}", id);
        if (!users.containsKey(id)) {
            log.warn("Пользователь с ID {} не найден.", id);
            throw new NotFoundException("Пользователь с ID " + id + " не найден.");
        }
        return users.get(id);
    }

    @Override
    public User add(User user) {
        if (emails.contains(user.getEmail())) {
            throw new ValidationException("Этот email уже занят.");
        }
        if (logins.contains(user.getLogin())) {
            throw new ValidationException("Этот login уже занят.");
        }

        int id = getNextId();
        user.setId(id);
        users.put(id, user);

        emails.add(user.getEmail());
        logins.add(user.getLogin());

        log.info("Пользователь успешно создан: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Попытка обновить несуществующего пользователя с ID: {}", user.getId());
            throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден.");
        }

        User oldUser = users.get(user.getId());

        if (!oldUser.getEmail().equals(user.getEmail())) {
            if (emails.contains(user.getEmail())) {
                log.warn("Попытка сменить email на уже существующий: {}", user.getEmail());
                throw new ValidationException("Этот email уже занят.");
            }
            emails.remove(oldUser.getEmail());
            emails.add(user.getEmail());
        }

        if (!oldUser.getLogin().equals(user.getLogin())) {
            if (logins.contains(user.getLogin())) {
                log.warn("Попытка сменить login на уже существующий: {}", user.getLogin());
                throw new ValidationException("Этот login уже занят.");
            }
            logins.remove(oldUser.getLogin());
            logins.add(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь успешно обновлен: {}", user);
        return user;
    }

    private int getNextId() {
        return nextId++;
    }
}
