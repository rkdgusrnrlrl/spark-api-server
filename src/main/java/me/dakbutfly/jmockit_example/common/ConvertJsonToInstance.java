package me.dakbutfly.jmockit_example.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dakbutfly.jmockit_example.exception.NotJsonFormatException;
import me.dakbutfly.jmockit_example.exception.NotMatchJsonToSomeException;

import java.io.IOException;

public class ConvertJsonToInstance {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertJsonStringTo(String body, Class<T> someClass) throws NotJsonFormatException, NotMatchJsonToSomeException {
        validJsonFormat(body);
        return jsonSringTo(body, someClass);
    }

    private static <T> T jsonSringTo(String body, Class<T> someClass) throws NotMatchJsonToSomeException {
        try {
            return objectMapper.readValue(body, someClass);
        } catch (IOException e) {
            throw new NotMatchJsonToSomeException();
        }
    }

    private static void validJsonFormat(String body) throws NotJsonFormatException {
        try {
            objectMapper.readTree(body);
        } catch (IOException e) {
            throw new NotJsonFormatException();
        }
    }
}