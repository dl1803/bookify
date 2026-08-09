package com.example.bookify.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("avatar")
    private String avatar;
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

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getAvatar() { return avatar; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getDob() { return dob; }
    public String getCity() { return city; }
    public String getBio() { return bio; }
}
