package ru.yandex.practicum.filmorate.model.type;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Rating {

    @Positive
    int id;

    private static Map<Integer, String> ratingCodes = new HashMap<>(){{
        put(1, "G");
        put(2, "PG");
        put(3, "PG_13");
        put(4, "R");
        put(5, "NC_17");
    }};

    public static String getRating(int id){
        return ratingCodes.getOrDefault(id, null);
    }

    public static int getCodeFromGenre(String ratingMark){
        for (Map.Entry<Integer, String> entry : ratingCodes.entrySet()) {
            if (entry.getValue().equals(ratingMark)) return entry.getKey();
        }
        return -1;
    }
}
