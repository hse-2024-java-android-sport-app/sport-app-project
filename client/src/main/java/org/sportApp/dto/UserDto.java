package org.sportApp.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class UserDto implements Serializable {
    private long id;
    private String firstName;
    private String secondName;
    private String login;
    private String password;

    public enum Kind {sportsman, coach}

    private Kind type;
    //    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date dateOfBirth;

    private long coachId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCoachId() {
        return coachId;
    }

    public void setCoachId(long coachId) {
        this.coachId = coachId;
    }

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

    public String getFullName() {
        return getFirstName() + " " + getSecondName();
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
        if (dateOfBirth != null) {
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(dateOfBirth);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        }
        else {
            return 20;
        }
    }

    public void setInfo(UserDto info) {
        this.id = info.getId();
        this.login = info.getLogin();
        this.firstName = info.getFirstName();
        this.secondName = info.getSecondName();
        this.type = info.getType();
        this.dateOfBirth = info.getDateOfBirth();
        this.coachId = info.getCoachId();
    }
}