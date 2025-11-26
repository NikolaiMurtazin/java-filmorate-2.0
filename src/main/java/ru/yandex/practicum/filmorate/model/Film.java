package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.Update;
import ru.yandex.practicum.filmorate.validation.ValidReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private static final int FILM_DESCRIPTION_MAX_LENGTH = 200;

    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = FILM_DESCRIPTION_MAX_LENGTH, message = "Описание фильма не должно превышать 200 символов")
    private String description;

    @NotNull
    @ValidReleaseDate
    private LocalDate releaseDate;

    @NotNull

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;

    private Mpa mpa;

    @Builder.Default
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();

    @Builder.Default
    private Set<Long> likes = new HashSet<>();
}