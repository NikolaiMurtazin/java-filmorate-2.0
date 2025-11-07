package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    /**
     * Возвращает список всех фильмов.
     */
    List<Film> getAll();

    /**
     * Находит фильм по его ID.
     * @param id ID фильма
     * @return Фильм (или null/исключение, если не найден)
     */
    Film getById(int id);

    /**
     * Создаёт новый фильм.
     * @param film Новый фильм
     * @return Созданный фильм (уже с присвоенным ID)
     */
    Film create(Film film);

    /**
     * Обновляет существующий фильм.
     * @param film Фильм для обновления
     * @return Обновлённый фильм
     */
    Film update(Film film);
}