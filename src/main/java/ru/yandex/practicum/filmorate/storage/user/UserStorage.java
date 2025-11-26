package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    // --- CRUD ---
    List<User> getAll();

    User getById(Long id);

    User add(User user);

    User update(User user);

    // --- ДРУЗЬЯ (Новые методы) ---

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long otherId);
}
