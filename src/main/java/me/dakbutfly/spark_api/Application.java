package me.dakbutfly.spark_api;


import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.get;
import static spark.Spark.post;
import static me.dakbutfly.jmockit_example.common.ConvertJsonToInstance.*;

public class Application {
    public static void main(String[] args) {

        get("/users", (req, res) -> {
            return "{\"data\":{\"users\":[]}}";
        });

        post("/users", (req, res) -> {
            String body = req.body();
            User user = convertJsonStringTo(body, User.class);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(user);
            return "{\"data\":{\"user\":"+json+"}}";
        });
    }
}
