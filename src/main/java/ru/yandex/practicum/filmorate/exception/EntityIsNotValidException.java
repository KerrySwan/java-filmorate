package ru.yandex.practicum.filmorate.exception;

public class EntityIsNotValidException extends RuntimeException {
    public EntityIsNotValidException(String message) {
        super(message);
    }

}
