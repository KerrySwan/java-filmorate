package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
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
    public User addUser(@RequestBody User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User putUser(@RequestBody User user){
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            user.setId(getNextId());
            users.put(user.getId(), user);
        }

        return user;
    }

}
