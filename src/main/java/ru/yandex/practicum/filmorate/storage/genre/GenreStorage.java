package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    List<Genre> getGenres();

    Genre getGenre(long id);

    Set<Genre> getGenresForFilm(long filmId);

    void updateGenreForFilm(Film film);

}
