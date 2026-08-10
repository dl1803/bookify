package com.example.bookify.data.model;

import java.util.List;

public class MessageThread {
    private String id;
    private String title;
    private String lastMessage;
    private String time;
    private int unreadCount;
    private boolean isGroupChat;
    private List<String> avatarUrls;

    public MessageThread(String id, String title, String lastMessage, String time, int unreadCount, boolean isGroupChat, List<String> avatarUrls) {
        this.id = id;
        this.title = title;
        this.lastMessage = lastMessage;
        this.time = time;
        this.unreadCount = unreadCount;
        this.isGroupChat = isGroupChat;
        this.avatarUrls = avatarUrls;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getLastMessage() { return lastMessage; }
    public String getTime() { return time; }
    public int getUnreadCount() { return unreadCount; }
    public boolean isGroupChat() { return isGroupChat; }
    public List<String> getAvatarUrls() { return avatarUrls; }
}
