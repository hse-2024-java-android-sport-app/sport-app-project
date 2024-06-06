package org.sportApp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.sportApp.registration.UserDto;

public class UserManager {
    private static UserDto instance;
    private Long userId = (long) -1;

    private UserManager() {
    }

    public static synchronized UserDto getInstance() {
        if (instance == null) {
            instance = new UserDto();
        }
        return instance;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
