package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(final MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getMpa() {
        return mpaStorage.getMpas();
    }

    public Mpa getMpa(long id) {
        return mpaStorage.getMpa(id);
    }

}
