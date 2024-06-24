package org.sportApp.entities;

import jakarta.persistence.*;

@Entity
@Table(name="Subscribers")
public class Subscriber {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="main_user_id")
    private User mainUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="follower_id")
    private User follower;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getMainUser() {
        return mainUser;
    }

    public void setMainUser(User mainUser) {
        this.mainUser = mainUser;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }
}