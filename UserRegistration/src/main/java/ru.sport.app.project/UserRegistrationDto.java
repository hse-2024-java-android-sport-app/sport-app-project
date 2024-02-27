package ru.sport.app.project;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import org.springframework.format.annotation.DateTimeFormat;

public class UserRegistrationDto {
    @SerializedName("name")
    private String name;
    @SerializedName("login")
    private String login;
    @SerializedName("password")
    private char[] password;
    public enum Kind {sportsman, coach}
    @SerializedName("type")
    private Kind type;
    @SerializedName("dateOfBirth")
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

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public UserRegistrationDto(String name, String login, char[] password, Kind type, Date dateOfBirth) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.type = type;
        this.dateOfBirth = dateOfBirth;
    }
}

