package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<Film> getFilms();

    Film getFilm(long id);

    Film addFilm(Film film);

    Film putFilm(Film film);

    Film.Rating getMpa(long id);

    List<Film.Rating> getMpas();

    List<Film.Genre> getGenres();

    Film.Genre getGenre(long id);
}
