package ru.yandex.practicum.filmorate.exception;

public class EntityIsNotFoundException extends RuntimeException {
    public EntityIsNotFoundException(String message) {
        super(message);
    }

}
