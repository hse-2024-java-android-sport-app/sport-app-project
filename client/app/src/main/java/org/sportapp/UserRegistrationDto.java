package org.sportapp;

import java.io.Serializable;
import java.util.Date;


public class UserRegistrationDto implements Serializable {
    private String firstName;
    private String secondName;
    private String login;
    private String password;

    public enum Kind {sportsman, coach}

    private Kind type;
    private Date date;

    public Kind getType() {
        return type;
    }

    public void setType(Kind type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String name) {
        this.secondName = secondName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserRegistrationDto() {
    }
}