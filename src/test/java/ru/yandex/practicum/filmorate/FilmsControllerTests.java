package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.client.HttpFlimorateClient;

import java.net.http.HttpResponse;

@SpringBootTest
class FilmsControllerTests {

    private final static int port = 8080;

    private final static String url = "http://localhost:" + port;

    @Test
    void shouldProcessUsualRequest() {
        String body = "{\"name\":\"film\",\"description\":\"film\",\"releaseDate\":\"1999-11-11\",\"duration\":100}";
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/films", body);
        Assertions.assertEquals(200, res.statusCode());
    }

    @Test
    void shouldNotProcessIfInvalidRelease() {
        String body = "{\"name\":\"film\",\"description\":\"film\",\"releaseDate\":\"1600-11-11\",\"duration\":100}";;
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/films", body);
        Assertions.assertEquals(500, res.statusCode());
    }

    @Test
    void shouldNotProcessIfInvalidDuration() {
        String body = "{\"name\":\"film\",\"description\":\"film\",\"releaseDate\":\"1999-11-11\",\"duration\":-100}";;
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/films", body);
        Assertions.assertEquals(400, res.statusCode());
    }

    @Test
    void shouldNotProcessIfEmptyName() {
        String body = "{\"name\":\"\",\"description\":\"film\",\"releaseDate\":\"1999-11-11\",\"duration\":100}";;
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/films", body);
        Assertions.assertEquals(400, res.statusCode());
    }

    @Test
    void shouldNotProcessIfEmptyDescription() {
        String body = "{\"name\":\"film\",\"releaseDate\":\"1999-11-11\",\"duration\":100}";;
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/films", body);
        Assertions.assertEquals(400, res.statusCode());
    }

    @Test
    void shouldNotProcessIfEmptyBody() {
        String body = "";
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/films", body);
        Assertions.assertEquals(400, res.statusCode());
    }

}
