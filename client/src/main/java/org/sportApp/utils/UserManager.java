package org.sportApp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static UserManager instance;
    private Long userId = (long) -1;

    private UserManager() {
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
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
