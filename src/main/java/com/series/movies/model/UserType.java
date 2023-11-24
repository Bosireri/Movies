package com.series.movies.model;

public enum UserType {

    ADMIN("ADMIN"), LOCALUSER("LOCALUSER");

    private final String type;

    UserType(String string) {
        type = string;
    }

    @Override
    public String toString() {
        return type;
    }

}
