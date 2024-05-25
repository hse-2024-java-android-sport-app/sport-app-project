package org.sportApp.services;

import org.sportApp.entities.User;
import org.sportApp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final long notExist = -1;

    @Autowired
    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        //TODO add password encoding
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

    public boolean notExistsById(long userId) {
        return !userRepository.existsById(userId);
    }

    public Optional<User.Kind> getUserType(long userId) {
        return userRepository.findById(userId).map(User::getType);
    }

    public Optional<User> getUser(long userId) {
        return userRepository.findById(userId);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> getAllSportsmenByCoachId(long coachId) {
        return userRepository.findAllByCoachId(coachId);
    }

    public Optional<Boolean> getIsCoachSet(long sportsmanId) {
        return userRepository.findById(sportsmanId).map(user -> user.getCoach() == null);
    }

    public List<User> searchCoaches(String searchString) {
        Stream<User> result = Stream.empty();
        for (String word : searchString.split("\\s+")) {
            List<User> foundCoaches = userRepository.findAllByFirstNameContainsIgnoreCaseOrSecondNameContainsIgnoreCaseOrLoginContainsIgnoreCase(word, word, word);
            result = Stream.concat(result, foundCoaches.stream());
        }
        return result.toList();
    }
    public Optional<Long> editCoach(long sportsmanId, long coachId) {
        Optional<User> sportsman = getUser(sportsmanId).filter(sp -> sp.getType() == User.Kind.sportsman);
        Optional<User> coach = getUser(coachId).filter(sp -> sp.getType() == User.Kind.coach);
        if(sportsman.isPresent() && coach.isPresent()){
            sportsman.get().setCoach(coach.get());
            return Optional.of(userRepository.save(sportsman.get()).getId());
        }
        return Optional.empty();
    }
}
