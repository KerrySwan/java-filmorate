package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private User firstUser;
    private User secondUser;
    private Film firstFilm;
    private Film secondFilm;

    private Set<Genre> genres;

    {
        genres = new HashSet<>(){{
            add(new Genre(1, "G"));
        }};
        firstUser = User.builder()
                .name("john")
                .login("john_1337")
                .email("john@email.com")
                .birthday(LocalDate.of(1999, 12, 12))
                .build();
        secondUser = User.builder()
                .name("felix")
                .login(">felix<")
                .email("felix@email.com")
                .birthday(LocalDate.of(1998, 12, 12))
                .build();
        firstFilm = Film.builder()
                .name("Simple Film")
                .description("Simple film desc")
                .releaseDate(LocalDate.of(2019, 11, 11))
                .duration(256)
                .mpa(new Mpa(1, "Комедия"))
                .genres(genres)
                .build();
        secondFilm = Film.builder()
                .name("Genuine Film")
                .description("Genuine film desc")
                .releaseDate(LocalDate.of(1950, 11, 11))
                .duration(256)
                .mpa(new Mpa(1, "Комедия"))
                .genres(genres)
                .build();
    }

    @BeforeAll
    public void setUp(){
        userStorage.addUser(firstUser);
        userStorage.addUser(secondUser);
        filmStorage.addFilm(firstFilm);
        filmStorage.addFilm(secondFilm);
    }


    @Test
    public void testFindUserById() {
        User user = userStorage.getUser(1);
        Assertions.assertEquals(1, user.getId());

    }

    @Test
    public void testGetAllUsers() {
        Collection<User> users = userStorage.getUsers();
        System.out.println(users);
        Assertions.assertEquals(2, users.size());
    }

    @Test
    public void testPutUser() {
        User user = User.builder()
                .id(1)
                .name("john_updated")
                .login("john_1337")
                .email("john@email.com")
                .birthday(LocalDate.of(1999, 12, 12))
                .build();
        userStorage.putUser(user);
        User userUpdated = userStorage.getUser(1);

        Assertions.assertEquals(1, userUpdated.getId());
        Assertions.assertEquals("john_updated", userUpdated.getName());
    }

    @Test
    public void testFindFilmById() {
        Film film = filmStorage.getFilm(1);
        Assertions.assertEquals(1, film.getId());

    }

    @Test
    public void testGetAllFilms() {
        Collection<Film> films = filmStorage.getFilms();
        Assertions.assertEquals(2, films.size());
    }

    @Test
    public void testPutFilm() {
        Film film = Film.builder()
                .id(1)
                .name("Neon Genesis Simple Film")
                .description("Simple film desc")
                .releaseDate(LocalDate.of(2019, 11, 11))
                .duration(256)
                .mpa(new Mpa(1, "Комедия"))
                .genres(genres)
                .build();
        filmStorage.putFilm(film);
        Film filmUpdated = filmStorage.getFilm(1);

        Assertions.assertEquals(1, filmUpdated.getId());
        Assertions.assertEquals("Neon Genesis Simple Film", filmUpdated.getName());
    }

}