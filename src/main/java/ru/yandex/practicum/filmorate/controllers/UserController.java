package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IdIsNotValidException;
import ru.yandex.practicum.filmorate.exceptions.NameIsInvalidException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.EntityValidator;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    HashMap<Integer, User> users = new HashMap<>();

    private static int userId;

    public int getNextId(){
        return ++userId;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) throws NameIsInvalidException, IdIsNotValidException {
        if (!UserValidator.isUserNameValid(user)) user.setName(user.getLogin());
        user.setId(getNextId());
        EntityValidator.throwIfIdIsNotPositive(user);
        users.put(user.getId(), user);
        log.info(user + " added to memory");
        return user;
    }

    @PutMapping
    public User putUser(@RequestBody @Valid User user) throws NameIsInvalidException, IdIsNotValidException {
        EntityValidator.throwIfIdIsNotPositive(user);
        if (!UserValidator.isUserNameValid(user)) user.setName(user.getLogin());
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info(user + " updated");
        } else {
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info(user + " added to memory");
        }

        return user;
    }

}
