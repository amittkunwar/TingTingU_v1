package com.example.tingtingu_v1;

public class User {
    private String name;
    private String lastMessage;
    private String profileImageUrl;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String lastMessage, String profileImageUrl) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
