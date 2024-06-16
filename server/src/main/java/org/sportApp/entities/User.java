package org.sportApp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
@DynamicUpdate
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String firstName;
    private String secondName;
    private String login;
    private String password;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    public enum Kind {sportsman, coach}
    @Enumerated(EnumType.STRING)
    private Kind type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coach_id")
    private User coach;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private List<User> sportsmen = new ArrayList<>();

    @OneToMany(mappedBy = "mainUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Subscriber> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Subscriber> subscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "userTo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Notification> notifications = new ArrayList<>();


    protected User() {}

    public User(String fName, String sName, String login, String password, Date dateOfBirth, Kind type) {
        this.firstName = fName;
        this.secondName = sName;
        this.login = login;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
    }

    public long getId() {
        return this.id;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Kind getType() {
        return this.type;
    }

    public void setType(Kind type) {
        this.type = type;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String fName) {
        this.firstName = fName;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public void setSecondName(String sName) {
        this.secondName = sName;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getCoach() {
        return coach;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }

    public List<User> getSportsmen() {
        return sportsmen;
    }

    public void setSportsmen(List<User> sportsmen) {
        this.sportsmen = sportsmen;
    }

    public List<Subscriber> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Subscriber> followers) {
        this.followers = followers;
    }

    public List<Subscriber> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscriber> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
