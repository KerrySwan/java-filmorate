package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MpaDbStorage(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpas() {
        String sql = "select id, rating from ratings";
        SqlRowSet ratingRows =
                jdbcTemplate.queryForRowSet(sql);
        List<Mpa> mpas = new ArrayList<>();
        while (ratingRows.next()) {
            mpas.add(new Mpa(ratingRows.getLong("id"), ratingRows.getString("rating")));
        }
        return mpas;
    }


    @Override
    public Mpa getMpa(long ratingId) {
        SqlRowSet ratingRows =
                jdbcTemplate.queryForRowSet("select id, rating from ratings where id = ?", ratingId);
        if (ratingRows.next()) {
            return new Mpa(ratingRows.getLong("id"), ratingRows.getString("rating"));
        }
        return null;
    }

}
