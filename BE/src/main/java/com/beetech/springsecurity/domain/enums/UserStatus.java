package com.beetech.springsecurity.domain.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    LOCKED(true),
    NORMAL(false);

    private boolean isLocked;

    private UserStatus(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public static UserStatus getUserStatus(boolean isLocked) {
        if (isLocked) {
            return UserStatus.LOCKED;
        } else {
            return UserStatus.NORMAL;
        }
    }
}
