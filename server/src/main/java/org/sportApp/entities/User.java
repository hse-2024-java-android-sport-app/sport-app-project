package org.sportApp.entities;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.Month;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String secondName;
    private String login;
    private String password;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    public enum Kind {sportsman, coach}
    @Enumerated(EnumType.STRING)
    private Kind type;

    protected User() {}

//    public User(String login, String password) {
//        this.firstName = "";
//        this.secondName = "";
//        this.login = login;
//        this.password = password;
//        this.dateOfBirth = Date.of(2000, Month.JANUARY, 1);
//        this.type = Kind.sportsman;
//    }

    public User(String fName, String sName, String login, String password, Date dateOfBirth, Kind type) {
        this.firstName = fName;
        this.secondName = sName;
        this.login = login;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', secondName='%s', login='%s', dateOfBirth='%s', type='%s']",
                this.id, this.firstName, this.secondName, this.login, this.dateOfBirth, this.type);
    }

    public Long getId() {
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
