package com.example.healthcareappdoctor;

public class Patient {
    private String name, UID, token;

    public Patient(String name, String UID, String token) {
        this.name = name;
        this.UID = UID;
        this.token = token;
    }

    public Patient() {
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
