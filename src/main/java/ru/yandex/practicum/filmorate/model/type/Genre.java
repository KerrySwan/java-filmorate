package ru.yandex.practicum.filmorate.model.type;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Genre {

    @Positive
    int[] id;

    private static Map<Integer, String> genreCodes = new HashMap<>(){{
        put(1, "COMEDY");
        put(2, "DRAMA");
        put(3, "CARTOON");
        put(4, "THRILLER");
        put(5, "DOCUMENTARY");
        put(6, "ACTION");
    }};

    public static String getGenre(int id){
        return genreCodes.getOrDefault(id, null);
    }

    public static int getCodeFromGenre(String genreName){
        for (Map.Entry<Integer, String> entry : genreCodes.entrySet()) {
            if (entry.getValue().equals(genreName)) return entry.getKey();
        }
        return -1;
    }

}
