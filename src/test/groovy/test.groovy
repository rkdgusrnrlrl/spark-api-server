import me.dakbutfly.spark_api.User
import me.dakbutfly.spark_api.repository.UserRepoImplByList
import me.dakbutfly.spark_api.repository.UserRepository
import spock.lang.Specification

class TestUserRepo extends Specification {
    def repo = new UserRepoImplByList()

    def "사용자를 등록하면 ID 값이 넣어서 반환"() {
        given:
         def user = User.builder().name("강현구").age(32).build()

        when:
         def savedUser = repo.save(user)

        then:
        savedUser.id == 1L
        savedUser.name == "강현구"
        savedUser.age == 32
    }

    def "사용자 두번 등록하면 두 User의 Id값이 달라야함"() {
        given:
        def user1 = User.builder().name("강현구1").age(32).build();
        def user2 = User.builder().name("강현구2").age(32).build();

        when:
        def newUser1 = repo.save(user1);
        def newUser2 = repo.save(user2);

        then:
        newUser1.id != newUser2.id
    }
}