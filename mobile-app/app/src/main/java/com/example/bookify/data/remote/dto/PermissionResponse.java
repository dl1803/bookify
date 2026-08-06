package com.example.bookify.data.remote.dto;

public class PermissionResponse {
    private String name;
    private String description;

    public PermissionResponse() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
