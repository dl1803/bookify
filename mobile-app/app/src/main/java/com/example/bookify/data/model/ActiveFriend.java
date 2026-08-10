package com.example.bookify.data.model;

public class ActiveFriend {
    private String id;
    private String name;
    private String avatarUrl;
    private boolean isOnline;

    public ActiveFriend(String id, String name, String avatarUrl, boolean isOnline) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.isOnline = isOnline;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAvatarUrl() { return avatarUrl; }
    public boolean isOnline() { return isOnline; }
}
