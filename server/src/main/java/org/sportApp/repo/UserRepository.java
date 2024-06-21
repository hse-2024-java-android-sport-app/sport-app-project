package org.sportApp.repo;

import org.sportApp.entities.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByLogin(String login);

    boolean existsByLoginAndPassword(String login, String password);

    Optional<User> getByLogin(String login);

    Optional<User> getByIdAndType(long id, User.Kind type);

    List<User> findAllByCoachId(long coach_id);

    List<User> findAllByFirstNameContainsIgnoreCaseOrSecondNameContainsIgnoreCaseOrLoginContainsIgnoreCase
            (String firstName, String secondName, String login);
}