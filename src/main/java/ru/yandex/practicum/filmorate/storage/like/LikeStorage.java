package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface LikeStorage {
    void getLikes(Map<Long, Film> films);

    void getLikes(Film film);

    void addLikes(Film film);

    void removeLikes(Film film);
}
