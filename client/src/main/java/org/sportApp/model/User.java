package org.sportApp.model;

public class User {
    private static int idCount = 0;
    private int id;
    private final String name;
    private final int type; //TODO : replace int with enum

    public User(String name, int type) {
        this.name = name;
        this.type = type;
        id = idCount;
        ++idCount;
    }
    public String getName() {
        return name;
    }
    public int getType() {
        return type;
    }
}
