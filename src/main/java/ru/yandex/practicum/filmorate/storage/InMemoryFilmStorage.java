package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage  implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @Override
    public List<Film> getAll() {
        log.debug("Запрошен список всех фильмов. Текущее количество: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(int id) {
        log.debug("Запрошен фильм с ID: {}", id);
        if (!films.containsKey(id)) {
            log.warn("Фильм с ID {} не найден.", id);
            throw new NotFoundException("Фильм с ID " + id + " не найден.");
        }
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        int id = getNextId();
        film.setId(id);
        films.put(film.getId(), film);
        log.info("Фильм успешно создан: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Попытка обновить несуществующий фильм с ID: {}", film.getId());
            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден.");
        }
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлен: {}", film);
        return film;
    }

    private int getNextId() {
        return nextId++;
    }
}
