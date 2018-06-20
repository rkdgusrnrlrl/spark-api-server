package me.dakbutfly.jmockit_example.unit;

import me.dakbutfly.spark_api.User;
import me.dakbutfly.spark_api.repository.UserRepository;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestUserRepo {

    class UserRepoImplByList implements UserRepository {
        private List<User> userListInRepo = new ArrayList<>();
        private long userId = 0L;


        @Override
        public List<User> findAll() {
            return userListInRepo;
        }

        @Override
        public void clear() {
            userListInRepo.clear();
        }

        @Override
        public User save(User user) {
            user.setId(++userId);
            return user;
        }
    };

    private UserRepository userRepository = new UserRepoImplByList();

    @Test
    public void 사용자_목록_가져오기_테스트() {
        List<User> userList = userRepository.findAll();
        assertEquals(userList.size(), 0);
    }

    @Test
    public void 사용자_등록하면_ID_값을_넣이_반환() {
        // given
        User user = User.builder().name("강현구").age(32).build();

        // when
        User newUser = userRepository.save(user);

        // then
        assertEquals(newUser.getId(), 1L);
        assertEquals(newUser.getName(), "강현구");
        assertEquals(newUser.getAge(), 32);
    }

    @Test
    public void 사용자_두번_등록하면_두_User의_Id값이_달라야함() {
        // given
        User user1 = User.builder().name("강현구1").age(32).build();
        User user2 = User.builder().name("강현구2").age(32).build();

        // when
        User newUser1 = userRepository.save(user1);
        User newUser2 = userRepository.save(user2);

        // then
        assertNotEquals(newUser1.getId(), newUser2.getId());
    }

    @AfterEach
    public void 레파지토리_초기화() {
        userRepository.clear();
    }
}
