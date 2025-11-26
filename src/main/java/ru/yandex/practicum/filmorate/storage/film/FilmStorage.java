package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    // --- CRUD ---
    List<Film> getAll();

    Film getById(Long id);

    Film create(Film film);

    Film update(Film film);

    // --- ЛАЙКИ (Новые методы) ---
    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);
}