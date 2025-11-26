package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public List<Film> getAll() {
        log.debug("Сервис: Запрошен список всех фильмов");
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        log.debug("Сервис: Запрошен фильм по ID: {}", id);
        return filmStorage.getById(id);
    }

    public Film create(Film film) {
        log.debug("Сервис: Запрос на создание фильма: {}", film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.debug("Сервис: Запрос на обновление фильма: {}", film);
        getById(film.getId());
        return filmStorage.update(film);
    }

    public List<Film> getPopularFilms(int count) {
        log.debug("Запрошен список {} самых популярных фильмов.", count);

        if (count <= 0) {
            log.warn("Запрошено некорректное количество фильмов: {}", count);
            throw new ValidationException("Параметр 'count' должен быть положительным.");
        }

        List<Film> allFilms = filmStorage.getAll();

        return allFilms.stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLike(Long filmId, Long userId) {
        log.debug("Запрос на добавление лайка от пользователя {} фильму {}", userId, filmId);
        Film film = filmStorage.getById(filmId);
        User user = userService.getById(userId);

        boolean success = film.getLikes().add(userId);

        if (!success) {
            log.warn("Пользователь {} уже лайкал фильм {}", userId, filmId);
            throw new ValidationException("Пользователь уже поставил лайк этому фильму.");
        }

        log.info("Пользователь {} успешно поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.debug("Запрос на удаление лайка от пользователя {} фильму {}", userId, filmId);
        Film film = filmStorage.getById(filmId);
        User user = userService.getById(userId);

        boolean success = film.getLikes().remove(userId);

        if (!success) {
            log.warn("Пользователь {} пытался удалить несуществующий лайк с фильма {}", userId, filmId);
            throw new ValidationException("Пользователь не ставил лайк этому фильму.");
        }

        log.info("Пользователь {} удалил лайк с фильма {}", userId, filmId);
    }
}
