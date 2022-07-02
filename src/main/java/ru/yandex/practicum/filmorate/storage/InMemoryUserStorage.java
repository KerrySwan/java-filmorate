package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityIsNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.EntityValidator;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private HashMap<Long, User> users = new HashMap<>();

    private long userId;

    public long getNextId() {
        return ++userId;
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUser(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            log.error("User with id: " + id + " does not exist.");
            throw new EntityIsNotFoundException("User with id: " + id + " does not exist.");
        }
    }

    @Override
    public User addUser(User user) {
        if (!EntityValidator.isUserNameValid(user)) user.setName(user.getLogin());
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info(user + " added to memory");
        return user;
    }

    @Override
    public User putUser(User user) {
        if (!EntityValidator.isUserNameValid(user)) user.setName(user.getLogin());
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info(user + " updated");
        } else {
            log.error("User with id: " + user.getId() + " does not exist.");
            throw new EntityIsNotFoundException("User with id: " + user.getId() + " does not exist.");
        }
        return user;
    }

}
