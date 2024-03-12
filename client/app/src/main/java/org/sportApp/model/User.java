package org.sportApp.model;

public class User {
    private final String name;
    private final int type; //TODO : replace int with enum

    public User(String name, int type) {
        this.name = name;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public int getType() {
        return type;
    }
}
