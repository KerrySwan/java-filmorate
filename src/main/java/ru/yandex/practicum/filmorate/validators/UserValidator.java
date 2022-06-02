package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NameIsInvalidException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
public class UserValidator {

    public static boolean isUserNameValid(User user) throws NameIsInvalidException {
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
            NameIsInvalidException e = new NameIsInvalidException();
            log.error(e.getMessage(), e);
            throw new NameIsInvalidException();
        }
    }

}
