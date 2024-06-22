package org.sportApp.services;

import org.sportApp.entities.*;
import org.sportApp.repo.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notifRepository;
    private final UserService userService;

    public NotificationService(NotificationRepository notifRepository, UserService userService) {
        this.notifRepository = notifRepository;
        this.userService = userService;
    }

    public void sendPlan(Plan plan, String message) {
        Notification notif = new Notification();
        Optional<User> sportsman = userService.getUser(plan.getSportsmanId());
        if (sportsman.isEmpty()) {
            return;
        }
        notif.setUserTo(sportsman.get());
        notif.setUserfromId(plan.getCoachId());
        notif.setMessage(message);
        notifRepository.save(notif);
    }

    public void sendEventCompleted(TrainingEvent event) {
        Notification notif = new Notification();
        long sportsmanId = event.getPlan().getSportsmanId();
        Optional<User> sportsman = userService.getUserAndCheckType(sportsmanId, User.Kind.sportsman);
        Optional<User> coach = userService.getUserAndCheckType(event.getPlan().getCoachId(), User.Kind.coach);
        if (sportsman.isEmpty() || coach.isEmpty()) {
            return;
        }
        notif.setUserfromId(sportsmanId);
        notif.setUserTo(coach.get());
        String beginWith = "Sportsman "
                + sportsman.get().getFirstName() + " "
                + sportsman.get().getSecondName();
        if (event.isCompleted()) {
            beginWith += " complete training event in plan ";
        } else {
            beginWith += " set \"Not completed\" for training event in plan ";
        }
        notif.setMessage(beginWith + event.getPlan().getName());
        notifRepository.save(notif);
    }


    public List<Notification> getNotificationsByUserId(long userId) {
        Optional<User> user = userService.getUser(userId);
        if (user.isEmpty()) {
            return List.of();
        }
        return notifRepository.getNotificationByUserTo(user.get());
    }

    public List<String> getNotificationMessagesByUserId(User user) {
        return notifRepository.getNotificationByUserTo(user).stream()
                .map(Notification::getMessage)
                .toList();
    }
}
