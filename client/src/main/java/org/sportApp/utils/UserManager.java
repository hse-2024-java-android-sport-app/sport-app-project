package org.sportApp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.sportApp.dto.UserDto;

public class UserManager {
    private static UserDto instance;
    private Long userId = (long) -1;
    private static UserDto lastUser;

    private UserManager() {
    }

    public static synchronized UserDto getInstance() {
        if (instance == null) {
            instance = new UserDto();
        }
        return instance;
    }

    public static void setLastUser(UserDto user) {
        lastUser = user;
    }

    public static UserDto getLastUser() {
        return lastUser;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}