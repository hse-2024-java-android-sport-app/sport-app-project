package org.sportApp.controller;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserStorable extends CrudRepository<User, Long> {

    List<User> findByName(String name);

    User findById(long id);
}