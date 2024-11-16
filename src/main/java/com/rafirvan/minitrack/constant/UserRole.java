package com.rafirvan.minitrack.constant;


import lombok.Getter;

@Getter
public enum UserRole {

    ROLE_USER ("pengguna"),
    ROLE_ADMIN("admin");

    private final String name;

    UserRole(String name){
        this.name = name;
    }

    public static UserRole findByName(String name) {
        for(UserRole role : values()) {
            if(role.name().equals(name) || role.name.equals(name.toLowerCase())) {
                return role;
            }
        }
        return null;
    }
}