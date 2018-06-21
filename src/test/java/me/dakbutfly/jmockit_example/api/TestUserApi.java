package me.dakbutfly.jmockit_example.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.dakbutfly.jmockit_example.data.UserListData;
import me.dakbutfly.jmockit_example.exception.NotJsonFormatException;
import me.dakbutfly.jmockit_example.exception.NotMatchJsonToSomeException;
import me.dakbutfly.spark_api.ResponseBody;
import me.dakbutfly.spark_api.User;

import me.dakbutfly.spark_api.Application;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static me.dakbutfly.jmockit_example.common.ConvertJsonToInstance.convertJsonStringTo;
import static me.dakbutfly.jmockit_example.common.ConvertJsonToInstance.toJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static spark.Spark.stop;

@DisplayName("사용자 API 테스트")
class TestUserApi {

    @BeforeAll
    static void 서버_실행() {
        Application.main(null);
    }

    @Test
    void 성공시_사용자정보를_출력() throws IOException {
        String body = ApiCall.findUsers();

        assertEquals(body, "{\"data\":{\"userList\":[]}}");
    }

    @Nested
    @DisplayName("사용자 등록 후 테스트")
    class TestUserApiBeforeSetUser {

        @BeforeEach
        void 사용자등록() throws IOException {
            User user = getUserInstance();
            String json = toJson(user);

            String body = ApiCall.registerUser(json);

            user.setId(1L);
            String userJson = toJson(user);
            assertEquals(body, "{\"data\":{\"user\":"+userJson+"}}");
        }

        @Test
        void 사용자_한명이_등록되_있어야함() throws IOException {
            String body = ApiCall.findUsers();

            User user = User.builder().id(1L).name("강현구").age(32).build();

            assertEquals(body, "{\"data\":{\"userList\":["+ toJson(user) +"]}}");

        }

        @Test
        void 사용자_한명_더_등록하면_2명이_있고_ID가_다름() throws IOException {
            // given
            User user = User.builder().name("강현구2").age(32).build();
            String json = toJson(user);
            ApiCall.registerUser(json);

            // when
            String body = ApiCall.findUsers();

            // then
            UserListData userListData = covertData(body);
            List<User> userList1 = userListData.userList;
            User user1 = userList1.get(0);
            User user2 = userList1.get(1);

            assertNotEquals(user1.getId(), user2.getId());
        }

        @Test
        void 사용자_삭제시_정상응답을_받음() throws IOException, NotMatchJsonToSomeException, NotJsonFormatException {
            // given

            // when
            String userId = "1";
            String body = ApiCall.deleteUser(userId);

            // then
            ResponseBody responseBody = convertJsonStringTo(body, ResponseBody.class);
            assertEquals(responseBody.getCodeno(), 2000L);
            assertEquals(responseBody.getCode(), "SUCCESS");
        }

        @Test
        void 사용자_삭제시_없는_사용자_ID로_호출시_에러_발생() throws IOException, NotMatchJsonToSomeException, NotJsonFormatException {
            // given

            // when
            String userId = "2";
            String body = ApiCall.deleteUser(userId);

            // then
            ResponseBody responseBody = convertJsonStringTo(body, ResponseBody.class);
            assertEquals(responseBody.getCodeno(), 5000L);
            assertEquals(responseBody.getCode(), "ERROR");

        }

        @AfterEach
        void 레포지토리_클리어() {
            Application.repositorysClear();
        }

        private User getUserInstance() {
            return User.builder()
                    .name("강현구")
                    .age(32)
                    .build();
        }

        private UserListData covertData(String jsonReponse) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonReponse);
            JsonNode dataNode = rootNode.path("data");
            return objectMapper.convertValue(dataNode, UserListData.class);
        }
    }

    @AfterAll
    static void 서버_종료() {
        stop();
    }
}