package me.dakbutfly.spark_api.repository;

import me.dakbutfly.spark_api.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepoImplByList implements UserRepository {
    private List<User> userListInRepo = new ArrayList<User>();
    private long userId = 0L;


    @Override
    public List<User> findAll() {
        return userListInRepo;
    }

    @Override
    public void clear() {
        userId = 0L;
        userListInRepo.clear();
    }

    @Override
    public User save(User user) {
        user.setId(++userId);
        userListInRepo.add(user);
        return user;
    }

    @Override
    public User findUserById(long id) {
        return userListInRepo.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }
}