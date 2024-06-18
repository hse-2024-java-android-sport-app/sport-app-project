package org.sportApp.utils;

import org.sportApp.dto.UserDto;

public class UserManager {
    private static UserDto instance;

    public static int myPosition;

    private UserManager() {
    }

    public static synchronized UserDto getInstance() {
        if (instance == null) {
            instance = new UserDto();
        }
        return instance;
    }

    public void setUserId(Long userId) {
        getInstance().setId(userId);
    }

    public Long getUserId() {
        return getInstance().getId();
    }

    public void setUserType(UserDto.Kind type) {
        getInstance().setType(type);
    }

    public UserDto.Kind getUserType() {
        return getInstance().getType();
    }

}
