package org.sportApp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.sportApp.entities.User;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name="Notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long notificationId;
    private long userfromId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_to_id")
    @JsonBackReference
    private User userTo;
    private String message;

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserfromId() {
        return userfromId;
    }

    public void setUserfromId(long userfromId) {
        this.userfromId = userfromId;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo( User userTo) {
        this.userTo = userTo;
    }
}