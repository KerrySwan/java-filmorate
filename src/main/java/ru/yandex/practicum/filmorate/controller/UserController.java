package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.sevice.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Component
@RequiredArgsConstructor
public class UserController {


    final private UserService service;

    @GetMapping("/users")
    public Collection<User> getUsers() {
        return service.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
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
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        service.removeFriend(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return service.getFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getMutuals(@PathVariable int id, @PathVariable int otherId) {
        return service.getMutualFriends(id, otherId);
    }


}
