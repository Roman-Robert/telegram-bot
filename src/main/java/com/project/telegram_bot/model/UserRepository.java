package com.project.telegram_bot.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends CrudRepository <User, Long> {
    public List<User> findUsersIsActiveYes();

}
