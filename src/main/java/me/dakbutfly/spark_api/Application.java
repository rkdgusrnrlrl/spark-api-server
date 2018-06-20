package me.dakbutfly.spark_api;


import me.dakbutfly.spark_api.repository.UserRepoImplByList;
import me.dakbutfly.spark_api.repository.UserRepository;

import java.util.ArrayList;

import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.post;
import static me.dakbutfly.jmockit_example.common.ConvertJsonToInstance.*;

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
    }
}
