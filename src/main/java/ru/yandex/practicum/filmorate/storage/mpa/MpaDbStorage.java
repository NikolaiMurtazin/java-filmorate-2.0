package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage { // <-- implements

    private final JdbcTemplate jdbcTemplate;

    @Override // <-- Добавили
    public List<Mpa> getAll() {
        String sql = "SELECT * FROM mpas ORDER BY mpa_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(
                rs.getInt("mpa_id"),
                rs.getString("name"))
        );
    }

    @Override // <-- Добавили
    public Mpa getById(int id) {
        String sql = "SELECT * FROM mpas WHERE mpa_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Mpa(
                    rs.getInt("mpa_id"),
                    rs.getString("name")), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинг с ID " + id + " не найден");
        }
    }
}
