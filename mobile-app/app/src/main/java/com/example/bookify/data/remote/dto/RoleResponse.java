package com.example.bookify.data.remote.dto;

import java.util.List;

public class RoleResponse {
    private String name;
    private String description;
    private List<PermissionResponse> permissions;

    public RoleResponse() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<PermissionResponse> getPermissions() { return permissions; }
    public void setPermissions(List<PermissionResponse> permissions) { this.permissions = permissions; }
}
