package me.dakbutfly.spark_api;


import me.dakbutfly.spark_api.repository.UserRepoImplByList;
import me.dakbutfly.spark_api.repository.UserRepository;

import java.util.ArrayList;

import static me.dakbutfly.jmockit_example.common.ConvertJsonToInstance.*;
import static spark.Spark.*;

public class Application {
    private static UserRepository userRepository = new UserRepoImplByList();

    public static void repositorysClear() {
        userRepository.clear();
    }

    public static void main(String[] args) {
        get("/users", (req, res) -> "{\"data\":{\"userList\":"+ toJson(userRepository.findAll()) +"}}");

        post("/users", (req, res) -> {
            User user = convertJsonStringTo(req.body(), User.class);

            userRepository.save(user);

            return "{\"data\":{\"user\":"+ toJson(user) +"}}";
        });

        delete("/users/:id", (req, res) -> {
            long userId = Long.parseLong(req.params("id"));
            if (!userRepository.existById(userId) )
                return toJson(ResponseBody.builder().codeno(5000L).code("ERROR").build());

            return toJson(ResponseBody.builder().codeno(2000L).code("SUCCESS").build());
        });
    }
}
