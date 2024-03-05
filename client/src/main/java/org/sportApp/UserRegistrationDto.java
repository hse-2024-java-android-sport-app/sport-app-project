package org.sportApp;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import org.springframework.format.annotation.DateTimeFormat;

public class UserRegistrationDto {
    private String name;
    private String login;
    private String password;

    public enum Kind {sportsman, coach}

    private Kind type;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Kind getType() {
        return type;
    }

    public void setType(Kind type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public UserRegistrationDto(String name, String login, String password, Kind type, Date dateOfBirth) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.type = type;
        this.dateOfBirth = dateOfBirth;
    }
}

