package com.beetech.springsecurity.domain.enums;

import lombok.Getter;

@Getter
public enum DeleteFlag {
    DELETED(true),
    NORMAL(false);

    private boolean isDeleteFlag;

    private DeleteFlag(boolean isDeleteFlag) {
        this.isDeleteFlag = isDeleteFlag;
    }

    public static DeleteFlag getDeleteFlag(boolean isDeleteFlag) {
        if (isDeleteFlag) {
            return DeleteFlag.DELETED;
        } else {
            return DeleteFlag.NORMAL;
        }
    }
}
