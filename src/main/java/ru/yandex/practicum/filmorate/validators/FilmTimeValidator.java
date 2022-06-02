package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.DateTimeException;
import java.time.LocalDate;

@Slf4j
public class FilmTimeValidator {

    public static void isDateValid(Film film) throws DateTimeException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 1, 28))){
            DateTimeException e = new DateTimeException("Date is invalid. Film's release must be after 28.01.1895");
            log.error("Release date is invalid. Film's id:" + film.getId() + " release must be after 28.01.1895\n" +
                      "current date is:" + film.getReleaseDate(), e);
            throw e;
        } else log.info("Release date is valid");
    }

}
