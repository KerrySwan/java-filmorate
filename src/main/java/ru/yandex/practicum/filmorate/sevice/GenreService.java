package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(final GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }


    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Genre getGenre(long id) {
        return genreStorage.getGenre(id);
    }

}
