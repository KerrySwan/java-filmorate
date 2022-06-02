package ru.yandex.practicum.filmorate.exceptions;

public class NameIsInvalidException extends Exception{
    public NameIsInvalidException() {
        super("Name is not valid");
    }
}
