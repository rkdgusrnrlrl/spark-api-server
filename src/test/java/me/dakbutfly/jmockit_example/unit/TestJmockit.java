package me.dakbutfly.jmockit_example.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import me.dakbutfly.jmockit_example.Some;
import me.dakbutfly.jmockit_example.Some2;
import me.dakbutfly.jmockit_example.SomeList;
import me.dakbutfly.jmockit_example.common.ConvertJsonToInstance;
import me.dakbutfly.jmockit_example.exception.NotJsonFormatException;
import me.dakbutfly.jmockit_example.exception.NotMatchJsonToSomeException;
import me.dakbutfly.spark_api.User;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spark.Request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("각 라이브러리(jmockit, jackson, ConvertJsonToInstance) 테스트")
class TestJmockit {

    @Mocked
    private Request request;

    private final String NAME = "강현구";
    private final int AGE = 32;
    private final String JSON_SOME_STRING = "{\"name\":\""+NAME+"\"}";
    private final String JSON_NOT_SOME_STRING = "{\"age\": 32}";


    @Test
    void request_body는_JSON_STRING_값을_리턴() {
        new Expectations() {{
           request.body(); result = JSON_SOME_STRING;
        }};

        String body = request.body();
        assertEquals(body, JSON_SOME_STRING);
    }

    @Test
    void request_body가_비어있으면__NotJsonFormatException_발생() throws Exception {
        assertThrows(NotJsonFormatException.class, () -> {
            new Expectations() {{
                request.body(); result = "";
            }};

            String body = request.body();
            Some some = ConvertJsonToInstance.convertJsonStringTo(body, Some.class);
        });
    }

    @Test
    void request_body가_json형태가_아니면_NotJsonFormatException_발생() throws Exception {
        assertThrows(NotJsonFormatException.class, () -> {
            new Expectations() {{
                request.body(); result = "";
            }};

            String body = request.body();
            ConvertJsonToInstance.convertJsonStringTo(body, Some.class);
        });
    }

    @Test
    void request_body가_json이_Some과_맞지않으면_NotMatchJsonToSomeException() throws Exception {
        assertThrows(NotMatchJsonToSomeException.class, () -> {
            new Expectations() {{
                request.body(); result = JSON_NOT_SOME_STRING;
            }};

            String body = request.body();
            ConvertJsonToInstance.convertJsonStringTo(body, Some.class);
        });
    }

    @Test
    void request_body가_JSON_STRING_이면_Some_인스턴스를_리턴() throws Exception {
        new Expectations() {{
            request.body(); result = JSON_SOME_STRING;
        }};

        String body = request.body();
        Some some = ConvertJsonToInstance.convertJsonStringTo(body, Some.class);

        assertEquals(some.name, NAME);
    }

    @Test
    void request_body가_JSON_STRING_이면_Some2_인스턴스를_리턴() throws Exception {
        new Expectations() {{
            request.body(); result = JSON_NOT_SOME_STRING;
        }};

        String body = request.body();
        Some2 some2 = ConvertJsonToInstance.convertJsonStringTo(body, Some2.class);

        assertEquals(some2.age, AGE);
    }

    @Test
    void request_body가_내부_변수에_리스트_없는경우_이면_해당_맴버는_빈_리스트임() throws Exception {
        new Expectations() {{
            request.body(); result = "{ \"list\": [] }";
        }};

        String body = request.body();
        SomeList some = ConvertJsonToInstance.convertJsonStringTo(body, SomeList.class);

        assertEquals(some.list.size(), 0);
    }

    @Test
    void request_body가_Class_맴버가_없는경우_이면_NotMatchJsonToSomeException() throws Exception {
        assertThrows(NotMatchJsonToSomeException.class, () -> {
            new Expectations() {{
                request.body(); result = "{ \"list\": [], \"age\" : 32 }";
            }};

            String body = request.body();
            SomeList some = ConvertJsonToInstance.convertJsonStringTo(body, SomeList.class);

            assertEquals(some.list.size(), 0);
        });
    }

    @Test
    void jackson_databind가_Builder_Class_여도_잘맵핑됨() throws Exception {
        new Expectations() {{
            request.body(); result = "{\"name\":\"강현구\",\"age\":32}";
        }};

        String body = request.body();
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(body, User.class);

        assertEquals(user.getName(), "강현구");
    }

    @Test
    void json_필드가_부족해도_인스턴스_생성되나_int는_0으로_초기화됨() throws Exception {
        String userString1 = "{\"name\":\"강현구\"}";
        String userString2 = "{\"age\":25}";
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        User user = objectMapper.readValue(userString1, User.class);
        User user2 = objectMapper.readValue(userString2, User.class);

        // then
        assertEquals(user.getAge(), 0);
        assertNull(user2.getName());
    }

    @Test
    void json_필드가_더_있다면_UnrecognizedPropertyException_발생() throws Exception {
        assertThrows(UnrecognizedPropertyException.class, () -> {
            String userString2 = "{\"name\":\"강현구\",\"age\":25}";
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readValue(userString2, Some.class);
        });
    }
}
