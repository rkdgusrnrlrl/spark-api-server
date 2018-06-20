package me.dakbutfly.jmockit_example.unit;

import me.dakbutfly.spark_api.User;
import me.dakbutfly.spark_api.repository.UserRepoImplByList;
import me.dakbutfly.spark_api.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestUserRepo {

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

    @Test
    public void 없는_사용자ID로_특정사용자를_가져온다면_null임() {

        // when
        User newUser1 = userRepository.findUserById(1L);

        // then
        assertNull(newUser1);
    }

    @Test
    public void 사용자ID로_특정사용자를_가져올수_있음() {
        // given
        User user = User.builder().name("강현구").age(32).build();
        userRepository.save(user);

        // when
        User newUser1 = userRepository.findUserById(1L);

        // then
        assertEquals(newUser1.getId(), 1L);
        assertEquals(newUser1.getName(), "강현구");
        assertEquals(newUser1.getAge(), 32);
    }

    @AfterEach
    public void 레파지토리_초기화() {
        userRepository.clear();
    }
}
