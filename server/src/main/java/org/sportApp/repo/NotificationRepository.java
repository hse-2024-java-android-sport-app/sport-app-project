package org.sportApp.repo;

import org.sportApp.entities.Notification;
import org.sportApp.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository  extends CrudRepository<Notification, Long> {
    List<Notification> getNotificationByUserTo(User user);
}
