package me.dakbutfly.jmockit_example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dakbutfly.spark_api.User;

import me.dakbutfly.spark_api.Application;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.*;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static spark.Spark.stop;

public class TestUserApi {

    @BeforeClass
    public static void 서버_실행() {
        Application.main(null);
    }

    @Test
    public void 성공시_사용자정보를_출력() throws IOException {

        Content content = Request.Get("http://localhost:4567/users")
                                .execute()
                                .returnContent();
        String body = content.asString();
        assertEquals(body, "{\"data\":{\"userList\":[]}}");

    }

    @Test
    public void 사용자정보를_등록_출력() throws IOException {
        User user = User.builder()
                .name("강현구")
                .age(32)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        Content content = Request.Post("http://localhost:4567/users")
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent();
        String body = content.asString();
        assertEquals(body, "{\"data\":{\"user\":"+json+"}}");
    }

    @AfterClass
    public static void 서버_종료() {
        stop();
    }
}