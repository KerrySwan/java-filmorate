package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component
public class GenreDbStorage implements GenreStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public GenreDbStorage(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getGenres() {
        String sql = "select id, genre from genres";
        SqlRowSet genreRows =
                jdbcTemplate.queryForRowSet(sql);
        List<Genre> genres = new ArrayList<>();
        while (genreRows.next()) {
            genres.add(new Genre(genreRows.getLong("id"), genreRows.getString("genre")));
        }
        return genres;
    }


    @Override
    public Genre getGenre(long genreId) {
        String sql = "select id, genre from genres g where id = ?";
        SqlRowSet genreRows =
                jdbcTemplate.queryForRowSet(sql, genreId);
        if (genreRows.next()) {
            return new Genre(genreRows.getLong("id"), genreRows.getString("genre"));
        }
        return null;
    }

    @Override
    public Set<Genre> getGenresForFilm(long filmId) {
        String sql = "select f.genre_id, g.genre from filmGenres f " +
                "inner join genres g on f.genre_id = g.id " +
                "where film_id = ?";
        SqlRowSet genreRows =
                jdbcTemplate.queryForRowSet(sql, filmId);
        Set<Genre> genres = new TreeSet<>();
        while (genreRows.next()) {
            genres.add(new Genre(genreRows.getLong("genre_id"), genreRows.getString("genre")));
        }
        return genres;
    }

    @Override
    public void updateGenreForFilm(Film film) {
        jdbcTemplate.update(
                "DELETE FROM filmGenres where film_id = ?",
                film.getId()
        );
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(
                    "INSERT INTO filmGenres (film_id, genre_id) VALUES (?, ?)",
                    film.getId(),
                    genre.getId()
            );
        }
    }


}
