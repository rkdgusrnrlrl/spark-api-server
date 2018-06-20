package me.dakbutfly.jmockit_example.api;

import me.dakbutfly.spark_api.Application;
import me.dakbutfly.spark_api.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static me.dakbutfly.jmockit_example.common.ConvertJsonToInstance.toJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static spark.Spark.stop;

public class TestUserApiBeforeSetUser {

    @BeforeAll
    public static void 서버_실행() {
        Application.main(null);
    }

    @BeforeEach
    public void 사용자등록() throws IOException {
        User user = getUserInstance();
        String json = toJson(user);

        String body = ApiCall.registerUserApiCall(json);

        user.setId(1L);
        String userJson = toJson(user);
        assertEquals(body, "{\"data\":{\"user\":"+userJson+"}}");
    }

    @Test
    public void 사용자_한명이_등록되_있어야함() throws IOException {
        String body = ApiCall.findUsersApiCall();

        User user = getUserInstance();
        assertEquals(body, "{\"data\":{\"userList\":["+ toJson(user) +"]}}");

    }

    @AfterAll
    public static void 서버_종료() {
        stop();
    }


    private User getUserInstance() {
        return User.builder()
                .name("강현구")
                .age(32)
                .build();
    }
}