package org.sportApp.entities;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
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
}
