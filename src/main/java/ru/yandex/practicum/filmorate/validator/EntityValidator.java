package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.EntityIsNotValidException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.DateTimeException;
import java.time.LocalDate;

@Slf4j
public class EntityValidator {

    private static final LocalDate minDate = LocalDate.of(1895,1,28);

    public static void throwIfIdIsNotPositive(int id) {
        if (id <= 0) {
            EntityIsNotValidException e =  new EntityIsNotValidException("Entities id is zero or below, current id: " + id);
            log.error(e.getMessage() ,e);
            throw e;
        }
    }

    public static void throwIfIdIsNotPositive(Entity entity) {
        throwIfIdIsNotPositive(entity.getId());
    }

    public static boolean isUserNameValid(User user) {
        String name = user.getName();
        if (name == null) {
            log.warn("passed name is null");
            return false;
        }
        if (name.isBlank()) {
            log.warn("passed name is blank");
            return false;
        }
        if (name.matches("[\\w\\s]+")) {
            log.info("name is accepted for user, current name" + user.getName());
            return true;
        } else {
            EntityIsNotValidException e = new EntityIsNotValidException("Username is invalid.");
            log.error("Username is invalid. User's id:" + user.getId() , e);
            throw e;
        }
    }

    public static void isDateValid(Film film) {
        if (film.getReleaseDate().isBefore(minDate)){
            DateTimeException e = new DateTimeException("Date is invalid. Film's release must be after 28.01.1895");
            log.error("Release date is invalid. Film's id:" + film.getId() + " release must be after 28.01.1895\n" +
                    "current date is:" + film.getReleaseDate(), e);
            throw e;
        } else log.info("Release date is valid");
    }

}
