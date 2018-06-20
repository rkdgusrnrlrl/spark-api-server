package me.dakbutfly.jmockit_example.api;

import me.dakbutfly.spark_api.User;

import me.dakbutfly.spark_api.Application;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static me.dakbutfly.jmockit_example.common.ConvertJsonToInstance.toJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static spark.Spark.stop;

public class TestUserApi {

    @BeforeAll
    public static void 서버_실행() {
        Application.main(null);
    }

    @Test
    public void 성공시_사용자정보를_출력() throws IOException {
        String body = ApiCall.findUsersApiCall();

        assertEquals(body, "{\"data\":{\"userList\":[]}}");
    }

    @Test
    public void 사용자정보를_등록_출력() throws IOException {
        User user = User.builder()
                .name("강현구")
                .age(32)
                .build();
        String json = toJson(user);

        String body = ApiCall.registerUserApiCall(json);

        user.setId(1);
        String userStirng = toJson(user);
        assertEquals(body, "{\"data\":{\"user\":"+userStirng+"}}");
    }

    @AfterAll
    public static void 서버_종료() {
        stop();
    }
}