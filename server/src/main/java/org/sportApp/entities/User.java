package org.sportApp.entities;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Month;

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
    private String name;
    private String login;
    private String password;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    public enum Kind {sportsman, coach}
    @Enumerated(EnumType.STRING)
    private Kind type;

    protected User() {}

    public User(String login, String password) {
        this.name = "";
        this.login = login;
        this.password = password;
        this.dateOfBirth = LocalDate.of(2000, Month.JANUARY, 1);
        this.type = Kind.sportsman;
    }

    public User(String name, String login, String password, LocalDate dateOfBirth, Kind type) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, login='%s', name='%s', type='%s', dateOfBirth='%s']",
                this.id, this.login, this.name, this.type, this.dateOfBirth);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
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
}
