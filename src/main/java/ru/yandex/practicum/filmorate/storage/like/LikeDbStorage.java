package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;
import java.util.Set;

@Component
public class LikeDbStorage implements LikeStorage {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public LikeDbStorage(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void getLikes(Map<Long, Film> films) {
        SqlRowSet likes = jdbcTemplate.queryForRowSet("select film_id, user_id from likes");
        while (likes.next()) {
            long film_id = likes.getLong("film_id");
            if (films.containsKey(film_id)) {
                films.get(film_id)
                        .getLikes()
                        .add(likes.getLong("user_id"));
            }
        }
    }

    @Override
    public void getLikes(Film film) {
        SqlRowSet likes = jdbcTemplate.queryForRowSet("select film_id, user_id from likes where film_id = ?", film.getId());
        if (likes.next()) {
            film.getLikes().add(likes.getLong("user_id"));
        }
    }

    @Override
    public void addLikes(Film film) {
        Set<Long> usersId = film.getLikes();
        for (Long id : usersId) {
            jdbcTemplate.update(
                    "INSERT INTO likes (film_id, user_id) values (?, ?)",
                    film.getId(),
                    id
            );
        }
    }

    @Override
    public void removeLikes(Film film) {
        jdbcTemplate.update(
                "DELETE FROM likes WHERE film_id = ?",
                film.getId()
        );
    }

}
