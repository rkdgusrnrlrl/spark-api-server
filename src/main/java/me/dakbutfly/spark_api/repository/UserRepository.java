package me.dakbutfly.spark_api.repository;

import me.dakbutfly.spark_api.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    void clear();
    User save(User user);
}
