package org.sportApp.services;

import org.hibernate.annotations.Immutable;
import org.sportApp.entities.Notification;
import org.sportApp.entities.Subscriber;
import org.sportApp.entities.TrainingEvent;
import org.sportApp.entities.User;
import org.sportApp.repo.SubscriberRepository;
import org.sportApp.repo.TrainingEventRepository;
import org.sportApp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@EnableScheduling
public class UserService {

    private final UserRepository userRepository;
    private final SubscriberRepository subscriberRepository;
    private final TrainingEventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService (UserRepository userRepository, SubscriberRepository subscriberRepository, TrainingEventRepository eventRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.subscriberRepository = subscriberRepository;
        this.eventRepository = eventRepository;
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
        System.out.println("AAAAA");
        return userRepository.findById(userId);
    }

    public Optional<User> getUserAndCheckType(long userId, User.Kind type) {
        return userRepository.getByIdAndType(userId, type);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> getAllSportsmenByCoachId(long coachId) {
        return userRepository.findAllByCoachId(coachId);
    }

    public List<User> searchUser(String searchString, User.Kind type) {
        Stream<User> result = Stream.empty();
        for (String word : searchString.split("\\s+")) {
            List<User> foundCoaches = userRepository.findAllByFirstNameContainsIgnoreCaseOrSecondNameContainsIgnoreCaseOrLoginContainsIgnoreCase(word, word, word);
            result = Stream.concat(result, foundCoaches.stream());
        }
        return result.filter(u -> u.getType() == type).toList();
    }

    public Optional<Long> editCoach(long sportsmanId, long coachId) {
        Optional<User> sportsman = getUserAndCheckType(sportsmanId, User.Kind.sportsman);
        Optional<User> coach = getUserAndCheckType(coachId, User.Kind.coach);
        if (sportsman.isPresent() && coach.isPresent()){
            sportsman.get().setCoach(coach.get());
            return Optional.of(userRepository.save(sportsman.get()).getId());
        }
        return Optional.empty();
    }

    public boolean editRating(long sportsmanId, int updateRatingScore) {
        if (updateRatingScore == 0) {
            return true;
        }
        Optional<User> sportsman = getUserAndCheckType(sportsmanId, User.Kind.sportsman);
        if (sportsman.isPresent()){
            sportsman.get().setRatingScore(sportsman.get().getRatingScore() + updateRatingScore);
            userRepository.save(sportsman.get());
            return true;
        }
        return false;
    }

    public Optional<?> addSubscription(long userId, long followToId) {
        Optional<User> user = getUser(userId);
        Optional<User> followToUser = getUser(followToId);
        if (user.isPresent() && followToUser.isPresent()) {
            Subscriber subscriber = new Subscriber();
            subscriber.setFollower(user.get());
            subscriber.setMainUser(followToUser.get());
            subscriberRepository.save(subscriber);
            return Optional.of(true);
        }
        return Optional.empty();
    }

    public List<User> getFollowers(User user) {
        return user.getFollowers().stream()
                .map(Subscriber::getFollower)
                .toList();
    }

    public List<User> getSubscriptions(User user) {
       return user.getSubscriptions().stream()
               .map(Subscriber::getMainUser)
               .toList();
    }

    public List<?> getRating(User sportsman) {
        List<User> subscriptions = new ArrayList<>(getSubscriptions(sportsman));
        System.out.println(subscriptions.size());
        subscriptions.add(sportsman);
        subscriptions.sort((userA, userB) -> (userB.getRatingScore() - userA.getRatingScore()));
        return subscriptions;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void updateRating() {
        LocalDate cur = LocalDate.now();
        Date weekAgo = Date.from(cur.minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println("TIME now: " + cur);
        System.out.println("TIME week ago: " + weekAgo);
        List<TrainingEvent> allCompletedWeekAgoEvents = eventRepository.getTrainingEventsByCompletedTrueAndDateIs(weekAgo);
        System.out.println("TIME completedEventsSize: " + allCompletedWeekAgoEvents.size());
        allCompletedWeekAgoEvents.stream()
                .map(event -> {
                    System.out.println(event.getTraining().getUserId());
                    return event.getTraining().getUserId();
                })
                .distinct()
                .forEach(userId -> editRating(userId, -10));
    }
}
