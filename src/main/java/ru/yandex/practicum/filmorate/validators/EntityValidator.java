package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.IdIsNotValidException;
import ru.yandex.practicum.filmorate.model.Entity;

@Slf4j
public class EntityValidator {

    public static void throwIfIdIsNotPositive(Entity entity) throws IdIsNotValidException {
        if (entity.getId() <= 0) {
            IdIsNotValidException e =  new IdIsNotValidException("Entities id is zero or below, current id:" + entity.getId());
            log.error(e.getMessage() ,e);
            throw e;
        }
    }

}
