package me.dakbutfly.jmockit_example.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

class JacksonTest {

    @Test
    void Enum_serializer_테스트() throws JsonProcessingException {
        TodoItem hello = new TodoItem(Status.TODO, "hello");
        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(hello);
        System.out.println(json);
    }

    @Test
    void Enum_deserializer_테스트() throws Exception {
        String json = "{\"status\":\"TODO\",\"title\":\"world\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TodoItem todoItem = objectMapper.readValue(json, TodoItem.class);
    }
}
