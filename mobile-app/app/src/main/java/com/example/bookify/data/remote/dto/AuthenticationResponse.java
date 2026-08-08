package com.example.bookify.data.remote.dto;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class AuthenticationResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("expiryTime")
    private Date expiryTime;

    public String getToken() {
        return token;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }
}
