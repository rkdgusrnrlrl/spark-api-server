package me.dakbutfly.spark_api;


import java.util.ArrayList;

import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.post;
import static me.dakbutfly.jmockit_example.common.ConvertJsonToInstance.*;

public class Application {

    private static ArrayList<User> userList = new ArrayList<>();
    private static long userId = 0;


    public static void main(String[] args) {

        get("/users", (req, res) -> "{\"data\":{\"userList\":"+ toJson(userList) +"}}");

        post("/users", (req, res) -> {
            User user = convertJsonStringTo(req.body(), User.class);

            user.setId(++userId);
            userList.add(user);

            return "{\"data\":{\"user\":"+ toJson(user) +"}}";
        });
    }
}
