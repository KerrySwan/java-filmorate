package ru.yandex.practicum.filmorate.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {

    final private FilmStorage filmStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(long id) {
        return filmStorage.getFilm(id);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film putFilm(Film film) {
        return filmStorage.putFilm(film);
    }

    public void addLike(long filmId, long userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film.getLikes().contains(userId)) {
            log.warn("User with id " + userId + " has already liked this film");
            return;
        }
        film.getLikes().add(userId);
        log.info("User with id " + userId + " liked film by id " + filmId);

    }

    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.getFilm(filmId);
        film.getLikes().remove(userId);
        log.info("User with id " + userId + " removed liked for film by id " + filmId);
    }

    public Set<Film> getOrderedSet(int range) {
        return new TreeSet<>(filmStorage.getFilms())
                .stream()
                .limit(range)
                .collect(Collectors.toSet());
    }

}
