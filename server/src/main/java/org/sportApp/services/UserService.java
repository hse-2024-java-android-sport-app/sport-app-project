package org.sportApp.services;

import org.sportApp.entities.User;
import org.sportApp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final long notExist = -1;

    @Autowired
    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        //FUTURE add password encoding
        return userRepository.save(user);
    }

    public long authorizeUser(String login, String password) {
        if (userRepository.existsByLoginAndPassword(login, password)) {
            return userRepository.getByLogin(login).getId();
        }
        return notExist;
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
}
