package org.sportApp.services;

import org.sportApp.entities.Subscriber;
import org.sportApp.entities.User;
import org.sportApp.repo.SubscriberRepository;
import org.sportApp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscriberRepository subscriberRepository;
    private final PasswordEncoder passwordEncoder;
    private final long notExist = -1;

    @Autowired
    public UserService (UserRepository userRepository, SubscriberRepository subscriberRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.subscriberRepository = subscriberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> authorizeUser(String login, String password) {
        return userRepository
                .getByLogin(login)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
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
        return searchUser(searchString).stream()
                .filter(u -> u.getType() == User.Kind.coach)
                .toList();
    }

    public List<User> searchSportsman(String searchString) {
        return searchUser(searchString).stream()
                .filter(u -> u.getType() == User.Kind.sportsman)
                .toList();
    }

    private List<User> searchUser(String searchString) {
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

    public boolean addSubscription(long userId, long followToId) {
        Optional<User> user = getUser(userId);
        Optional<User> followToUser = getUser(followToId);
        if (user.isPresent() && followToUser.isPresent()) {
            Subscriber subscriber = new Subscriber();
            subscriber.setFollower(user.get());
            subscriber.setMainUser(followToUser.get());
            subscriberRepository.save(subscriber);
            return true;
        }
        return false;
    }

    public List<User> getFollowers(long userId) {
        return getSubscribersByUser(userId, User::getFollowers, Subscriber::getFollower);
    }

    public List<User> getSubscriptions(long userId) {
        return getSubscribersByUser(userId, User::getSubscriptions, Subscriber::getMainUser);
    }

    private List<User> getSubscribersByUser(long userId, Function<User, List<Subscriber>> mapper, Function<Subscriber, User> convert) {
        return userRepository.findById(userId)
                .map(user -> mapper.apply(user)
                        .stream()
                        .map(convert)
                        .toList())
                .orElseGet(List::of);
    }
}
