package com.example.bookify.data.model;

public class NotificationModel {

    public enum NotificationType {
        LIKE,
        FOLLOW,
        GOAL,
        COMMENT,
        CLUB,
        HEADER
    }

    private String id;
    private String userName;
    private String contentText;
    private String timeAgo;
    private int avatarResId;
    private NotificationType type;
    private boolean isUnread;
    private String sectionTitle; 
    private boolean isFollowing;

    public NotificationModel(String sectionTitle) {
        this.type = NotificationType.HEADER;
        this.sectionTitle = sectionTitle;
    }

    public NotificationModel(String id, String userName, String contentText, String timeAgo, int avatarResId, NotificationType type, boolean isUnread, boolean isFollowing) {
        this.id = id;
        this.userName = userName;
        this.contentText = contentText;
        this.timeAgo = timeAgo;
        this.avatarResId = avatarResId;
        this.type = type;
        this.isUnread = isUnread;
        this.isFollowing = isFollowing;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getContentText() {
        return contentText;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public NotificationType getType() {
        return type;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public void setUnread(boolean unread) {
        isUnread = unread;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
