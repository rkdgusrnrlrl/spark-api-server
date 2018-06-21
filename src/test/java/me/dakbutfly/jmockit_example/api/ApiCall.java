package me.dakbutfly.jmockit_example.api;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;

public class ApiCall {
    public static String registerUser(String json) throws IOException {
        return Request.Post("http://localhost:4567/users")
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
    }

    public static String findUsers() throws IOException {
        return Request.Get("http://localhost:4567/users")
                .execute()
                .returnContent()
                .asString();
    }
}