//package org.sportApp.entities;
//
//import jakarta.persistence.*;
//import org.sportApp.entities.User;
//
//@Entity
//@Table(name="Notifications")
//public class Notification {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private long notificationId;
//    private User sportsman;
//    private User coach;
//    private String message;
//
//    public long getNotificationId() {
//        return notificationId;
//    }
//
//    public void setNotificationId(long notificationId) {
//        this.notificationId = notificationId;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public User getSportsman() {
//        return sportsman;
//    }
//
//    public void setSportsman(User sportsman) {
//        this.sportsman = sportsman;
//    }
//
//    public User getCoach() {
//        return coach;
//    }
//
//    public void setCoach(User coach) {
//        this.coach = coach;
//    }
//}