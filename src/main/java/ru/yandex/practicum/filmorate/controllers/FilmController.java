package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {

    HashMap<Integer, Film> films = new HashMap<>();

    private static int filmId;

    public int getNextId() {
        return ++filmId;
    }


    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            film.setId(getNextId());
            films.put(film.getId(), film);
        }

        return film;
    }

}
