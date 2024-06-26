package org.sportApp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
    }

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

    @ColumnDefault("0")
    private int ratingScore = 0;


    protected User() {}

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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id+
                ", \nfirstName='" + ((firstName == null) ? null : firstName) + '\'' +
                ", \nsecondName='" + ((secondName == null) ? null : secondName) + '\'' +
                ", \nlogin='" + ((login == null) ? null : login) + '\'' +
                ", \npassword='" + ((password == null) ? null : password) + '\'' +
                ", \ndateOfBirth=" + ((dateOfBirth == null) ? null : dateOfBirth) +
                ", \ntype=" + ((type == null) ? null : type) +
                ", \ncoach=" + ((coach == null) ? null : coach.getId())+
                ", \nratingScore=" + ratingScore +
                '}';
    }
}
