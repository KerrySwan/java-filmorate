package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Entity {

    protected long id;

    public static long findMissingId(List<Long> ids, long maxId) {
        List<Long> seq = Stream.iterate(1L, n -> n + 1)
                .limit(maxId)
                .collect(Collectors.toList());
        for (int i = 0; i < maxId - 1; i++) {
            long xor = seq.get(i)^ids.get(i);
            if (xor != 0) return seq.get(i);
        }
        return -1;
    }

}
