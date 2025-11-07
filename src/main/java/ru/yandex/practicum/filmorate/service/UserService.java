package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getAll() {
        log.debug("Сервис: Запрошен список всех пользователей");
        return userStorage.getAll();
    }

    public User getById(int id) {
        log.debug("Сервис: Запрошен пользователь по ID: {}", id);
        return userStorage.getById(id);
    }

    public User create(User user) {
        log.debug("Сервис: Запрос на создание пользователя: {}", user);
        validateName(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        log.debug("Сервис: Запрос на обновление пользователя: {}", user);
        getById(user.getId());
        validateName(user);
        return userStorage.update(user);
    }


    public List<User> getFriendsList(int userId) {
        log.debug("Сервис: Запрошен список друзей для пользователя {}", userId);
        User user = userStorage.getById(userId);
        Set<Integer> friendIds = user.getFriends();
        return friendIds.stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        log.debug("Сервис: Запрошен список общих друзей для {} и {}", userId, otherId);
        Set<Integer> userFriendIds = userStorage.getById(userId).getFriends();
        Set<Integer> otherFriendIds = userStorage.getById(otherId).getFriends();
        Set<Integer> commonIds = new HashSet<>(userFriendIds);
        commonIds.retainAll(otherFriendIds); // Идеальное решение
        return commonIds.stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            log.warn("Попытка добавить в друзья самого себя (ID: {})", userId);
            throw new ValidationException("Нельзя добавить в друзья самого себя.");
        }

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user.getFriends().contains(friendId) || friend.getFriends().contains(userId)) {
            log.warn("Попытка добавить в друзья пользователей, которые уже друзья: {} и {}", userId, friendId);
            throw new ValidationException("Пользователи уже являются друзьями.");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        log.info("Пользователь {} и {} теперь друзья.", user.getLogin(), friend.getLogin());
    }

    public void removeFriend(int userId, int friendId) {
        if (userId == friendId) {
            log.warn("Попытка удалить из друзей самого себя (ID: {})", userId);
            throw new ValidationException("Нельзя удалить из друзей самого себя.");
        }

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (!user.getFriends().contains(friendId) || !friend.getFriends().contains(userId)) {
            log.warn("Попытка удалить из друзей пользователей, которые не являются друзьями: {} и {}", userId, friendId);
            throw new ValidationException("Пользователи не являются друзьями.");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        log.info("Пользователь {} и {} теперь не друзья.", user.getLogin(), friend.getLogin());
    }

    private void validateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя пустое, используем логин: {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
