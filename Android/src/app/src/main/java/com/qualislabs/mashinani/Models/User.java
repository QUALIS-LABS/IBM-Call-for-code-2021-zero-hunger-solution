package com.qualislabs.mashinani.Models;

public class User {
    private String userName, email, userType, token;
    private int id;

    public User() {
    }

    public User(String userName, String email, String userType, String token, int id) {
        this.userName = userName;
        this.email = email;
        this.userType = userType;
        this.token = token;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
