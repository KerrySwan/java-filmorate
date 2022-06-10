package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.http.HttpResponse;

@SpringBootTest
class UsersControllerTests {

    private final static int port = 8080;

    private final static String url = "http://localhost:" + port;

    @Test
    void shouldProcessUsualRequest() {
        String body = "{\"login\":\"user\",\"email\":\"user@mail.ru\",\"birthday\":\"1999-08-20\"}";
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/users", body);
        Assertions.assertEquals(200, res.statusCode());
    }

    @Test
    void shouldNotProcessIfInvalidEmail() {
        String body = "{\"login\":\"user user\",\"email\":\"mail.ru\",\"birthday\":\"2446-08-20\"}";
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/users", body);
        Assertions.assertEquals(400, res.statusCode());
    }

    @Test
    void shouldNotProcessIfInvalidBd() {
        String body = "{\"name\":\"user\",\"login\":\"user user\",\"user@email\":\"user@mail.ru\",\"birthday\":\"2919-11-11\"}";
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/users", body);
        Assertions.assertEquals(400, res.statusCode());
    }

    @Test
    void shouldNotProcessIfEmptyLogin() {
        String body = "{\"name\":\"user\",\"login\":\"\",\"email\":\"user@mail.ru\",\"birthday\":\"1999-08-20\"}";
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/users", body);
        Assertions.assertEquals(400, res.statusCode());
    }

    @Test
    void shouldNotProcessIfEmptyBody() {
        String body = "";
        HttpResponse<String> res = HttpFlimorateClient.sendPostRequest(url + "/users", body);
        Assertions.assertEquals(400, res.statusCode());
    }

}
