package com.library.model;

public enum OrderType {
    READINGROOM, LIBRARYCARD;

    static OrderType of(String name) {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.name().equalsIgnoreCase(name)) {
                return orderType;
            }
        }
        return null;
    }
}