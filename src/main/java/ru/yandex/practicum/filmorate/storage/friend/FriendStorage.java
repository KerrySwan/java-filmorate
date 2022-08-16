package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface FriendStorage {
    void getFriends(Map<Long, User> users);

    void getFriends(User user);

    void addFriends(User user);

    void removeFriends(User user);
}
