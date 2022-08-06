package ru.yandex.practicum.filmorate.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    @Autowired
    @Qualifier("inMemoryUserStorage")
    final private UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(long id) {
        return userStorage.getUser(id);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User putUser(User user) {
        return userStorage.putUser(user);
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user != friend) {
            boolean isAccepted = friend.getFriends().containsKey(userId);
            user.getFriends().put(friendId, isAccepted);
            friend.getFriends().put(userId, isAccepted);
            log.info("Users with ids " + userId + " and " + friendId + " befriended.");
        } else {
            log.warn("Impossible to befriend yourself.");
        }
    }

    public void removeFriend(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Users with ids " + userId + " and " + friendId + " are not friends anymore.");
    }

    public List<User> getMutualFriends(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        Map<Long, Boolean> userFriends = user.getFriends();
        if (userFriends == null || userFriends.isEmpty()) return Collections.emptyList();
        Map<Long, Boolean> friendFriends = friend.getFriends();
        if (friendFriends == null || friendFriends.isEmpty()) return Collections.emptyList();
        List<User> mutuals = new ArrayList<>();
        for (Long id : userFriends.keySet()) {
            if (friendFriends.containsKey(id)) {
                mutuals.add(userStorage.getUser(id));
            }
        }
        return mutuals;

    }

    public List<User> getFriends(long userId) {
        List<User> friends = new ArrayList<>();
        for (Long id : userStorage.getUser(userId).getFriends().keySet()) {
            friends.add(userStorage.getUser(id));
        }
        return friends;
    }

}
