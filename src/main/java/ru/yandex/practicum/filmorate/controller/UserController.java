package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.sevice.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@RestController
@Component
@RequiredArgsConstructor
@Validated
public class UserController {

    final private UserService service;

    @GetMapping("/users")
    public Collection<User> getUsers() {
        return service.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable @Positive long id) {
        return service.getUser(id);
    }


    @PostMapping("/users")
    public User addUser(@RequestBody @Valid User user) {
        return service.addUser(user);
    }

    @PutMapping("/users")
    public User putUser(@RequestBody @Valid User user) {
        return service.putUser(user);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable @Positive long id, @PathVariable @Positive long friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable @Positive long id, @PathVariable @Positive long friendId) {
        service.removeFriend(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getFriends(@PathVariable @Positive long id) {
        return service.getFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getMutuals(@PathVariable @Positive long id, @PathVariable @Positive int otherId) {
        return service.getMutualFriends(id, otherId);
    }


}
