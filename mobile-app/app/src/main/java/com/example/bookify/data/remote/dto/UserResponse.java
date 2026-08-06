package com.example.bookify.data.remote.dto;

import java.util.List;

public class UserResponse {
    private String id;
    private String username;
    private String email;
    private boolean emailVerified;
    private List<RoleResponse> roles;

    public UserResponse() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public List<RoleResponse> getRoles() { return roles; }
    public void setRoles(List<RoleResponse> roles) { this.roles = roles; }
}
