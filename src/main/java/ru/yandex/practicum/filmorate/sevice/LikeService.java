package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

@Slf4j
@Service
public class LikeService {

    @Autowired
    @Qualifier("filmDbStorage")
    final private FilmStorage filmStorage;

    public LikeService(@Autowired @Qualifier("filmDbStorage") final FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


    public void addLike(long filmId, long userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film.getLikes().contains(userId)) {
            log.warn("User with id " + userId + " has already liked this film");
            return;
        }
        film.getLikes().add(userId);
        filmStorage.putFilm(film);
        log.info("User with id " + userId + " liked film by id " + filmId);

    }

    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.getFilm(filmId);
        film.getLikes().remove(userId);
        log.info("User with id " + userId + " removed liked for film by id " + filmId);
    }


}
