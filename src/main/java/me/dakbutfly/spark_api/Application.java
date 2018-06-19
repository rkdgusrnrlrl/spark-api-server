package me.dakbutfly.spark_api;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import static spark.Spark.get;
import static spark.Spark.post;
import static me.dakbutfly.jmockit_example.common.ConvertJsonToInstance.*;

public class Application {

    private static ArrayList<User> userList = new ArrayList<>();

    public static void main(String[] args) {

        get("/users", (req, res) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(userList);

            return "{\"data\":{\"userList\":"+json+"}}";
        });

        post("/users", (req, res) -> {
            String body = req.body();
            User user = convertJsonStringTo(body, User.class);

            userList.add(user);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(user);
            return "{\"data\":{\"user\":"+json+"}}";
        });
    }
}
