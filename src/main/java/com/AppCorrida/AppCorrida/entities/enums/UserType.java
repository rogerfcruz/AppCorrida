package com.AppCorrida.AppCorrida.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
    PASSENGER(1),
    DRIVER(2);

    private int code;

    private UserType(int code){
        this.code = code;
    }

    @JsonCreator
    public static UserType valueOf(int code) {
        for (UserType value : UserType.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("User Type Code invalid.");
    }

    @JsonValue
    public int getCode() {
        return code;
    }
}
