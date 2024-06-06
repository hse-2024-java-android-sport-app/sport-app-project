package org.sportApp.registration;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class UserDto implements Serializable {
    private String firstName;
    private String secondName;
    private String login;
    private String password;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public enum Kind {sportsman, coach}

    private Kind type;
    private Date dateOfBirth;

    public Kind getType() {
        return type;
    }

    public void setType(Kind type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date date) {
        this.dateOfBirth = date;
    }

    public UserDto() {
    }

    public int getAge() {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(dateOfBirth);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
}