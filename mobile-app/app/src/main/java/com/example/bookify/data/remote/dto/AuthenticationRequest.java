package com.example.bookify.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class AuthenticationRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
