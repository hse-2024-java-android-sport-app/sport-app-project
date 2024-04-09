package org.sportApp.repo;

import org.sportApp.entities.User;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByLogin(String login);
    boolean existsByLoginAndPassword(String login, String password);
    User getByLogin(String login);
}