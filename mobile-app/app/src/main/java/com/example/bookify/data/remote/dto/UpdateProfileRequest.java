package com.example.bookify.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    @SerializedName("email")
    private String email;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("dob")
    private String dob;
    @SerializedName("city")
    private String city;
    @SerializedName("bio")
    private String bio;

    public UpdateProfileRequest(String email, String firstName, String lastName, String dob, String city, String bio) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.city = city;
        this.bio = bio;
    }
}
