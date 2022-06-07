package ru.yandex.practicum.filmorate.exceptions;

public class EntityIsNotValidException extends RuntimeException{
    public EntityIsNotValidException(String message) {
        super(message);
    }

}
