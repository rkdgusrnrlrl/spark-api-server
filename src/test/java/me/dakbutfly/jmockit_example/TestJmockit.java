package me.dakbutfly.jmockit_example;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dakbutfly.jmockit_example.common.ConvertJsonToInstance;
import me.dakbutfly.jmockit_example.exception.NotJsonFormatException;
import me.dakbutfly.jmockit_example.exception.NotMatchJsonToSomeException;
import me.dakbutfly.spark_api.User;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import spark.Request;

import static org.junit.Assert.*;

public class TestJmockit {

    @Mocked
    private Request request;

    private final String NAME = "강현구";
    private final int AGE = 32;
    private final String JSON_SOME_STRING = "{\"name\":\""+NAME+"\"}";
    private final String JSON_NOT_SOME_STRING = "{\"age\": 32}";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void request_body는_JSON_STRING_값을_리턴() {
        new Expectations() {{
           request.body(); result = JSON_SOME_STRING;
        }};

        String body = request.body();
        assertEquals(body, JSON_SOME_STRING);
    }

    @Test
    public void request_body가_비어있으면__NotJsonFormatException_발생() throws Exception {
        expectedException.expect(NotJsonFormatException.class);
        new Expectations() {{
            request.body(); result = "";
        }};

        String body = request.body();
        Some some = ConvertJsonToInstance.convertJsonStringTo(body, Some.class);

        assertNull(some);
    }

    @Test
    public void request_body가_json형태가_아니면_NotJsonFormatException_발생() throws Exception {
        expectedException.expect(NotJsonFormatException.class);
        new Expectations() {{
            request.body(); result = "";
        }};

        String body = request.body();
        ConvertJsonToInstance.convertJsonStringTo(body, Some.class);
    }

    @Test
    public void request_body가_json이_Some과_맞지않으면_NotMatchJsonToSomeException() throws Exception {
        expectedException.expect(NotMatchJsonToSomeException.class);
        new Expectations() {{
            request.body(); result = JSON_NOT_SOME_STRING;
        }};

        String body = request.body();
        ConvertJsonToInstance.convertJsonStringTo(body, Some.class);
    }

    @Test
    public void request_body가_JSON_STRING_이면_Some_인스턴스를_리턴() throws Exception {
        new Expectations() {{
            request.body(); result = JSON_SOME_STRING;
        }};

        String body = request.body();
        Some some = ConvertJsonToInstance.convertJsonStringTo(body, Some.class);

        assertEquals(some.name, NAME);
    }

    @Test
    public void request_body가_JSON_STRING_이면_Some2_인스턴스를_리턴() throws Exception {
        new Expectations() {{
            request.body(); result = JSON_NOT_SOME_STRING;
        }};

        String body = request.body();
        Some2 some2 = ConvertJsonToInstance.convertJsonStringTo(body, Some2.class);

        assertEquals(some2.age, AGE);
    }

    @Test
    public void request_body가_내부_변수에_리스트_없는경우_이면_해당_맴버는_빈_리스트임() throws Exception {
        new Expectations() {{
            request.body(); result = "{ \"list\": [] }";
        }};

        String body = request.body();
        SomeList some = ConvertJsonToInstance.convertJsonStringTo(body, SomeList.class);

        assertEquals(some.list.size(), 0);
    }

    @Test
    public void request_body가_Class_맴버가_없는경우_이면_NotMatchJsonToSomeException() throws Exception {
        expectedException.expect(NotMatchJsonToSomeException.class);
        new Expectations() {{
            request.body(); result = "{ \"list\": [], \"age\" : 32 }";
        }};

        String body = request.body();
        SomeList some = ConvertJsonToInstance.convertJsonStringTo(body, SomeList.class);

        assertEquals(some.list.size(), 0);
    }

    @Test
    public void jackson_databind가_Builder_Class_여도_잘맵핑됨() throws Exception {
        new Expectations() {{
            request.body(); result = "{\"name\":\"강현구\",\"age\":32}";
        }};

        String body = request.body();
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(body, User.class);

        assertEquals(user.getName(), "강현구");
    }

}
