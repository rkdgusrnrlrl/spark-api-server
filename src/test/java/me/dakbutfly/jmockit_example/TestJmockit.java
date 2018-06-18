package me.dakbutfly.jmockit_example;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import spark.Request;

import java.io.IOException;

import static org.junit.Assert.*;

public class TestJmockit {

    @Mocked
    private Request request;

    private final String NAME = "강현구";
    private final String JSON_SOME_STRING = "{\"name\":\""+NAME+"\"}";
    private final String JSON_NOT_SOME_STRING = "{\"age\": 32}";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void 리퀘스트_바디는_JSON_STRING_값을_리턴() {
        new Expectations() {{
           request.body(); result = JSON_SOME_STRING;
        }};

        String body = request.body();
        assertEquals(body, JSON_SOME_STRING);
    }

    @Test
    public void 리퀘스트_바디가_비어있으면__NotJsonFormatException_발생() throws Exception {
        expectedException.expect(NotJsonFormatException.class);
        new Expectations() {{
            request.body(); result = "";
        }};

        String body = request.body();
        Some some = covertJsonStringTo(body, Some.class);

        assertNull(some);
    }

    @Test
    public void 리퀘스트_바디가_json형태가_아니면_NotJsonFormatException_발생() throws Exception {
        expectedException.expect(NotJsonFormatException.class);
        new Expectations() {{
            request.body(); result = "";
        }};

        String body = request.body();
         covertJsonStringTo(body, Some.class);
    }

    @Test
    public void 리퀘스트_바디가_json이_Some과_맞지않으면_NotMatchJsonToSomeException() throws Exception {
        expectedException.expect(NotMatchJsonToSomeException.class);
        new Expectations() {{
            request.body(); result = JSON_NOT_SOME_STRING;
        }};

        String body = request.body();
        covertJsonStringTo(body, Some.class);
    }

    @Test
    public void 리퀘스트_바디가_JSON_STRING_이면_Some_인스턴스를_리턴() throws Exception {
        new Expectations() {{
            request.body(); result = JSON_SOME_STRING;
        }};

        String body = request.body();
        Some some = covertJsonStringTo(body, Some.class);

        assertEquals(some.name, NAME);
    }

    private Some covertJsonStringTo(String body, Class<Some> someClass) throws NotJsonFormatException, NotMatchJsonToSomeException {
        ObjectMapper objectMapper = new ObjectMapper();
        validJsonFormat(body, objectMapper);
        return jsonSringTo(body, someClass, objectMapper);
    }

    private Some jsonSringTo(String body, Class<Some> someClass, ObjectMapper objectMapper) throws NotMatchJsonToSomeException {
        try {
            return objectMapper.readValue(body, someClass);
        } catch (IOException e) {
            throw new NotMatchJsonToSomeException();
        }
    }

    private void validJsonFormat(String body, ObjectMapper objectMapper) throws NotJsonFormatException {
        try {
            objectMapper.readTree(body);
        } catch (IOException e) {
            throw new NotJsonFormatException(body);
        }
    }

    private class NotJsonFormatException extends Exception {
        public NotJsonFormatException(String body) {
            super(body);
        }
    }

    private class NotMatchJsonToSomeException extends Exception {
    }
}
