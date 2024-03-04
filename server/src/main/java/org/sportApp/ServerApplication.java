package org.sportApp;

import org.sportApp.controller.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sportApp.controller.UserStorable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
        System.out.println("Server started...");
    }

    private static final Logger log = LoggerFactory.getLogger(ServerApplication.class);

    private Date getData(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, date);
        return cal.getTime();
    }

    @Bean
    public CommandLineRunner demo(UserStorable repository) {
        return (args) -> {
            // save a few Users
            repository.save(new User("Jack", "Bauer", "password", User.Kind.coach, getData(1900, 1, 20)));
            repository.save(new User("Chloe", "O'Brian", "password", User.Kind.sportsman, getData(1900, 2, 20)));
            repository.save(new User("Kim", "Bauer", "password", User.Kind.coach, getData(1900, 3, 20)));
            repository.save(new User("David", "Palmer", "password", User.Kind.sportsman, getData(1899, 1, 20)));
            repository.save(new User("Michelle", "Dessler", "password", User.Kind.coach, getData(1900, 10, 20)));

            // fetch all Users
            log.info("Users found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(User -> {
                log.info(User.toString());
            });
            log.info("");

            // fetch an individual User by ID
            User User = repository.findById(1L);
            log.info("User found with findById(1L):");
            log.info("--------------------------------");
            log.info(User.toString());
            log.info("");

            // fetch Users by last name
            log.info("User found with findByName('David'):");
            log.info("--------------------------------------------");
            repository.findByName("David").forEach(bauer -> {
                log.info(bauer.toString());
            });
            log.info("");
        };
    }


}