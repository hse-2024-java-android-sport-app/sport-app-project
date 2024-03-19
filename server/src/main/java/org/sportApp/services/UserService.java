package org.sportApp.services;

import org.sportApp.entities.User;
import org.sportApp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService (UserRepository catRepository) {
        this.userRepository = catRepository;
    }

    public User registerUser(User user) {
        //FUTURE add password encoding
        return userRepository.save(user);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
}
