package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, FilmDbStorage.class})
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testCreateAndFindUserById() {
        User newUser = User.builder()
                .email("test@email.com")
                .login("testLogin")
                .name("Test Name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User savedUser = userStorage.add(newUser);

        assertThat(savedUser.getId()).isNotNull();

        User foundUser = userStorage.getById(savedUser.getId());

        assertThat(foundUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", savedUser.getId())
                .hasFieldOrPropertyWithValue("email", "test@email.com")
                .hasFieldOrPropertyWithValue("login", "testLogin");
    }

    @Test
    public void testUpdateUser() {
        User newUser = User.builder()
                .email("update@email.com")
                .login("updateLogin")
                .name("Update Name")
                .birthday(LocalDate.now())
                .build();
        User savedUser = userStorage.add(newUser);

        // Обновляем данные
        savedUser.setName("New Name");
        userStorage.update(savedUser);

        User updatedUser = userStorage.getById(savedUser.getId());

        assertThat(updatedUser.getName()).isEqualTo("New Name");
    }

    @Test
    public void testCreateAndFindFilm() {
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(new Genre(1, "Комедия"));

        Film newFilm = Film.builder()
                .name("New Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .genres(genres)
                .build();

        Film savedFilm = filmStorage.create(newFilm);
        Film foundFilm = filmStorage.getById(savedFilm.getId());

        assertThat(foundFilm.getId()).isNotNull();
        assertThat(foundFilm.getName()).isEqualTo("New Film");
        assertThat(foundFilm.getMpa().getId()).isEqualTo(1);
        assertThat(foundFilm.getGenres()).hasSize(1);
        assertThat(foundFilm.getGenres().getFirst().getId()).isEqualTo(1);
    }
}
