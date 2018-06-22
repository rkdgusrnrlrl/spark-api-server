package me.dakbutfly.jmockit_example.unit;

import me.dakbutfly.spark_api.User;
import me.dakbutfly.spark_api.repository.UserRepoImplByList;
import me.dakbutfly.spark_api.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserRepoImplByList Unit 테스트")
class TestUserRepo {

    private UserRepository userRepository = new UserRepoImplByList();

    @Test
    void 사용자_목록_가져오기_테스트() {
        List<User> userList = userRepository.findAll();
        assertEquals(userList.size(), 0);
    }

    @Test
    void 사용자_등록하면_ID_값을_넣이_반환() {
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
    void 사용자_두번_등록하면_두_User의_Id값이_달라야함() {
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
    void 없는_사용자ID로_특정사용자를_가져온다면_null임() {

        // when
        User newUser1 = userRepository.findUserById(1L);

        // then
        assertNull(newUser1);
    }

    @Test
    void 사용자ID로_특정사용자를_가져올수_있음() {
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

    @Test
    void 사용자_ID로_사용자_유무를_체크할_수_있음() {
        // given
        User user = User.builder().name("강현구").age(32).build();
        userRepository.save(user);

        // when
        boolean isExistUserHaveId1 = userRepository.existById(1L);
        boolean isExistUserHaveId2 = userRepository.existById(2L);

        // then
        assertTrue(isExistUserHaveId1);
        assertFalse(isExistUserHaveId2);
    }

    @Test
    void 없는_사용자_ID로_삭제_요청시_false_리턴() {
        // given
        User user = User.builder().name("강현구").age(32).build();
        userRepository.save(user);

        // when
        boolean isDeleted = userRepository.deleteById(2L);

        // then
        assertFalse(isDeleted);
    }

    @Test
    void 있는_사용자_ID로_삭제_요청시_true_리턴() {
        // given
        User user = User.builder().name("강현구").age(32).build();
        userRepository.save(user);

        // when
        boolean isDeleted = userRepository.deleteById(1L);

        // then
        assertTrue(isDeleted);
    }

    @Test
    void 있는_사용자_ID로_삭제_요청시_해당_사용자를_목록에서_없음() {
        // given
        User user = User.builder().name("강현구").age(32).build();
        userRepository.save(user);

        // when
        userRepository.deleteById(1L);
        List<User> userList = userRepository.findAll();

        // then
        User userFound = userList.stream().filter((user1 -> user.getId() == 1L)).findAny().orElse(null);
        assertNull(userFound);
    }

    @AfterEach
    void 레파지토리_초기화() {
        userRepository.clear();
    }
}
