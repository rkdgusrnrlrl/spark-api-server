package me.dakbutfly.jmockit_example;


import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Test;
import spark.Request;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestJmockit {

    @Mocked
    private Request request;

    private final String JSON_STRING = "{\"name\":\"강현구\"}";

    @Test
    public void testMockTest() {
        new Expectations() {{
           request.body(); result = JSON_STRING;
        }};

        String body = request.body();
        assertEquals(body, JSON_STRING);
    }
}
