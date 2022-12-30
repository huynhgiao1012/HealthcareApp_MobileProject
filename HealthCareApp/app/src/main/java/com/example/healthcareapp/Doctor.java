package com.example.healthcareapp;

public class Doctor {
    private String UID, name, token;

    public Doctor(String name, String UID, String token) {
        this.name = name;
        this.UID = UID;
        this.token = token;
    }

    public Doctor() {

    }

    public String getName() {
        return name;
    }

    public String getUID() {
        return UID;
    }

    public String getToken() {
        return token;
    }
}
