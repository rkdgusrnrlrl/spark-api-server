package me.dakbutfly.jmockit_example.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.dakbutfly.spark_api.Application;
import me.dakbutfly.spark_api.User;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static spark.Spark.stop;

public class TestUserApiBeforeSetUser {

    @BeforeClass
    public static void 서버_실행() {
        Application.main(null);
    }

    @Before
    public void 사용자등록() throws IOException {
        String json = getUserJsonString();

        Content content = Request.Post("http://localhost:4567/users")
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent();
        String body = content.asString();
        assertEquals(body, "{\"data\":{\"user\":"+json+"}}");
    }

    private static String getUserJsonString() throws JsonProcessingException {
        User user = User.builder()
                .name("강현구")
                .age(32)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }

    @Test
    public void 사용자_한명이_등록되_있어야함() throws IOException {

        Content content = Request.Get("http://localhost:4567/users")
                                .execute()
                                .returnContent();
        String body = content.asString();
        assertEquals(body, "{\"data\":{\"userList\":["+getUserJsonString()+"]}}");

    }

    @AfterClass
    public static void 서버_종료() {
        stop();
    }
}